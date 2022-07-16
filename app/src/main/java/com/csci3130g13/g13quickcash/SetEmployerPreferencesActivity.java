package com.csci3130g13.g13quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

/** SetEmployerPreferencesActivity
 *  Settings page for saving preferences of new Employers.
 *  This class closely parallels SetEmployeePreferencesActivity, so no additional comments are provided.
 */
public class SetEmployerPreferencesActivity extends AppCompatActivity {

    // This class closely parallels SetEmployeePreferencesActivity, so no additional comments are provided.
    // Please refer to SetEmployeePreferencesActivity.

    private Employer employer = null;
    private Employer newEmployer = null;
    private Employer oldEmployer = null;
    private String errorMsg = "";
    private String city = "";
    private TextView cityTV;

    protected ActivityResultLauncher<Intent> locationLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                if(result.getResultCode() == 1){

                    city = result.getData().getStringExtra("loc_city");
                    cityTV.setText("City: " + city);
                    employer.setCity(city);
                }

            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_employer_preferences);

        newEmployer = (Employer) getIntent().getSerializableExtra("newEmployerTag");

        if(newEmployer == null){
            oldEmployer = (Employer) getIntent().getSerializableExtra("oldEmployerTag");
            employer = oldEmployer;
        } else {
            employer = newEmployer;
        }
        cityTV = (TextView) findViewById(R.id.cityTV2);
        EditText nameField = (EditText) findViewById(R.id.nameField);
        EditText emailField = (EditText) findViewById(R.id.emailField);
        EditText businessNameField = (EditText) findViewById(R.id.businessNameField);
        EditText creditCardNumberField = (EditText) findViewById(R.id.creditCardNumberField);
        Button deleteBtn = (Button) findViewById(R.id.deleteBtn);

        Spinner businessTypeSpinner = (Spinner) findViewById(R.id.businessTypeSpinner);
        ArrayAdapter<CharSequence> businessTypeAdapter =
                new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.business_type_array));
        businessTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        businessTypeSpinner.setAdapter(businessTypeAdapter);

        Spinner prefEmployeeTypeSpinner = (Spinner) findViewById(R.id.prefEmployeeTypeSpinner);
        ArrayAdapter<CharSequence> prefEmployeeTypeAdapter =
                new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.employee_type_array));
        prefEmployeeTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prefEmployeeTypeSpinner.setAdapter(prefEmployeeTypeAdapter);

        if(oldEmployer != null) {
            nameField.setText(oldEmployer.getName());
            emailField.setText(oldEmployer.getEmail());
            businessNameField.setText(oldEmployer.getBusinessName());
            city = oldEmployer.getCity();
            cityTV.setText("City: " + city);
            businessTypeSpinner.setSelection(businessTypeAdapter.getPosition(oldEmployer.getBusinessType()));
            prefEmployeeTypeSpinner.setSelection(prefEmployeeTypeAdapter.getPosition(oldEmployer.getHirePreference1()));
            if (!oldEmployer.getCreditCardNumber().equals("")) {
                creditCardNumberField.setText(oldEmployer.getCreditCardNumber());
                deleteBtn.setVisibility(View.VISIBLE);
            }
        }


        businessTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Log.i("Selected Business", parent.getItemAtPosition(pos).toString());
                employer.setBusinessType(parent.getItemAtPosition(pos).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        prefEmployeeTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Log.i("Selected Hire Pref.", parent.getItemAtPosition(pos).toString());
                employer.setHirePreference1(parent.getItemAtPosition(pos).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        Button locRedirectBtn = (Button) findViewById(R.id.getLocBtn2);
        locRedirectBtn.setOnClickListener(view -> locationLauncher.launch(new Intent (this, LocationActivity.class)));

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> {

            errorMsg = "";
            if(nameField.getText().toString().equals("")){
                errorMsg = "Must enter name.";
            } else if (emailField.getText().toString().equals("")){
                errorMsg = "Must enter email.";
            } else if (businessNameField.getText().toString().equals("")){
                errorMsg = "Must enter name of business.";
            } else if (city.isEmpty()){
                errorMsg = "Must enter city.";
            }

            if(errorMsg.equals("")){
                employer.setName(nameField.getText().toString());
                employer.setEmail(emailField.getText().toString());
                employer.setBusinessName(businessNameField.getText().toString());
                employer.setCreditCardNumber(creditCardNumberField.getText().toString());
                saveAndRedirectToLandingPage();
            } else {
                Toast.makeText(SetEmployerPreferencesActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }

        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                creditCardNumberField.setText("");
                DBConst.dbRef.child("users").child("employer").child(employer.getId()).child("creditCardNumber").removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SetEmployerPreferencesActivity.this, "Credit card deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(SetEmployerPreferencesActivity.this, "Credit card delete failed", Toast.LENGTH_SHORT).show());

            }

        });

    }

    private void saveAndRedirectToLandingPage(){
        DBConst.dbRef.child("users").child("employer").child(employer.getId()).setValue(employer);
        Intent startLandingPageIntent = new Intent(this, LandingPageEmployerActivity.class);
        startLandingPageIntent.putExtra("EmployerTag", employer);
        startActivity(startLandingPageIntent);
    }


    // for test purposes only

    public Employer getEmployer(){
        return employer;
    }

}