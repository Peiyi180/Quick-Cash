package com.csci3130g13.g13quickcash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class AlertSettingsActivity extends AppCompatActivity {

    private Spinner reqSkillSpinner;
    private Spinner hireTypeSpinner;
    private ArrayAdapter<CharSequence> reqSkillSpinnerAdapter;
    private ArrayAdapter<CharSequence> hireTypeSpinnerAdapter;
    private Switch alertSwitch;
    private EditText cityET;
    private Button backBtn;
    private Button saveBtn;

    private Map<String, String> alertTopicMap;
    private String prefHireType = "";
    private String prefReqSkill = "";
    private String prefCity = "";
    protected Employee employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_settings);

        employee = (Employee) getIntent().getSerializableExtra("employee");

        getUI();
        initSpinners();
        loadSettings();

        backBtn.setOnClickListener(view -> {
            finish();
        });

        saveBtn.setOnClickListener(view -> {

            unsubscribeFromTopics();

            prefCity = cityET.getText().toString();
            alertTopicMap = buildAlertTopicMap();
            employee.setAlertSettings(alertTopicMap);
            DBConst.dbRef.child("users/employee/" + employee.getId() + "/alertSettings" ).setValue(alertTopicMap);

            subscribeToTopics(alertTopicMap);

            startActivity(new Intent(this, LandingPageEmployeeActivity.class).putExtra("EmployeeTag", employee));
            finish();

        });


    }

    private void unsubscribeFromTopics() {

        Map<String, String> oldSettings = employee.getAlertSettings();

        if(oldSettings.isEmpty()) { return; }

        FirebaseMessaging.getInstance().unsubscribeFromTopic(oldSettings.get(ConstantLabels.TOPIC_CITY));
        FirebaseMessaging.getInstance().unsubscribeFromTopic(oldSettings.get(ConstantLabels.TOPIC_HIRE_TYPE));
        FirebaseMessaging.getInstance().unsubscribeFromTopic(oldSettings.get(ConstantLabels.TOPIC_REQ_SKILL));

    }

    private void subscribeToTopics(Map<String, String> alertTopicMap){

        if(alertTopicMap.isEmpty()) { return; }

        FirebaseMessaging.getInstance().subscribeToTopic(alertTopicMap.get(ConstantLabels.TOPIC_CITY));
        FirebaseMessaging.getInstance().subscribeToTopic(alertTopicMap.get(ConstantLabels.TOPIC_HIRE_TYPE));
        FirebaseMessaging.getInstance().subscribeToTopic(alertTopicMap.get(ConstantLabels.TOPIC_REQ_SKILL));

    }

    private void getUI(){

        reqSkillSpinner = (Spinner) findViewById(R.id.reqSkillSpinner_alert);
        hireTypeSpinner = (Spinner) findViewById(R.id.hireTypeSpinner_alert);
        alertSwitch = (Switch) findViewById(R.id.alertSwitch);
        cityET = (EditText) findViewById(R.id.cityET_alert);
        backBtn = (Button) findViewById(R.id.backBtn_alert);
        saveBtn = (Button) findViewById(R.id.saveBtn_alert);

    }

    private void loadSettings(){

        Map<String, String> settingsMap = employee.getAlertSettings();

        if(settingsMap.isEmpty()){
            return;
        }

        alertSwitch.setChecked(true);
        cityET.setText(settingsMap.get(ConstantLabels.TOPIC_CITY));
        reqSkillSpinner.setSelection(reqSkillSpinnerAdapter.getPosition(settingsMap.get(ConstantLabels.TOPIC_REQ_SKILL)));
        hireTypeSpinner.setSelection(hireTypeSpinnerAdapter.getPosition(settingsMap.get(ConstantLabels.TOPIC_HIRE_TYPE)));

        return;

    }

    private void initSpinners(){

        reqSkillSpinnerAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.business_type_array_alert));
        reqSkillSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reqSkillSpinner.setAdapter(reqSkillSpinnerAdapter);

        hireTypeSpinnerAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.employee_type_array_alert));
        hireTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hireTypeSpinner.setAdapter(hireTypeSpinnerAdapter);

        reqSkillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                if (pos == 0) {
                    prefReqSkill = "";
                } else {
                    prefReqSkill = parent.getItemAtPosition(pos).toString();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        hireTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                if (pos == 0) {
                    prefHireType = "";
                } else {
                    prefHireType = parent.getItemAtPosition(pos).toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

    }

    private Map<String, String> buildAlertTopicMap(){

        Map<String, String> result = new HashMap<>();

        if(!alertSwitch.isChecked()){
            return result;
        }

        if(!prefCity.equals("")){
            result.put(ConstantLabels.TOPIC_CITY, prefCity);
        } else {
            result.put(ConstantLabels.TOPIC_CITY, "anycity");
        }

        if(!prefHireType.equals("")){
            result.put(ConstantLabels.TOPIC_HIRE_TYPE, prefHireType);
        } else {
            result.put(ConstantLabels.TOPIC_HIRE_TYPE, "anyhiretype");
        }

        if(!prefReqSkill.equals("")){
            result.put(ConstantLabels.TOPIC_REQ_SKILL, prefReqSkill);
        } else {
            result.put(ConstantLabels.TOPIC_REQ_SKILL, "anyreqskill");
        }

        return result;

    }



}