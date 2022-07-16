package com.csci3130g13.g13quickcash;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;


/** SetEmployeePreferencesActivity
 *  Settings page for saving preferences of new Employees.
 */
public class SetEmployeePreferencesActivity extends AppCompatActivity {

    private Employee employee = null;
    private Employee newEmployee = null;
    private Employee oldEmployee = null;
    private String errorMsg = "";
    private String city = "";
    private TextView cityTV;

    protected ActivityResultLauncher<Intent> locationLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                if(result.getResultCode() == 1){

                    city = result.getData().getStringExtra("loc_city");
                    cityTV.setText("City: " + city);
                    employee.setCity(city);
                } else {
                    cityTV.setText("You must enter city to continue with the registration.");
                }

            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_employee_preferences);

        newEmployee = (Employee) getIntent().getSerializableExtra("newEmployeeTag");

        // this activity may be started from either ChooseUserTypeActivity for new users, or LandingPageEmployeeActivity for old users.
        // Check which case it is by examining the tag of the intent that started it.

        if(newEmployee == null){
            oldEmployee = (Employee) getIntent().getSerializableExtra("oldEmployeeTag");
            employee = oldEmployee;
        } else {
            employee = newEmployee;
        }

        cityTV = (TextView) findViewById(R.id.cityTV);
        EditText nameField = (EditText) findViewById(R.id.nameField);
        EditText emailField = (EditText) findViewById(R.id.emailField);
        EditText paypalClientIdField = (EditText) findViewById(R.id.paypalClientIdET);

        // business type drop-down box
        Spinner professionTypeSpinner = (Spinner) findViewById(R.id.businessTypeSpinner);
        ArrayAdapter<CharSequence> professionTypeAdapter =
                new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.business_type_array));
        professionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professionTypeSpinner.setAdapter(professionTypeAdapter);

        // job preference (part-time/full-time) drop-down box
        Spinner prefEmployeeTypeSpinner = (Spinner) findViewById(R.id.prefEmployeeTypeSpinner);
        ArrayAdapter<CharSequence> prefEmployeeTypeAdapter =
                new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.employee_type_array));
        prefEmployeeTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prefEmployeeTypeSpinner.setAdapter(prefEmployeeTypeAdapter);


        // In case an old employee started this activity to modify his preferences, load his saved preferences from the DB.

        if(oldEmployee != null) {
            nameField.setText(oldEmployee.getName());
            emailField.setText(oldEmployee.getEmail());
            paypalClientIdField.setText(oldEmployee.getPaypalClientID());
            city = oldEmployee.getCity();
            cityTV.setText("City: " + city);
            professionTypeSpinner.setSelection(professionTypeAdapter.getPosition(oldEmployee.getProfession()));
            prefEmployeeTypeSpinner.setSelection(prefEmployeeTypeAdapter.getPosition(oldEmployee.getJobPreference1()));
        }


        professionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Log.i("Selected Profession", parent.getItemAtPosition(pos).toString());
                employee.setProfession(parent.getItemAtPosition(pos).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });


        prefEmployeeTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Log.i("Selected Job Preference", parent.getItemAtPosition(pos).toString());
                employee.setJobPreference1(parent.getItemAtPosition(pos).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        Button locRedirectBtn = (Button) findViewById(R.id.getLocBtn);
        locRedirectBtn.setOnClickListener(view -> locationLauncher.launch(new Intent (this, LocationActivity.class)));

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> {

            // check for empty fields. if there are none, then proceed with redirection to landing page.

            errorMsg = "";
            if(nameField.getText().toString().equals("")){
                errorMsg = "Must enter name.";
            } else if (emailField.getText().toString().equals("")){
                errorMsg = "Must enter email.";
            } else if (city.isEmpty()){
                errorMsg = "Must enter city.";
            }

            if(errorMsg.equals("")){
                employee.setName(nameField.getText().toString());
                employee.setEmail(emailField.getText().toString());
                employee.setPaypalClientID(paypalClientIdField.getText().toString());
                saveAndRedirectToLandingPage();
            } else {
                Toast.makeText(SetEmployeePreferencesActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }

        });



    }

    /** Redirect to Landing Page. */

    private void saveAndRedirectToLandingPage(){
        DBConst.dbRef.child("users").child("employee").child(employee.getId()).setValue(employee);
        Intent startLandingPageIntent = new Intent(this, LandingPageEmployeeActivity.class);
        startLandingPageIntent.putExtra("EmployeeTag", employee);
        startActivity(startLandingPageIntent);
    }


    // for test purposes only

    public Employee getEmployee(){
        return employee;
    }

}