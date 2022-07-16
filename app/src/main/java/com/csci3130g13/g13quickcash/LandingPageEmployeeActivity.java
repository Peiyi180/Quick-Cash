package com.csci3130g13.g13quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

/** Default landing activity for Employees. */

public class LandingPageEmployeeActivity extends AppCompatActivity {
    protected JobAdapter jobAdapter;
    protected RecyclerView recyclerView;
    protected String sortCategory = "default";
    protected Bundle searchCriteriaData = null;

    // Launcher for launching SearchJobActivity for result.
    protected ActivityResultLauncher<Intent> searchLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                if(result.getResultCode() == 1){

                    searchCriteriaData = result.getData().getExtras();
                    SearchHelper searchHelper = new SearchHelper (this, searchCriteriaData);
                    jobAdapter.setFilter(searchHelper.buildSearchFilter());
                    refreshRecyclerView();
                }


            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page_employee);


        Employee employee = (Employee) getIntent().getSerializableExtra("EmployeeTag");

        Button modifyPrefButton = (Button) findViewById(R.id.changePreferencesBtn2);
        Button gotoSearchBtn = findViewById(R.id.searchBtn2);
        Button clearSearchBtn = findViewById(R.id.clearBtn3);
        Button viewMetricsBtn = (Button) findViewById(R.id.viewMetricsBtn2);
        Button setAlertSettingsBtn = findViewById(R.id.setAlertSettingsBtn);


        recyclerView = findViewById(R.id.jobsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobAdapter = new JobAdapter(buildOptions(), this, employee);
        recyclerView.setAdapter(jobAdapter);

        Spinner sortCategorySpinner = findViewById(R.id.sortCategorySpinner2);
        ArrayAdapter<CharSequence> sortCategoryAdapter =
                new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.job_sort_criteria));
        sortCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortCategorySpinner.setAdapter(sortCategoryAdapter);

        sortCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                sortCategory = SearchHelper.parseSortCategory(pos);
                refreshRecyclerView();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        modifyPrefButton.setOnClickListener(view -> {
            Intent modifyPrefIntent = new Intent(LandingPageEmployeeActivity.this, SetEmployeePreferencesActivity.class);
            modifyPrefIntent.putExtra("oldEmployeeTag", employee);
            startActivity(modifyPrefIntent);
        });

        gotoSearchBtn.setOnClickListener(view -> {

            Intent gotoSearch = new Intent(this, SearchJobActivity.class);
            searchLauncher.launch(gotoSearch);

        });

        clearSearchBtn.setOnClickListener(view -> {

            searchCriteriaData = null;
            jobAdapter.clearFilter();
            refreshRecyclerView();

        });

        viewMetricsBtn.setOnClickListener(view -> {

            Intent viewMetricsIntent = new Intent(LandingPageEmployeeActivity.this, ViewEmployeeMetricsActivity.class);
            viewMetricsIntent.putExtra("EmployeeTag", employee);
            startActivity(viewMetricsIntent);

        });

        setAlertSettingsBtn.setOnClickListener(view -> {

            startActivity(new Intent(this, AlertSettingsActivity.class).putExtra("employee", employee));

        });

    }

    /** @return FirebaseUI Recycler Options to construct or modify JobAdapter  */

    protected FirebaseRecyclerOptions<Job> buildOptions(){

        // Should return appropriate query according to search criteria, ultimately ordered by the sort category.

        Query jobRef = DBConst.dbRef.child("jobs");

        if( !sortCategory.equals("default") ){
            jobRef = jobRef.orderByChild(sortCategory);
        }

        return new FirebaseRecyclerOptions.Builder<Job>()
                .setQuery(jobRef, Job.class)
                .build();

    }

    /** refresh the recyclerView when a new category is set. */
    protected void refreshRecyclerView(){

        jobAdapter.updateOptions(buildOptions());
        jobAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        super.onStart();
        jobAdapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        jobAdapter.stopListening();
    }

}
