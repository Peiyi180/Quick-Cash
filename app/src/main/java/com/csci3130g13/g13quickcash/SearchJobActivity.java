package com.csci3130g13.g13quickcash;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchJobActivity extends AppCompatActivity {

    protected Double minSalary;
    protected Long startDate_sec_start;
    protected Long startDate_sec_end;
    protected Long endDate_sec_start;
    protected Long endDate_sec_end;
    protected String hireType;
    protected String reqSkill;
    protected String city;
    protected Double workHours_start;
    protected Double workHours_end;

    private EditText minSalaryET;
    private EditText startDateET_start;
    private EditText startDateET_end;
    private EditText endDateET_start;
    private EditText endDateET_end;
    private EditText cityET;
    private Spinner hireTypeSpinner;
    private Spinner reqSkillSpinner;
    private EditText workHours_startET;
    private EditText workHours_endET;
    private Button backBtn;
    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_job);

        //get UI

        getUI();

        //setup UI

        setupUI();

        //spinner listeners

        hireTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                hireType = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        reqSkillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                reqSkill = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        backBtn.setOnClickListener(view -> {
            setResult(0);
            finish();
        });

        submitBtn.setOnClickListener(view -> {


            extractInput();

            if(validateInput()) {

                //if the input is valid, pack them up and send them back as Extras to the activity that called this activity.

                Intent resultIntent = new Intent(this, getCallingActivity().getClass());
                resultIntent.putExtra("minSalary", minSalary);
                resultIntent.putExtra("startDate_sec_start", startDate_sec_start);
                resultIntent.putExtra("startDate_sec_end", startDate_sec_end);
                resultIntent.putExtra("endDate_sec_start", endDate_sec_start);
                resultIntent.putExtra("endDate_sec_end", endDate_sec_end);
                resultIntent.putExtra("hireType", hireType);
                resultIntent.putExtra("reqSkill", reqSkill);
                resultIntent.putExtra("city", city);
                resultIntent.putExtra("workHours_start", workHours_start);
                resultIntent.putExtra("workHours_end", workHours_end);
                setResult(1, resultIntent);
                finish();

            }

        });

    }

    /** extract the user input into the class variables, setting it to -1 if the input field is empty.

     */
    private void extractInput() {

        if(!minSalaryET.getText().toString().equals("")) {
            minSalary = Double.parseDouble(minSalaryET.getText().toString());
        } else {
            minSalary = -1.0;
        }

        if(!startDateET_start.getText().toString().equals("")) {
            startDate_sec_start = getDateSeconds(startDateET_start.getText().toString());
        } else {
            startDate_sec_start = -1L;
        }

        if(!startDateET_end.getText().toString().equals("")) {
            startDate_sec_end = getDateSeconds(startDateET_end.getText().toString());
        } else {
            startDate_sec_end = -1L;
        }

        if(!endDateET_start.getText().toString().equals("")){
            endDate_sec_start = getDateSeconds(endDateET_start.getText().toString());
        } else {
            endDate_sec_start = -1L;
        }

        if(!endDateET_end.getText().toString().equals("")) {
            endDate_sec_end = getDateSeconds(endDateET_end.getText().toString());
        } else {
            endDate_sec_end = -1L;
        }

        if(!workHours_startET.getText().toString().equals("")){
            workHours_start = Double.parseDouble(workHours_startET.getText().toString());
        } else {
            workHours_start = -1.0;
        }

        if(!workHours_endET.getText().toString().equals("")) {
            workHours_end = Double.parseDouble(workHours_endET.getText().toString());
        } else {
            workHours_end = -1.0;
        }

        if(!cityET.getText().toString().isEmpty()){
            city = cityET.getText().toString();
        } else {
            city = "Not specified";
        }

    }

    /** Helper method for converting the string date format into seconds.
     */
    private long getDateSeconds(String date){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date parsedDate = null;
        long seconds = 0;

        if(date.equals("")){
            return seconds;
        }

        try {
            parsedDate = dateFormat.parse(date);
            if(parsedDate != null){
                seconds = parsedDate.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return seconds;
    }


    private void getUI() {
        cityET = findViewById(R.id.cityET_search);
        minSalaryET = (EditText) findViewById(R.id.salaryMinET);
        startDateET_start = (EditText) findViewById(R.id.startDateET_start);
        startDateET_end = (EditText) findViewById(R.id.startDateET_end);
        endDateET_start = (EditText) findViewById(R.id.endDateET_start);
        endDateET_end = (EditText) findViewById(R.id.endDateET_end);
        hireTypeSpinner = (Spinner) findViewById(R.id.hireTypeSpinner);
        reqSkillSpinner = (Spinner) findViewById(R.id.reqSkillSpinner);
        workHours_startET = (EditText) findViewById(R.id.workHours_start);
        workHours_endET = (EditText) findViewById(R.id.workHours_end);

        backBtn = (Button) findViewById(R.id.backBtn);
        submitBtn = (Button) findViewById(R.id.postSearchBtn);
    }


    private void setupUI() {
        ArrayAdapter<CharSequence> hireTypeAdapter =
                new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.employee_type_array_search));
        hireTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hireTypeSpinner.setAdapter(hireTypeAdapter);

        ArrayAdapter<CharSequence> reqSkillAdapter =
                new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.business_type_array_search));
        reqSkillAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reqSkillSpinner.setAdapter(reqSkillAdapter);

        startDateET_start.setInputType(InputType.TYPE_NULL);
        startDateET_end.setInputType(InputType.TYPE_NULL);
        endDateET_start.setInputType(InputType.TYPE_NULL);
        endDateET_end.setInputType(InputType.TYPE_NULL);

        startDateET_start.setOnClickListener(view -> onDateEditTextClicked(view));
        startDateET_end.setOnClickListener(view -> onDateEditTextClicked(view));
        endDateET_start.setOnClickListener(view -> onDateEditTextClicked(view));
        endDateET_end.setOnClickListener(view -> onDateEditTextClicked(view));
    }

    /** Open up the DatePickerDialog when the Date field is clicked

     */
    private void onDateEditTextClicked(View view) {

        DatePickerDialog dp = new DatePickerDialog(SearchJobActivity.this);

        dp.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                //DatePicker counts from month 0, so 1 has to be added to the month.

                ((EditText) view).setText(day + "/" + (month + 1) + "/" + year);

            }
        });

        dp.show();

    }

    public boolean validateInput(){

        Log.d("validate_minSalary", minSalary.toString());
        Log.d("validate_startDate_sec_start", startDate_sec_start.toString());
        Log.d("validate_startDate_sec_end", startDate_sec_end.toString());
        Log.d("validate_hireType", hireType);
        Log.d("validate_workHours_start", workHours_start.toString());

        String errorMsg = "";

        if(startDate_sec_end < startDate_sec_start) {
            errorMsg = "start date range invalid.";
        }

        if((startDate_sec_end == -1) ^ (startDate_sec_start == -1)){
            errorMsg = "start date range invalid.";
        }

        if(endDate_sec_end < endDate_sec_start) {
            errorMsg = "end date range invalid.";
        }

        if((endDate_sec_end == -1) ^ (endDate_sec_start == -1)){
            errorMsg = "end date range invalid.";
        }

        if((workHours_end < workHours_start) || workHours_start > 24 || workHours_end > 24) {
            errorMsg = "work hours range invalid.";
        }

        if((workHours_start == -1) ^ (workHours_end == -1)){
            errorMsg = "work hours range invalid.";
        }


        if (errorMsg.equals("")) {
            return true;
        } else {
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            return false;
        }

    }

}