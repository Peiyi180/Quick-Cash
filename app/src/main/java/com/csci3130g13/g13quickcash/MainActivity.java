package com.csci3130g13.g13quickcash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.csci3130g13.g13quickcash.utils.PasswordValidator;
import com.csci3130g13.g13quickcash.utils.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.logging.Level;
import java.util.logging.Logger;

/** Login Activity (MAIN ACTIVITY)
 *  governs login and registration of new users.
 */

public class MainActivity extends AppCompatActivity {

    private String inputId = "";
    private String inputPw = "";
    private LoginCredential loginUserCredential = null;
    private Logger  logger = Logger.getLogger("MainActivity");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginBtn = findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(view -> buttonAction());
        logger.setLevel(Level.INFO);

    }

    private void buttonAction() {

        inputId = ((EditText) findViewById(R.id.idField)).getText().toString();
        inputPw = ((EditText) findViewById(R.id.passwordField)).getText().toString();

        if (validateInput(inputId, inputPw)) {
            checkIfIdExists(inputId);
        }

    }

    /** checks if given id exists in the db credentials. If it does, password is compared.
     * @param id The id being checked.
     */
    protected void checkIfIdExists(String id){
        Query searchById = DBConst.dbRef.child(getString(R.string.CredentialModel)).orderByKey().equalTo(id);

        searchById.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // check if query is nonempty, meaning id is found
                if(snapshot.hasChildren()){
                    comparePassword(id);
                } else {
                    registerNewCredential(inputId, inputPw);
                }
                searchById.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                logger.warning("Check Cred DB Error");
            }
        });
    }

    /** checks if given id has stored password that matches the input password. If it does, login called.
     *  @param id The id for the password being compared. */
    protected void comparePassword(String id){
        Query passwordQuery = DBConst.dbRef.child(getString(R.string.CredentialModel)).child(id).child("password");

        passwordQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String storedPw = snapshot.getValue(String.class);
                if(storedPw.equals(inputPw)){
                    login(id);
                } else {
                    Toast.makeText(MainActivity.this, "Password didn't match!" , Toast.LENGTH_SHORT).show();
                }
                passwordQuery.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                logger.warning("Compare password DB Error");
            }
        });
    }

    /** if id didn't exist in the db, then create a new Credential and proceed with register.
     *  @param id the new user's id
     *  @param pw the new user's password.         */
    protected void registerNewCredential(String id, String pw){
        LoginCredential newCredential = new LoginCredential(id, pw);
        DBConst.dbRef.child(getString(R.string.CredentialModel)).child(id).setValue(newCredential);
        Toast.makeText(this, "Successful Registration! Your credentials have been added to the db." , Toast.LENGTH_SHORT).show();

        // Redirect to user type choice activity

        Intent chooseIntent = new Intent(this, ChooseUserTypeActivity.class);
        chooseIntent.putExtra("newCredentialTag", newCredential);
        startActivity(chooseIntent);
    }

    /** extract usertype from credentials and call redirect function.
     *  @param id the existing user's id
     */
    protected void login(String id){
        Toast.makeText(this, "Successful Login!" , Toast.LENGTH_SHORT).show();

        Query searchById = DBConst.dbRef.child(getString(R.string.CredentialModel)).orderByKey().equalTo(id);

        searchById.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshotChild: snapshot.getChildren()) {
                    loginUserCredential = snapshotChild.getValue(LoginCredential.class);
                    redirectToLandingPage(loginUserCredential.getUsertype(), id);
                }

                searchById.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                logger.warning("extract user type DB Error");
            }
        });
    }

    private void redirectToSettings(String usertype, String id){

        if(usertype.equals("employee")){
            Employee newEmployee = new Employee(id);
            Intent setIntent = new Intent(MainActivity.this, SetEmployeePreferencesActivity.class);
            setIntent.putExtra("newEmployeeTag", newEmployee);
            startActivity(setIntent);
        } else {
            Employer newEmployer = new Employer(id);
            Intent setIntent = new Intent(MainActivity.this, SetEmployerPreferencesActivity.class);
            setIntent.putExtra("newEmployerTag", newEmployer);
            startActivity(setIntent);
        }

    }
    /** redirect to appropriate landing pages, depending on user type.
     *  @param userType the user type of the user.
     *  @param id the existing user's id
     */
    private void redirectToLandingPage(String userType, String id){

        if(userType.equals("employer")){

            Query searchById = DBConst.dbRef.child("users").child("employer").child(id);

            searchById.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Employer employer = snapshot.getValue(Employer.class);

                    if(employer == null){
                        redirectToSettings(userType, id);
                        searchById.removeEventListener(this);
                        return;
                    }

                    Intent employerLandingIntent = new Intent(MainActivity.this, LandingPageEmployerActivity.class);
                    employerLandingIntent.putExtra("EmployerTag", employer);

                    Session.getInstance().setUser(employer);
                    startActivity(employerLandingIntent);

                    searchById.removeEventListener(this);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    logger.warning("Get employer DB Error");
                }
            });


        } else if (userType.equals("employee")){


            Query searchById = DBConst.dbRef.child("users").child("employee").child(id);

            searchById.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Employee employee = snapshot.getValue(Employee.class);

                    if(employee == null){
                        redirectToSettings(userType, id);
                        searchById.removeEventListener(this);
                        return;
                    }

                    Intent employeeLandingIntent = new Intent(MainActivity.this, LandingPageEmployeeActivity.class);
                    employeeLandingIntent.putExtra("EmployeeTag", employee);

                    Session.getInstance().setUser(employee);
                    startActivity(employeeLandingIntent);


                    searchById.removeEventListener(this);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    logger.warning("Get employee DB Error");
                }
            });

        }

    }

    /** validate if the password fits the requirements.
     *  @param password the new password being tested.
     *  @return whether the criteria are met or not.
     */

    protected boolean validateInput(String id, String password){
        TextView statusBox = findViewById(R.id.statusText);
        if (!PasswordValidator.isLengthValid(password)) {
            statusBox.setText(R.string.lengthNotMeetMessage);
            return false;
        }
        if (!PasswordValidator.isAlphanumeric(password)) {
            statusBox.setText(R.string.alphanumericNotMeetMessage);
            return false;
        }
        if (!PasswordValidator.hasSpecialCharacter(password)) {
            statusBox.setText(R.string.specialCharacterNotMeetMessage);
            return false;
        }
        if (!PasswordValidator.hasNoWhiteSpace(password)){
            statusBox.setText(R.string.pwContainsWhitespaceMessage);
            return false;
        }
        if(!PasswordValidator.hasNoWhiteSpace(id)){
            statusBox.setText("ID should not contain whitespace.");
            return false;
        }
        return true;
    }

}