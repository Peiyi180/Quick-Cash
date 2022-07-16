package com.csci3130g13.g13quickcash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.content.Intent;
import android.widget.DatePicker;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

/**
 *  Tests for ComposeNewJobActivity
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)


public class ComposeNewJobActivityTest {

    private static Employer testEmployer = new Employer("testEmployerID");


    private static Intent startIntent = new Intent(ApplicationProvider.getApplicationContext(), ComposeNewJobActivity.class);

    @Rule

    public ActivityScenarioRule<ComposeNewJobActivity> activityRule = new ActivityScenarioRule<>(startIntent);


    @BeforeClass
    public static void setup() {

        testEmployer.setHirePreference1("Full-Time");
        testEmployer.setCity("Boston");
        testEmployer.setBusinessType("Construction");
        testEmployer.setBusinessName("Boston cements");
        testEmployer.setEmail("asdoif@asoidjf.net");
        testEmployer.setName("test Employer");
        startIntent.putExtra("EmployerTag",  testEmployer);

    }
    @Before
    public void IntentInit(){
        Intents.init();
    }

    @After
    public void releaseIntent() {
        Intents.release();

    }

    @Test
    public void testPostNewJob(){

        onView(withId(R.id.positionName)).perform(typeText("testPosition"));
        onView(withId(R.id.salaryField)).perform(typeText("35"));
        onView(withId(R.id.jobDescField)).perform(typeText("test description"));

        closeSoftKeyboard();

        onView(withId(R.id.jobStartDate)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(2022, 1, 5) );
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.jobEndDate)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(2022, 1, 6) );
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.jobHoursPerDay)).perform(typeText("5"));

        closeSoftKeyboard();

        onView(withId(R.id.postButton)).perform(click());

        // verify that the job was saved in the DB.
        // fetch the stored data as a Job object

        Job expectedJob = new Job(testEmployer);
        expectedJob.setPositionName("testPosition");
        expectedJob.setSalary(35);
        expectedJob.setJobDesc("test description");
        expectedJob.setWorkHours(5);
        expectedJob.setStartDate("5/1/2022");
        expectedJob.setEndDate("6/1/2022");
        expectedJob.setPrefEmployeeType("Full-Time");
        expectedJob.setPrefSkill("Construction");


        Query jobRef = DBConst.dbRef.child("jobs").orderByChild("employerID").equalTo(testEmployer.getId());



        jobRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Job storedJob = snapshot.getValue(Job.class);

                //verify if the stored job is equal to what we would expect.
                assertEquals(storedJob, expectedJob);

                //remove the Job to clean up DB after test
                snapshot.getRef().removeValue();
                jobRef.removeEventListener(this);
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        try{
            Thread.sleep(2000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }


}



