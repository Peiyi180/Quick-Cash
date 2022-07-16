package com.csci3130g13.g13quickcash;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.paypal.android.sdk.payments.PaymentActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class PaySalaryActivityTest {
    private static Intent startIntent = new Intent(ApplicationProvider.getApplicationContext(), PaySalaryActivity.class);
    public static final int STARTACTIVITY_TIMEOUT = 1000;

    @Rule
    public ActivityScenarioRule<LandingPageEmployerActivity> activityRule = new ActivityScenarioRule<>(startIntent);

    @BeforeClass
    public static void setup() {
        Employer testEmployer = new Employer("testPaypalEmployer");
        startIntent.putExtra("EmployerTag",  testEmployer);
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
    public void redirectToLandingPageEmployerPage() throws InterruptedException {
        onView(withId(R.id.backToEmployerBtn)).perform(click());
        Thread.sleep(STARTACTIVITY_TIMEOUT);
        Intents.intended(hasComponent(LandingPageEmployerActivity.class.getName()));
    }

    @Test
    public void redirectToPaypalPaymentPage() throws InterruptedException {
        onView(withId(R.id.amountET)).perform(typeText(String.valueOf("200")));
        onView(withId(R.id.employeeIdET)).perform(typeText("testPaypalEmployee"));
        closeSoftKeyboard();
        onView(withId(R.id.payButton)).perform(click());
        Thread.sleep(STARTACTIVITY_TIMEOUT);
        Intents.intended(hasComponent(PaymentActivity.class.getName()));
    }
}
