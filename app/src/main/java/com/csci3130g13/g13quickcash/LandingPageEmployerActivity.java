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


/** Default landing activity for Employers. */

public class LandingPageEmployerActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_landing_page_employer);


        Employer employer = (Employer) getIntent().getSerializableExtra("EmployerTag");

        Button modifyPrefButton = (Button) findViewById(R.id.changePreferencesBtn);
        Button gotoSearchBtn = findViewById(R.id.searchBtn);
        Button clearSearchBtn = findViewById(R.id.clearSearchBtn);
        Button postNewJobBtn = (Button) findViewById(R.id.jobPostButton);
        Button viewMetricsBtn = (Button) findViewById(R.id.viewMetricsBtn);
        Button employerPayBtn = (Button) findViewById(R.id.employerPayBtn);

        recyclerView = findViewById(R.id.jobsRecyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobAdapter = new JobAdapter(buildOptions(), this, employer);
        recyclerView.setAdapter(jobAdapter);


        Spinner sortCategorySpinner = findViewById(R.id.sortCategorySpinner);
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
            Intent modifyPrefIntent = new Intent(LandingPageEmployerActivity.this, SetEmployerPreferencesActivity.class);
            modifyPrefIntent.putExtra("oldEmployerTag", employer);
            startActivity(modifyPrefIntent);
        });


        postNewJobBtn.setOnClickListener(view -> {

                Intent postNewJobIntent = new Intent(LandingPageEmployerActivity.this, ComposeNewJobActivity.class);
                postNewJobIntent.putExtra("EmployerTag", employer);
                startActivity(postNewJobIntent);

        });

        employerPayBtn.setOnClickListener(view -> {
            redirectToPaySalaryPage(employer);
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

//            setContentView(R.layout.activity_view_metrics_employer);
            Intent viewMetricsIntent = new Intent(LandingPageEmployerActivity.this, ViewEmployerMetricsActivity.class);
            viewMetricsIntent.putExtra("EmployerTag", employer);
            startActivity(viewMetricsIntent);

        });

    }


    /**
     * Employer Pay button event - Redirect to pay salary page
     * @param employer - Employer Object
     */
    private void redirectToPaySalaryPage(Employer employer) {
        Intent paySalaryIntent = new Intent(this, PaySalaryActivity.class);
        paySalaryIntent.putExtra("EmployerTag", employer);

        startActivity(paySalaryIntent);
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