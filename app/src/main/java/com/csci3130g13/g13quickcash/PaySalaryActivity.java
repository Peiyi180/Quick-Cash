package com.csci3130g13.g13quickcash;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.csci3130g13.g13quickcash.utils.FirebaseDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Pay salary page using by employer. Integrate Paypal SDK
 *
 * @author      Peiyi Jiang <pjiang@dal.ca>
 */
public class PaySalaryActivity extends AppCompatActivity {
    /* Code learned from CSCI 3130 Paypal Tutorial, URL: https://dal.brightspace.com/d2l/le/content/201532/viewContent/2946085/View,
        Date accessed: Mar 28, 2022
     */
    private static final String TAG = PaySalaryActivity.class.getName();
    private FirebaseDB fbDB = new FirebaseDB();
    private UserMetricManager userMetricManager = new UserMetricManager(fbDB);
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private PayPalConfiguration config;
    private Employer employer;
    private Employee employee;
    private int salary;

    private EditText amountET;
    private EditText employeeIdET;
    private Button payBtn;
    private TextView paymentStatusTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_salary);

        init();
        initActivityLauncher();
        setListeners();
    }

    /**
     * Init Activity by binding params
     *
     * @return void
     */
    private void init() {
        employer = (Employer) getIntent().getSerializableExtra("EmployerTag");

        payBtn = findViewById(R.id.payButton);
        amountET = findViewById(R.id.amountET);
        employeeIdET = findViewById(R.id.employeeIdET);
        paymentStatusTV = findViewById(R.id.paymentStatusTV);
        Button backToEmployerPageBtn = findViewById(R.id.backToEmployerBtn);
        backToEmployerPageBtn.setOnClickListener(view -> redirectToLandingEmployerPage());
    }

    /**
     * Redirect to Landing Employer Page
     *
     * @return void
     */
    private void redirectToLandingEmployerPage() {
        Intent employerLandingIntent = new Intent(this, LandingPageEmployerActivity.class);
        employerLandingIntent.putExtra("EmployerTag", employer);
        startActivity(employerLandingIntent);
    }

    /**
     * Config Paypal object
     *
     * @return void
     */
    private void configPayPal(String clientId) {
        config = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(clientId);
    }


    /**
     * Config Activity Launcher
     *
     * @return void
     */
    private void initActivityLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    int resultCode = result.getResultCode();
                    if (resultCode == RESULT_OK) {
                        PaymentConfirmation confirmation = result.getData().getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                        if (confirmation != null) {
                            try {
                                String paymentInformation = confirmation.toJSONObject().toString(4);
                                JSONObject paymentObj = new JSONObject(paymentInformation);

                                String payID = paymentObj.getJSONObject("response").getString("id");
                                String state = paymentObj.getJSONObject("response").getString("state");
                                paymentStatusTV.setText("Payment " + state + "\n with payment id is " + payID);

                                // Update metrics (reputation and income/expenditure)
                                userMetricManager.updateMetricsPayment(employer, employee, salary);
                            } catch (JSONException e) {
                                Log.e("Error", "failure happens during parsing confirmation result: ", e);
                            }
                        }
                    } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                        Log.d(TAG, "Launch result in valid!");
                        paymentStatusTV.setText("Invalid payee/payer Client ID!");
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Log.d(TAG, "Payment cancelled!");
                        paymentStatusTV.setText("Payment cancelled!");
                    }

                });
    }


    /**
     * Bind paypal payment process to Pay button
     *
     * @return void
     */
    private void setListeners() {
        payBtn.setOnClickListener(view -> processPayment());
    }

    /**
     * Get information needed for payment and pass it to payment intent
     * then redirect to payment activity
     *
     * @return void
     */
    private void processPayment() {
        String salaryAmount = amountET.getText().toString();
        String employeeId = employeeIdET.getText().toString();
        salary = new Integer(salaryAmount);

        if (employeeId.isEmpty() || salaryAmount.isEmpty()) {
            Toast.makeText(this, "Please input employee id and salary amount" , Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference userRef = DBConst.dbRef.child("users").child("employee").child(employeeId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    toastEmployeeNotExist();
                } else {
                    employee = snapshot.getValue(Employee.class);
                    if (employee.getPaypalClientID().isEmpty()) {
                        toastClientIdNotSetting();
                    } else {
                        String clientId = employee.getPaypalClientID();
                        configPayPal(clientId);
                        redirectToPaypalIntent(salaryAmount);
                    }
                }


                userRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userRef.removeEventListener(this);
            }
        });

    }


    /**
     * Notify user the clint id is missing
     */
    private void toastClientIdNotSetting() {
        Toast.makeText(this, "Paypal Client ID is not setting. Please ask your employee to update it!" , Toast.LENGTH_SHORT).show();
    }

    /**
     * Notify user the employee is not our user
     */
    private void toastEmployeeNotExist() {
        Toast.makeText(this, "Employee id is not correct!" , Toast.LENGTH_SHORT).show();
    }

    /**
     * Go to Paypal authentication page
     *
     * @param salaryAmount - Salary amount input by employer
     */
    private void redirectToPaypalIntent(String salaryAmount){

        startService(new Intent(this, PayPalService.class).putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config));

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(
                salaryAmount), "CAD", "Pay Salary to " + employee.getName(), PayPalPayment.PAYMENT_INTENT_SALE);

        Intent startPaymentActivityIntent = new Intent(this,  PaymentActivity.class);

        startPaymentActivityIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startPaymentActivityIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        activityResultLauncher.launch(startPaymentActivityIntent);

    }

    @Override
    protected void onResume() {

        super.onResume();
        stopService(new Intent(this, PayPalService.class));

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        stopService(new Intent(this, PayPalService.class));

    }

}