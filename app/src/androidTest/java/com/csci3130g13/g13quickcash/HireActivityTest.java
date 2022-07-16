package com.csci3130g13.g13quickcash;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;

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

@RunWith(AndroidJUnit4.class)
public class HireActivityTest {

    private static Employer testEmployer = new Employer("testEmployerID");
    private static Intent startIntent = new Intent(ApplicationProvider.getApplicationContext(), HireActivity.class);
    public static final int STARTACTIVITY_TIMEOUT = 1000;
    private Activity currentActivity = null;

    @Rule
    public ActivityScenarioRule<HireActivity> activityRule = new ActivityScenarioRule<>(startIntent);

    @BeforeClass
    public static void setup() {

        startIntent.putExtra("EMPLOYER_TO_HIRE", testEmployer);
        startIntent.putExtra("JOB_TO_HIRE_KEY", "randomkey");
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
    public void checkTestSetupEmployer() {
        assertEquals("testEmployerID", testEmployer.getId());
    }

}
