package com.csci3130g13.g13quickcash;

import android.content.Context;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Tests for the login activity(MainActivity)
 * Authors: Jae Hyuk Lee, Peiyi Jiang
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


@RunWith(AndroidJUnit4.class)


public class MainActivityTest {

    private final static String testID = "testID";
    private final static String testPW = "testPW123!";
    public static final int STARTACTIVITY_TIMEOUT = 1000;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    // Remove the test ID from DB before each test if it exists.

    @Before
    public void clearDBInitIntent() {
        DBConst.dbRef.child("credentials").child(testID).removeValue();
        Intents.init();
    }

    @After
    public void releaseIntent() {
        Intents.release();
    }

    @AfterClass
    public static void cleanUp() {
        DBConst.dbRef.child("credentials").child(testID).removeValue();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.csci3130g13.g13quickcash", appContext.getPackageName());
    }

    // IDDoesNotExist:
    // Test case for when the ID does not exist, check whether app redirects to registration page.

    @Test
    public void IDDoesNotExist() throws InterruptedException {
        onView(withId(R.id.idField)).perform(typeText(testID));
        onView(withId(R.id.passwordField)).perform(typeText(testPW));
        closeSoftKeyboard();
        onView(withId(R.id.loginButton)).perform(click());
        Thread.sleep(STARTACTIVITY_TIMEOUT);
        Intents.intended(hasComponent(ChooseUserTypeActivity.class.getName()));
    }

    // IDExistsWrongPW:
    // If ID exists in the DB, but the user has not entered the correct password, the user is not redirected.

    @Test
    public void IDExistsWrongPW() {
        onView(withId(R.id.idField)).perform(typeText("mrkrabs"));
        onView(withId(R.id.passwordField)).perform(typeText("wrongPW"));
        closeSoftKeyboard();
        onView(withId(R.id.loginButton)).perform(click());
        Intents.times(0);
    }


    /*

    // IDExistsCorrectPW:
    // If ID exists in the DB, and the user has entered the correct password, the user is redirected to the landing page. This is not implemented yet.

    @Test
    public void IDExistsCorrectPW(){
        onView(withId(R.id.idField)).perform(typeText("mrkrabs"));
        onView(withId(R.id.passwordField)).perform(typeText("badPassword"));
        closeSoftKeyboard();
        onView(withId(R.id.loginButton)).perform(click());
        Intents.intended(hasComponent("EmployerLandingActivity"));
    }
    */

    @Test
    public void passwordLengthNotValid() {
        String invalidPassword = "12345?a";
        onView(withId(R.id.idField)).perform(typeText(testID));
        onView(withId(R.id.passwordField)).perform(typeText(invalidPassword));
        closeSoftKeyboard();
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.statusText)).check(matches(withText("Password too short")));
    }

    @Test
    public void passwordLengthValid() throws InterruptedException{
        String validPassword = "12345678?a";
        onView(withId(R.id.idField)).perform(typeText(testID));
        onView(withId(R.id.passwordField)).perform(typeText(validPassword));
        closeSoftKeyboard();
        onView(withId(R.id.loginButton)).perform(click());
        Thread.sleep(STARTACTIVITY_TIMEOUT);
        Intents.intended(hasComponent(ChooseUserTypeActivity.class.getName()));
    }

    @Test
    public void passwordAlphanumericNotValid() {
        String invalidPassword = "12345678?";
        onView(withId(R.id.idField)).perform(typeText(testID));
        onView(withId(R.id.passwordField)).perform(typeText(invalidPassword));
        closeSoftKeyboard();
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.statusText)).check(matches(withText("Password need to be Alphanumeric")));
    }

    @Test
    public void passwordAlphanumericValid() throws InterruptedException {
        String validPassword = "12345678a?";
        onView(withId(R.id.idField)).perform(typeText(testID));
        onView(withId(R.id.passwordField)).perform(typeText(validPassword));
        closeSoftKeyboard();
        onView(withId(R.id.loginButton)).perform(click());
        Thread.sleep(STARTACTIVITY_TIMEOUT);
        Intents.intended(hasComponent(ChooseUserTypeActivity.class.getName()));
    }

    @Test
    public void passwordSpecialCharacterNotValid() {
        String invalidPassword = "12345678a";
        onView(withId(R.id.idField)).perform(typeText(testID));
        onView(withId(R.id.passwordField)).perform(typeText(invalidPassword));
        closeSoftKeyboard();
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.statusText)).check(matches(withText("Password should contain a special character")));
    }

    @Test
    public void passwordSpecialCharacterValid() throws InterruptedException {
        String validPassword = "12345678a?";
        onView(withId(R.id.idField)).perform(typeText(testID));
        onView(withId(R.id.passwordField)).perform(typeText(validPassword));
        closeSoftKeyboard();
        onView(withId(R.id.loginButton)).perform(click());
        Thread.sleep(STARTACTIVITY_TIMEOUT);
        Intents.intended(hasComponent(ChooseUserTypeActivity.class.getName()));
    }
}
