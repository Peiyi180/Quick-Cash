package com.csci3130g13.g13quickcash.utils;

import com.csci3130g13.g13quickcash.Employee;
import com.csci3130g13.g13quickcash.Employer;
import com.csci3130g13.g13quickcash.User;

/** Session class for inter-activity persistence of User model classes
 *
 *  References CSCI3130 Tutorial: Individual Chat Feature by Dhrumil Shah (Tutorial TA).
 *  https://dal.brightspace.com/d2l/le/content/201532/viewContent/2968817/View (Date Accessed: April 1st, 2022)
 */

public class Session {

    private static SessionInstance sessionInstance = null;

    private Session(){}

    public static SessionInstance getInstance(){

        if(sessionInstance == null){
            sessionInstance = new SessionInstance();
        }
        return sessionInstance;

    }


    public static class SessionInstance{

        private User user;

        private SessionInstance(){}

        public void setUser(User user){
            this.user = user;
        }

        public <T extends User> T getUser(){

            return (T) user;

        }

    }

}
