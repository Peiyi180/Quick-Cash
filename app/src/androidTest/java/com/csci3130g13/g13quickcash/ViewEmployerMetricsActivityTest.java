package com.csci3130g13.g13quickcash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
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

@RunWith(AndroidJUnit4.class)

public class ViewEmployerMetricsActivityTest {

    private static Intent startIntent = new Intent(ApplicationProvider.getApplicationContext(), ViewEmployerMetricsActivity.class);

    @Rule
    public ActivityScenarioRule<ViewEmployerMetricsActivity> activityRule = new ActivityScenarioRule<>(startIntent);

    @BeforeClass
    public static void setUp() {
        startIntent.putExtra("EmployerTag", new Employer("testEmployerID"));
    }

    @Before
    public void InitIntent() {
        Intents.init();
    }

    @After
    public void releaseIntent() {
        Intents.release();
    }

    @Test
    public void CheckEmployerPageVisibility(){
        onView(withId(R.id.headLabel2)).check(matches(isDisplayed()));
        onView(withId(R.id.jobHistoryLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.jobHistoryRecyclerView)).check(matches(isDisplayed()));
        onView(withId(R.id.expenditureLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.expenditureTV)).check(matches(isDisplayed()));
        onView(withId(R.id.reputationLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.ratingsTV)).check(matches(isDisplayed()));
    }
}
