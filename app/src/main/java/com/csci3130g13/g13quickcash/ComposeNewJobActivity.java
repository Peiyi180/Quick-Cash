package com.csci3130g13.g13quickcash;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity for composing a new job
 */

public class ComposeNewJobActivity extends AppCompatActivity {

    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";

    protected EditText jobStartDateField;
    protected EditText jobEndDateField;
    protected EditText jobDescriptionField;
    protected EditText salaryField;
    protected EditText hoursPerDayField;
    protected EditText positionNameField;
    protected Button postJobBtn;
    protected Spinner wantedEmployeeTypeSpinner;
    protected Spinner wantedSkillSpinner;
    protected Employer employer;
    protected Job newJob;
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private RequestQueue volleyRequestQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_new_job);

        volleyRequestQ = Volley.newRequestQueue(this);

        employer = (Employer) getIntent().getSerializableExtra("EmployerTag");
        newJob =  new Job(employer);

        positionNameField = (EditText) findViewById(R.id.positionName);
        jobStartDateField = (EditText) findViewById(R.id.jobStartDate);
        jobEndDateField = (EditText) findViewById(R.id.jobEndDate);
        jobDescriptionField = (EditText) findViewById(R.id.jobDescField);
        salaryField = (EditText) findViewById(R.id.salaryField);
        hoursPerDayField = (EditText) findViewById(R.id.jobHoursPerDay);
        postJobBtn = (Button) findViewById(R.id.postButton);

        jobStartDateField.setInputType(InputType.TYPE_NULL);
        jobEndDateField.setInputType(InputType.TYPE_NULL);

        wantedEmployeeTypeSpinner = (Spinner) findViewById(R.id.wantedEmployeeTypeSpinner);
        ArrayAdapter<CharSequence> wantedEmployeeTypeAdapter =
                new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.employee_type_array));
        wantedEmployeeTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wantedEmployeeTypeSpinner.setAdapter(wantedEmployeeTypeAdapter);

        wantedSkillSpinner = (Spinner) findViewById(R.id.wantedSkillSpinner);
        ArrayAdapter<CharSequence> wantedSkillAdapter =
                new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.business_type_array));
        wantedSkillAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wantedSkillSpinner.setAdapter(wantedSkillAdapter);

        jobStartDateField.setOnClickListener(view -> onDateEditTextClicked(view));
        jobEndDateField.setOnClickListener(view -> onDateEditTextClicked(view));

        wantedEmployeeTypeSpinner.setSelection(wantedEmployeeTypeAdapter.getPosition(employer.getHirePreference1()));
        wantedSkillSpinner.setSelection(wantedSkillAdapter.getPosition(employer.getBusinessType()));

        wantedEmployeeTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                newJob.setPrefEmployeeType(parent.getItemAtPosition(pos).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        wantedSkillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                newJob.setPrefSkill(parent.getItemAtPosition(pos).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        postJobBtn.setOnClickListener(view -> verifyInput());

    }

    private void onDateEditTextClicked(View view) {

        DatePickerDialog dp = new DatePickerDialog(ComposeNewJobActivity.this);

        dp.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                //DatePicker counts from month 0, so 1 has to be added to the month.

                ((EditText) view).setText(day + "/" + (month + 1) + "/" + year);

            }
        });

        dp.show();

    }

    private void verifyInput() {

        String errorMsg = "";

        if (jobStartDateField.getText().toString().equals("")) {
            errorMsg = "Must enter start date.";
        } else if (jobEndDateField.getText().toString().equals("")) {
            errorMsg = "Must enter end date.";
        } else if (jobDescriptionField.getText().toString().equals("")) {
            errorMsg = "Must enter job description.";
        } else if (salaryField.getText().toString().equals("")) {
            errorMsg = "Must enter salary.";
        } else if (hoursPerDayField.getText().toString().equals("")) {
            errorMsg = "Must enter hours per day.";
        } else if (positionNameField.getText().toString().equals("")) {
            errorMsg = "Must enter position name.";
        }

        try {
            Date startDate = dateFormat.parse(jobStartDateField.getText().toString());
            Date endDate = dateFormat.parse(jobEndDateField.getText().toString());
            if(startDate.after(endDate)){
                errorMsg = "Job start date is after the end date.";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(Double.parseDouble(hoursPerDayField.getText().toString()) > 24) {
            errorMsg = "Job hours are more than 24 hours!";
        }

        if (errorMsg.equals("")) {
            postNewJob();
        } else {
            Toast.makeText(ComposeNewJobActivity.this, errorMsg, Toast.LENGTH_LONG).show();
        }

    }

    private void postNewJob(){


        newJob.setPositionName(positionNameField.getText().toString());
        newJob.setSalary(Double.parseDouble(salaryField.getText().toString()));
        newJob.setJobDesc(jobDescriptionField.getText().toString());
        newJob.setStartDate(jobStartDateField.getText().toString());
        newJob.setEndDate(jobEndDateField.getText().toString());
        Date startDate = newJob.fetchFullStartDate();
        Date endDate = newJob.fetchFullEndDate();
        newJob.setStartDate_sec(startDate.getTime());
        newJob.setEndDate_sec(endDate.getTime());
        newJob.setWorkHours(Double.parseDouble(hoursPerDayField.getText().toString()));

        DatabaseReference jobPath = DBConst.dbRef.child("jobs").push();

        newJob.setKey(jobPath.getKey());

        // update the job entry under "root/jobs"
        jobPath.setValue(newJob);

        // add the job to the jobs list in employer entry under "root/users/employer/employerID/jobs"
        DBConst.dbRef.child("users").child("employer").child(employer.getId()).child("jobs").child(jobPath.getKey()).setValue("N/A");

        sendJobNotification(newJob);

        Intent startLandingPageIntent = new Intent(this, LandingPageEmployerActivity.class);
        startLandingPageIntent.putExtra("EmployerTag", employer);
        startActivity(startLandingPageIntent);

    }


    /** Sends a notification through FCM when a new Job is posted.
     *  @param job the new job created.
     *
     *  References CSCI3130 Tutorial: Push Notifications by Dhrumil Shah (Tutorial TA).
     *  https://dal.brightspace.com/d2l/le/content/201532/viewContent/2968827/View (Date Accessed: April 1st, 2022)
     */

    private void sendJobNotification(Job job){

            try{

                JSONObject notification = new JSONObject();
                notification.put("title", "New matching job has been posted.");
                notification.put("body", "Position Name: " + job.getPositionName() + ", Salary: $" + job.getSalary());


                JSONObject messageThisCity = new JSONObject();

                StringBuilder cond = new StringBuilder();
                cond.append("'" + job.getCity() + "' in topics && (");
                cond.append("'anyreqskill' in topics || ");
                cond.append("'" + job.getPrefSkill() + "' in topics) && (");
                cond.append("'anyhiretype' in topics || ");
                cond.append("'" + job.getPrefEmployeeType() + "' in topics)");

                messageThisCity.put("condition", cond.toString());
                messageThisCity.put("notification", notification);

                JSONObject messageAllCity = new JSONObject();

                cond = new StringBuilder();
                cond.append("'anycity' in topics && (");
                cond.append("'anyreqskill' in topics || ");
                cond.append("'" + job.getPrefSkill() + "' in topics) && (");
                cond.append("'anyhiretype' in topics || ");
                cond.append("'" + job.getPrefEmployeeType() + "' in topics)");

                messageAllCity.put("condition", cond.toString());
                messageAllCity.put("notification", notification);

                JsonObjectRequest allCityRequest = new JsonObjectRequest(Request.Method.POST, FCM_URL, messageAllCity,
                        response -> { Log.d("job_post_notification_response", response.toString()); } , e -> { Log.e("job_notification_error", e.getMessage()); }) {

                                @Override
                                public Map<String, String> getHeaders() {
                                    Map<String, String> newHeader = new HashMap<>();
                                    newHeader.put("content-type", "application/json");
                                    newHeader.put("authorization", "key=" + BuildConfig.SERVER_FCM_KEY);
                                    return newHeader;
                                }
                        };

                JsonObjectRequest thisCityRequest = new JsonObjectRequest(Request.Method.POST, FCM_URL, messageThisCity,
                        response -> { Log.d("job_post_notification_response", response.toString()); } , e -> { Log.e("job_notification_error", e.getMessage());}) {

                                @Override
                                public Map<String, String> getHeaders() {
                                    Map<String, String> newHeader = new HashMap<>();
                                    newHeader.put("content-type", "application/json");
                                    newHeader.put("authorization", "key=" + BuildConfig.SERVER_FCM_KEY);
                                    return newHeader;
                                }
                        };

                volleyRequestQ.add(allCityRequest);
                volleyRequestQ.add(thisCityRequest);

            } catch (JSONException e){
                e.printStackTrace();
            }


    }

}