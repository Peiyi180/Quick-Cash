package com.csci3130g13.g13quickcash;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class HireActivity extends AppCompatActivity {

    protected RecyclerView employeeList;
    protected Button backBtn;
    protected Button showRecBtn;
    protected Button clearFilterBtn;
    protected EmployeeAdapter employeeAdapter;
    protected final DatabaseReference jobRef = DBConst.dbRef.child("jobs");

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire);

        String jobToHireKey = getIntent().getStringExtra("JOB_TO_HIRE_KEY");
        Employer employer = (Employer) getIntent().getSerializableExtra("EMPLOYER_TO_HIRE");

        employeeList = findViewById(R.id.employeeList);
        backBtn = findViewById(R.id.backBtn_hire);
        showRecBtn = findViewById(R.id.showRecEmployeesBtn);
        clearFilterBtn = findViewById(R.id.clearFilterBtn);


        employeeList.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Employee>()
                .setQuery(DBConst.dbRef.child("users").child("employee"), Employee.class)
                .build();
        employeeAdapter = new EmployeeAdapter(options, this, employer, jobToHireKey);
        employeeList.setAdapter(employeeAdapter);

        backBtn.setOnClickListener(view -> {
            finish();
        });

        clearFilterBtn.setOnClickListener(view -> {

            employeeAdapter.setFilter(null);
            employeeAdapter.notifyDataSetChanged();

        });

        showRecBtn.setOnClickListener(view -> {

            // work for Pair 3
            // build appropriate filters for employeeAdapter.setFilter(filter)

//            Set<String> filter = new HashSet<>();



            DatabaseReference jobDBRef= jobRef.child(jobToHireKey);
            jobDBRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String reqSkill = dataSnapshot.child("prefSkill").getValue(String.class);
                    String hireType = dataSnapshot.child("prefEmployeeType").getValue(String.class);
                    String city = dataSnapshot.child("city").getValue(String.class);

                    HashMap<String, String> criteriaMap = new HashMap<>();
                    criteriaMap.put("reqSkill", reqSkill);
                    criteriaMap.put("hireType", hireType);
                    criteriaMap.put("city", city);

                    SearchHelper searchHelper = new SearchHelper (HireActivity.this, criteriaMap);

                    employeeAdapter.setFilter(searchHelper.buildEmployeeSearchFilter());
                    employeeAdapter.notifyDataSetChanged();

                    jobDBRef.removeEventListener(this);
                }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });



        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        employeeAdapter.startListening();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        employeeAdapter.stopListening();
    }



}