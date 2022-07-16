package com.csci3130g13.g13quickcash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.app.Activity;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 *  Tests for the Employee Landing Page
 *
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)

public class LandingPageEmployeeActivityTest {

    private static Intent startIntent = new Intent(ApplicationProvider.getApplicationContext(), LandingPageEmployeeActivity.class);
    public static final int STARTACTIVITY_TIMEOUT = 1000;
    private Activity currentActivity = null;

    @Rule
    public ActivityScenarioRule<LandingPageEmployeeActivity> activityRule = new ActivityScenarioRule<>(startIntent);

    @BeforeClass
    public static void setup() {
        Employee testEmployee = new Employee("testEmployeeID");
        testEmployee.setJobPreference1("Full-Time");
        testEmployee.setCity("Boston");
        testEmployee.setProfession("Construction");
        testEmployee.setEmail("asdoif@asoidjf.net");
        testEmployee.setName("test employee");
        startIntent.putExtra("EmployeeTag",  testEmployee);

        /*
        Job job = new Job();
        job.setName("Math Tutor");
        job.setCategory("Education");
        job.setCity("Toronto");
        job.setType("Part-Time");
        job.setContactEmail("mary@gmail.com");
        Job[] jobs = {job};
        startIntent.putExtra("Jobs", jobs); */
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
        onView(withId(R.id.changePreferencesBtn2)).perform(click());
        Thread.sleep(STARTACTIVITY_TIMEOUT);
        Intents.intended(hasComponent(SetEmployeePreferencesActivity.class.getName()));
    }
    /*
    @Test
    public void postedJobIsListed() {
        RecyclerView recyclerView = currentActivity.findViewById(R.id.jobsRecyclerView);

        int jobNumber = Objects.requireNonNull(recyclerView.getAdapter(), "Adapter is Null").getItemCount();
        Assert.assertEquals(1, jobNumber);
    }*/

    @Test
    public void viewMetrics() throws InterruptedException {
        onView(withId(R.id.viewMetricsBtn2)).perform(click());
        Thread.sleep(STARTACTIVITY_TIMEOUT);
        Intents.intended(hasComponent(ViewEmployeeMetricsActivity.class.getName()));
    }



}
