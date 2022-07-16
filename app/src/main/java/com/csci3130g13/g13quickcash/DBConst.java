package com.csci3130g13.g13quickcash;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/** Class for package-wide reference to repetitively used DB constants. */

public class DBConst {

    private DBConst(){}
    public static final String DB_URL = "https://g13-quick-cash-default-rtdb.firebaseio.com/";
    public static final DatabaseReference dbRef = FirebaseDatabase.getInstance(DB_URL).getReference();

    /**
     * Collections
     */
    public static final String CL_USERS = "users";
    public static final String CL_EMPLOYEE = "employee";
    public static final String CL_EMPLOYER = "employer";

}
