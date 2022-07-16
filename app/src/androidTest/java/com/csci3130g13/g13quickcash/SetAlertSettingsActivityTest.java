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
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Map;

public class SetAlertSettingsActivityTest {

    private static Intent startIntent = new Intent(ApplicationProvider.getApplicationContext(), AlertSettingsActivity.class);
    private static final int STARTACTIVITY_TIMEOUT = 1000;
    private static Employee testEmployee = new Employee("alertTester");
    private Map<String, Object> alertMap;

    @Rule

    public ActivityScenarioRule<AlertSettingsActivity> activityRule = new ActivityScenarioRule<>(startIntent);


    @BeforeClass
    public static void setup() {
        startIntent.putExtra("employee", testEmployee );
    }
    @Before
    public void getEmployeeReference(){
        Intents.init();
    }
    @After
    public void tearDown() {
        Intents.release();

    }


    @Test
    public void checkTestSetupEmployee() {
        assertEquals("alertTester", testEmployee.getId());
    }

    @Test
    public void testSetAlertSettings() throws InterruptedException{

        // set alert parameters
        onView(withId(R.id.alertSwitch)).perform(click());
        onView(withId(R.id.cityET_alert)).perform(typeText("Halifax"));
        closeSoftKeyboard();

        onView(withId(R.id.saveBtn_alert)).perform(click());

        Thread.sleep(STARTACTIVITY_TIMEOUT);
        // verify that the alert settings were saved in the DB.

        DatabaseReference testRef = DBConst.dbRef.child("users").child("employee").child(testEmployee.getId());
        DatabaseReference alertRef = testRef.child("alertSettings");

        alertRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                alertMap = (Map<String, Object>) snapshot.getValue();

                // run eqaulity check.
                assertEquals("Halifax", alertMap.get("city"));
                alertRef.removeEventListener(this);
                alertRef.removeValue();

                FirebaseMessaging.getInstance().unsubscribeFromTopic("Halifax");
                FirebaseMessaging.getInstance().unsubscribeFromTopic("anyhiretype");
                FirebaseMessaging.getInstance().unsubscribeFromTopic("anyreqskill");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
