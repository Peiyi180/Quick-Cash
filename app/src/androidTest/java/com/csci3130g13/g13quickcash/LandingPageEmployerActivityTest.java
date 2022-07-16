package com.csci3130g13.g13quickcash;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Intent;

import com.firebase.ui.database.FirebaseRecyclerOptions;


/**
 *  Tests for the Employer Landing Page
 *
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)

public class LandingPageEmployerActivityTest {

    private static Intent startIntent = new Intent(ApplicationProvider.getApplicationContext(), LandingPageEmployerActivity.class);
    public static final int STARTACTIVITY_TIMEOUT = 1000;
    private Activity currentActivity = null;

    @Rule
    public ActivityScenarioRule<LandingPageEmployerActivity> activityRule = new ActivityScenarioRule<>(startIntent);

    @BeforeClass
    public static void setup() {
        Employer testEmployer = new Employer("testEmployerID");
        testEmployer.setHirePreference1("Full-Time");
        testEmployer.setCity("Boston");
        testEmployer.setBusinessType("Construction");
        testEmployer.setBusinessName("Boston Cement");
        testEmployer.setEmail("asdoif@asoidjf.net");
        testEmployer.setName("test Employer");
        startIntent.putExtra("EmployerTag",  testEmployer);
    }

    @Before
    public void InitIntent() {
        Intents.init();
        activityRule.getScenario().onActivity(activity ->{
            currentActivity = activity;
        });
    }

    @After
    public void releaseIntent() {
        Intents.release();
    }

    @Test
    public void modifyPreferences() throws InterruptedException{
        onView(withId(R.id.changePreferencesBtn)).perform(click());
        Thread.sleep(STARTACTIVITY_TIMEOUT);
        Intents.intended(hasComponent(SetEmployerPreferencesActivity.class.getName()));
    }

    @Test
    public void postNewJob() throws InterruptedException{
        onView(withId(R.id.jobPostButton)).perform(click());
        Thread.sleep(STARTACTIVITY_TIMEOUT);
        Intents.intended(hasComponent(ComposeNewJobActivity.class.getName()));
    }

    @Test
    public void sortByBusinessName(){
        LandingPageEmployerActivity activity = (LandingPageEmployerActivity) currentActivity;
        activity.sortCategory = "businessName";
        FirebaseRecyclerOptions<Job> producedOptions = activity.buildOptions();
        FirebaseRecyclerOptions<Job> expectedOptions =  new FirebaseRecyclerOptions.Builder<Job>()
                .setQuery(DBConst.dbRef.child("jobs").orderByChild("businessName"), Job.class)
                .build();

        assertTrue(producedOptions instanceof FirebaseRecyclerOptions );
    }

    @Test
    public void sortBySalary(){
        LandingPageEmployerActivity activity = (LandingPageEmployerActivity) currentActivity;
        activity.sortCategory = "salary";
        FirebaseRecyclerOptions<Job> producedOptions = activity.buildOptions();
        FirebaseRecyclerOptions<Job> expectedOptions =  new FirebaseRecyclerOptions.Builder<Job>()
                .setQuery(DBConst.dbRef.child("jobs").orderByChild("salary"), Job.class)
                .build();

        assertTrue(producedOptions instanceof FirebaseRecyclerOptions);
    }

    @Test
    public void redirectToPaySalaryPage() throws InterruptedException {
        onView(withId(R.id.employerPayBtn)).perform(click());
        Thread.sleep(STARTACTIVITY_TIMEOUT);
        Intents.intended(hasComponent(PaySalaryActivity.class.getName()));
    }

}
