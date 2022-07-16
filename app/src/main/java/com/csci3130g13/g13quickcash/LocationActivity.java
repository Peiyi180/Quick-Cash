package com.csci3130g13.g13quickcash;
/**
 * Beside using the android google documents
 * To complete the dynamic location using GPS,
 * I referenced some of the logic code from the below Youtube video
 * https://www.youtube.com/watch?v=_xUcYfbtfsI
 * Also got help from TA: Julia Olmstead and Zolboo Erdenebaatar
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.csci3130g13.g13quickcash.utils.ManualLocationValidator;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity {

    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    private static final int PERMISSION_FINE_LOCATION = 5;

    private TextView tv_sensor, tv_updates, tv_address;
    private Switch switch_locationUpdates, switch_gps;

    private String country = "";
    private String city = "";
    private String address = "";
    private String inputCountry = "";
    private String inputCity = "";


    //location request is a config file for all settings related to fusedLocationProviderClient
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    //Goggle's API for location services
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //give each UI variable a value
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);
        tv_address = findViewById(R.id.tv_address);
        switch_gps = findViewById(R.id.sw_gps);
        switch_locationUpdates = findViewById(R.id.sw_locationsUpdates);

        Button btnShareLocation = findViewById(R.id.shareButton);
        Button skipButton = findViewById(R.id.skipButton);
        Button locationBtn = findViewById(R.id.locationSubmit);

        //set all properties of locationRequest
        locationRequest = new LocationRequest();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //how often does the default location check occur?
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        //how often does the location check occur when set to the most frequent update?
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //event that is triggered whenever the update interval is meet
        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                //save the location
                Location location = locationResult.getLastLocation();
                updateFieldsAndUI(location);
            }
        };

        switch_gps.setOnClickListener(view -> {
            if (switch_gps.isChecked()) {
                // most accurate -use gps
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                tv_sensor.setText("Using GPS sensors");
            } else {
                locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                tv_sensor.setText("Using Towers + WIFI");
            }
        });


        switch_locationUpdates.setOnClickListener(view -> {
            if (switch_locationUpdates.isChecked()) {
                //turn on location tracking
                startLocationUpdates();
            } else {
                //turn off tracking
                stopLocationUpdates();
            }
        });

        // share location button click event
        btnShareLocation.setOnClickListener(view -> {
            if(!city.isEmpty()) {
                setResult(1, new Intent(this, getCallingActivity().getClass()).putExtra("loc_city", city));
                finish();
            }
        });


        //manual location
        locationBtn.setOnClickListener(view -> {
            inputCountry = ((EditText) findViewById(R.id.countryField)).getText().toString();
            inputCity = ((EditText) findViewById(R.id.cityField)).getText().toString();

            if (validateInput(inputCountry, inputCity)) {
                setResult(1, new Intent(this, getCallingActivity().getClass()).putExtra("loc_city", inputCity));
                finish();
            }

        });

        //skip location update
        skipButton.setOnClickListener(view -> {
            // Redirect to user type choice activity
            setResult(Activity.RESULT_CANCELED);
            finish();

        });
    } //END onCreate method


    private void startLocationUpdates() {
        tv_updates.setText("Location is being tracked");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // here to request the missing permissions, and then overriding
            //permissions not granted yet
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_FINE_LOCATION);

        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

    }

    private void stopLocationUpdates() {
        tv_updates.setText("Location is NOT being tracked");
        tv_address.setText("Not tracking location");
        tv_sensor.setText("Not tracking location");
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "This app requires permission to be granted to access location", Toast.LENGTH_LONG).show();
        }

    }


    private void updateFieldsAndUI(Location location){
        //update all of the text view object of the new location

        Geocoder geocoder = new Geocoder(this);
        List<Address> addressLists = new ArrayList<>();

        try{
            addressLists = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        }
        catch(Exception e){
            tv_address.setText("Unavailable");
        }

        if (addressLists.size() > 0) {
            country = addressLists.get(0).getCountryName();

            city = addressLists.get(0).getLocality();
            if (city == null){
                city = addressLists.get(0).getAdminArea();
            }

            address = addressLists.get(0).getAddressLine(0);

            tv_address.setText("Country: " + country + "\nCity: " + city);
        }


        // \n is for new line
        /*Toast.makeText(getApplicationContext(), "Your Location is - \nCountry: "
                + country + "\nCity: " + city + "\nAddress: " + address, Toast.LENGTH_LONG).show();
         */

    }

    protected boolean validateInput(String country, String city){
        TextView statusBox = findViewById(R.id.statusText);
        if (!ManualLocationValidator.isLengthValid(country,city)) {
            statusBox.setText(R.string.lengthTooShortMessage);
            return false;
        }
        if (!ManualLocationValidator.isAlphanumeric(country, city)) {
            statusBox.setText(R.string.alphanumericInMeetMessage);
            return false;
        }
        if (!ManualLocationValidator.hasNoSpecialCharacter(country,city)) {
            statusBox.setText(R.string.specialCharacterInMeetMessage);
            return false;
        }
        return true;
    }



 }

