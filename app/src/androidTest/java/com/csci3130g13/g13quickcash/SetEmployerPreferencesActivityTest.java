package com.csci3130g13.g13quickcash;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *  Tests for the SetEmployerPreferencesActivity
 *  Author: Jae Hyuk Lee
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)


public class SetEmployerPreferencesActivityTest {

    private static Employer testEmployer = new Employer("testEmployerID");
    private Employer storedEmployer = null;

    private static Intent startIntent = new Intent(ApplicationProvider.getApplicationContext(), SetEmployerPreferencesActivity.class);

    @Rule

    public ActivityScenarioRule<SetEmployerPreferencesActivity> activityRule = new ActivityScenarioRule<>(startIntent);


    @BeforeClass
    public static void setup() {
        testEmployer.setCity("bla");
        testEmployer.setBusinessName("Krusty Krab");
        startIntent.putExtra("oldEmployerTag",  testEmployer);
    }
    @Before
    public void getEmployerReference(){
        Intents.init();
        activityRule.getScenario().onActivity(activity ->{
            testEmployer = activity.getEmployer();
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

    @Test
    public void testSetPreferencesEmployer(){

        // mocking the setting of user preferences
        onView(withId(R.id.nameField)).perform(typeText("krabs"));
        onView(withId(R.id.emailField)).perform(typeText("mrkrabs@bikini.net"));
        onView(withId(R.id.businessNameField)).perform(typeText("Krusty Krab"));
        closeSoftKeyboard();

        onView(withId(R.id.saveButton)).perform(ViewActions.scrollTo());
        // mock clicking the save button behavior
        closeSoftKeyboard();
        onView(withId(R.id.saveButton)).perform(click());

        // verify that the preferences were saved in the DB.

        // fetch the stored data as an Employer object

        DatabaseReference userRef = DBConst.dbRef.child("users").child("employer").child(testEmployer.getId());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storedEmployer = snapshot.getValue(Employer.class);

                // run eqaulity check.
                assertEquals(testEmployer, storedEmployer);
                userRef.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}



