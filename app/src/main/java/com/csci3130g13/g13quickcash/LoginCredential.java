package com.csci3130g13.g13quickcash;

import java.io.Serializable;

/** Class for representing the password, id, and usertype of the user. */

public class LoginCredential implements Serializable {


    protected String id = "";
    protected String password = "";
    protected String usertype = "undef";

    public LoginCredential(String id, String password){
        this.id = id;
        this.password = password;
    }

    public LoginCredential(){}

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /** @param usertype User type (Employee/Employer) */
    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

}
