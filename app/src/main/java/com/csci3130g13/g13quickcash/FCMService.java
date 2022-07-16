package com.csci3130g13.g13quickcash;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.csci3130g13.g13quickcash.utils.Session;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/** FCM Message Processing Service
 *
 *  Displays a notification in the system tray when it receives an FCM message.
 *
 *  References CSCI3130 Tutorial: Push Notifications by Dhrumil Shah (Tutorial TA).
 *  https://dal.brightspace.com/d2l/le/content/201532/viewContent/2968827/View (Date Accessed: April 1st, 2022)
 */

public class FCMService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        RemoteMessage.Notification receivedMsg = remoteMessage.getNotification();

        if(receivedMsg == null){
            Log.d("job_notif_err", "Unknown Message Format");
            return;
        }

        Log.d("job_notif_received", receivedMsg.getBody());

        // initialize NotificationManagerCompat

        NotificationManagerCompat trayNotificationManager = NotificationManagerCompat.from(this);

        // Add Notification Channel if android version is higher than Android O

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            trayNotificationManager.createNotificationChannel(new NotificationChannel("QUICKCASH", "QuickCash New Job Alert",
                    NotificationManager.IMPORTANCE_HIGH));
        }

        // Build Intent for redirection on click

        User loggedInUser = Session.getInstance().getUser();
        Intent redirectIntent;

        if(loggedInUser != null && loggedInUser instanceof Employee){

            // if app is already running and user is signed as employee, launch LandingPageEmployee on clicking the receivedMsg.
            // Otherwise, open the main login activity on click.

            redirectIntent = new Intent (this, LandingPageEmployeeActivity.class);
            redirectIntent.putExtra("EmployeeTag", (Employee) Session.getInstance().getUser());

        } else {
            redirectIntent= new Intent (this, MainActivity.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 200, redirectIntent, 0 );

        // Build Notification

        Notification newNotification =
                 new NotificationCompat.Builder(this, "QUICKCASH")
                         .setSmallIcon(android.R.drawable.sym_def_app_icon)
                         .setContentTitle(receivedMsg.getTitle())
                         .setContentText(receivedMsg.getBody())
                         .setContentIntent(pendingIntent)
                .build();

        //display Notification

        trayNotificationManager.notify(receivedMsg.hashCode(), newNotification);

    }

}