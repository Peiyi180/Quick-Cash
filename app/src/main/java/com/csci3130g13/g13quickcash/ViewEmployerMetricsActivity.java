package com.csci3130g13.g13quickcash;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

public class ViewEmployerMetricsActivity extends AppCompatActivity {

    protected JobHistoryAdapter jobHistoryAdapter;
    protected RecyclerView jobHistoryRecyclerView;

    private TextView expenditureTV;
    private TextView ratingsTV;
    private Employer employer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_metrics_employer);

        getUI();

        employer = (Employer) getIntent().getSerializableExtra("EmployerTag");

        Query jobRef = DBConst.dbRef.child("jobs");

        final FirebaseRecyclerOptions<Job> options = new FirebaseRecyclerOptions.Builder<Job>()
                .setQuery(jobRef, Job.class)
                .build();

        jobHistoryAdapter = new JobHistoryAdapter(options, this, employer);
        jobHistoryRecyclerView.setAdapter(jobHistoryAdapter);

        try {
            expenditureTV.setText(String.valueOf(employer.getTotalExpenditure()));
            ratingsTV.setText(String.valueOf(employer.getReputation()));
        } catch(NullPointerException e){
            e.printStackTrace();
        }

    }

    private void getUI() {
        expenditureTV = (TextView) findViewById(R.id.expenditureTV);
        ratingsTV = (TextView) findViewById(R.id.ratingsTV);
        jobHistoryRecyclerView = findViewById(R.id.jobHistoryRecyclerView);
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