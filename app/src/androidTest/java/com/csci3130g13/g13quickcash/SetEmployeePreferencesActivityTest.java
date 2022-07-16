package com.csci3130g13.g13quickcash;

import androidx.annotation.NonNull;
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

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


/**
 *  Tests for the SetEmployeePreferencesActivity
 *  Author: Jae Hyuk Lee
 *
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)


public class SetEmployeePreferencesActivityTest {

    private static Employee testEmployee = new Employee("testEmployeeID");
    private Employee storedEmployee = null;

    private static Intent startIntent = new Intent(ApplicationProvider.getApplicationContext(), SetEmployeePreferencesActivity.class);

    @Rule

    public ActivityScenarioRule<SetEmployeePreferencesActivity> activityRule = new ActivityScenarioRule<>(startIntent);


    @BeforeClass
    public static void setup() {
        testEmployee.setCity("bla");
        startIntent.putExtra("oldEmployeeTag",  testEmployee);
    }
    @Before
    public void getEmployeeReference(){
        Intents.init();
        activityRule.getScenario().onActivity(activity ->{
            testEmployee = activity.getEmployee();
        });
    }
    @After
    public void tearDown() {
        Intents.release();
    }


    @Test
    public void checkTestSetupEmployee() {
        assertEquals("testEmployeeID", testEmployee.getId());
    }

    @Test
    public void testSetPreferencesEmployee(){

        // mocking the setting of user preferences
        onView(withId(R.id.nameField)).perform(typeText("krabs"));
        onView(withId(R.id.emailField)).perform(typeText("mrkrabs@bikini.net"));
        onView(withId(R.id.paypalClientIdET)).perform(typeText("Ax1Ue1E76FI1tbOyGFoFD2EprdNScxcAciP68VAn7UAIE_9127Y-eunWFxvJhU4fgSFI7wVZ-WECyQH"));

        // mock clicking the save button behavior
        closeSoftKeyboard();
        onView(withId(R.id.saveButton)).perform(click());


        // verify that the preferences were saved in the DB.

        // fetch the stored data as an Employee object

        DatabaseReference userRef = DBConst.dbRef.child("users").child("employee").child(testEmployee.getId());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storedEmployee = snapshot.getValue(Employee.class);

                // run eqaulity check.
                assertEquals(testEmployee, storedEmployee);
                userRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}



