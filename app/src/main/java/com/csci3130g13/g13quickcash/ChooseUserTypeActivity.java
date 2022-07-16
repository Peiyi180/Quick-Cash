package com.csci3130g13.g13quickcash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Activity for setting the user type of newly registered users.
 */


public class ChooseUserTypeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user_type);


        Button employeeBtn = findViewById(R.id.buttonChooseEmployee);
        Button employerBtn = findViewById(R.id.buttonChooseEmployer);

        // get the serializable LoginCredential that was passed as an Extra to the intent that started this activity.
        LoginCredential newUserCredential = (LoginCredential) getIntent().getSerializableExtra("newCredentialTag");
        String newUserID = newUserCredential.getId();

        employeeBtn.setOnClickListener(view -> {

            // Set user type and save to DB/credentials and DB/users/employees

            User newEmployee = new Employee(newUserID);
            newUserCredential.setUsertype("employee");
            DBConst.dbRef.child("credentials").child(newUserID).setValue(newUserCredential);

            //redirect to Employee Preference configuration activity

            Intent employeePrefIntent = new Intent(this, SetEmployeePreferencesActivity.class);
            employeePrefIntent.putExtra("newEmployeeTag", newEmployee);
            startActivity(employeePrefIntent);
            finish();

        });

        employerBtn.setOnClickListener(view -> {

            User newEmployer = new Employer(newUserID);
            newUserCredential.setUsertype("employer");
            DBConst.dbRef.child("credentials").child(newUserID).setValue(newUserCredential);

            Intent employerPrefIntent = new Intent(this, SetEmployerPreferencesActivity.class);
            employerPrefIntent.putExtra("newEmployerTag", newEmployer);
            startActivity(employerPrefIntent);
            finish();

        });


    }

}