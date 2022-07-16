package com.csci3130g13.g13quickcash;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class LocationActivityTest {

    private final static String testCountry = "Canada";
    private final static String testCity = "Halifax";

    private static Intent startIntent =
            new Intent(ApplicationProvider.getApplicationContext(), LocationActivity.class);

    @Rule
    public ActivityScenarioRule<LocationActivity> activityRule =
            new ActivityScenarioRule<>(startIntent);


    @Before
    public void InitIntent() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void InputLengthNotValid() {
        String invalidCountry = "Ufa";
        String invalidCity = "On";
        onView(withId(R.id.countryField)).perform(typeText(invalidCountry));
        closeSoftKeyboard();
        onView(withId(R.id.cityField)).perform(typeText(invalidCity));
        closeSoftKeyboard();
        onView(withId(R.id.locationSubmit)).perform(click());
        onView(withId(R.id.statusText)).check(matches(withText("country or city too short")));
    }

    @Test
    public void InputLengthValid() throws InterruptedException {
        onView(withId(R.id.countryField)).perform(typeText(testCountry));
        closeSoftKeyboard();
        onView(withId(R.id.cityField)).perform(typeText(testCity));
        closeSoftKeyboard();
        onView(withId(R.id.locationSubmit)).perform(click());
        Thread.sleep(1000);
        assertEquals(1, activityRule.getScenario().getResult().getResultCode());
        //Intents.intended(hasComponent(LocationActivity.class.getName()));

    }

    @Test
    public void InputAlphanumericNotValid() {
        String invalidCountry = "Canada9";
        String invalidCity = "Halifax7";
        onView(withId(R.id.countryField)).perform(typeText(invalidCountry));
        closeSoftKeyboard();
        onView(withId(R.id.cityField)).perform(typeText(invalidCity));
        closeSoftKeyboard();
        onView(withId(R.id.locationSubmit)).perform(click());
        onView(withId(R.id.statusText)).check(matches(withText("country or city does not need to be Alphanumeric")));
    }

    @Test
    public void InputNoAlphanumericValid() throws InterruptedException {
        onView(withId(R.id.countryField)).perform(typeText(testCountry));
        closeSoftKeyboard();
        onView(withId(R.id.cityField)).perform(typeText(testCity));
        closeSoftKeyboard();
        onView(withId(R.id.locationSubmit)).perform(click());
        Thread.sleep(1000);
        assertEquals(1, activityRule.getScenario().getResult().getResultCode());
    }

    @Test
    public void InputSpecialCharacterNotValid() {
        String invalidCountry = "Canada*";
        String invalidCity = "Halifax&";
        onView(withId(R.id.countryField)).perform(typeText(invalidCountry));
        closeSoftKeyboard();
        onView(withId(R.id.cityField)).perform(typeText(invalidCity));
        closeSoftKeyboard();
        onView(withId(R.id.locationSubmit)).perform(click());
        onView(withId(R.id.statusText)).check(matches(withText("country or city should not contain a special character")));
    }

    @Test
    public void InputNoSpecialCharacterValid() throws InterruptedException {
        onView(withId(R.id.countryField)).perform(typeText(testCountry));
        closeSoftKeyboard();
        onView(withId(R.id.cityField)).perform(typeText(testCity));
        closeSoftKeyboard();
        onView(withId(R.id.locationSubmit)).perform(click());
        Thread.sleep(1000);
        assertEquals(1, activityRule.getScenario().getResult().getResultCode());
    }

}
