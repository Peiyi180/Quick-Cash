package com.csci3130g13.g13quickcash;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

public class ViewEmployeeMetricsActivity extends AppCompatActivity {

    protected JobHistoryAdapter jobHistoryAdapter;
    protected RecyclerView jobHistoryRecyclerView;

    private TextView incomeTV;
    private TextView ratingsTV;
    private Employee employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_metrics_employee);

        getUI();

        employee = (Employee) getIntent().getSerializableExtra("EmployeeTag");

        Query jobRef = DBConst.dbRef.child("jobs");

        final FirebaseRecyclerOptions<Job> options = new FirebaseRecyclerOptions.Builder<Job>()
                .setQuery(jobRef, Job.class)
                .build();

        jobHistoryAdapter = new JobHistoryAdapter(options, this, employee);
        jobHistoryRecyclerView.setAdapter(jobHistoryAdapter);

        try{
            incomeTV.setText(String.valueOf(employee.getTotalIncome()));
            ratingsTV.setText(String.valueOf(employee.getReputation()));
        } catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    private void getUI() {
        incomeTV = (TextView) findViewById(R.id.incomeTV);
        ratingsTV = (TextView) findViewById(R.id.ratingsTV2);
        jobHistoryRecyclerView = findViewById(R.id.jobHistoryRecyclerView2);
        jobHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        jobHistoryAdapter.startListening();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        jobHistoryAdapter.stopListening();
    }

}
