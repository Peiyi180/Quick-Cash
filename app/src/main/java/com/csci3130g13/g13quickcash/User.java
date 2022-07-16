package com.csci3130g13.g13quickcash;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/** User
 *  Object representation of a User. Parent class for Employers and Employees.
 */

public abstract class User implements Serializable {

    protected String id = "";
    protected String name = "";
    protected String email = "";
    protected String city = "";
    protected String usertype = "";
    protected int reputation = 0;
    protected Map<String, String> jobs = new HashMap<>();

    public User(String id){
        this.id = id;
    }

    public User(){}

    public Map<String, String> getJobs() {
        return jobs;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public String getId() {
        return id;
    }

    public String getUsertype() {return usertype;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    /** @return whether User u equals object o.
     * @param o the Object being compared.
     */
    @Override
    public boolean equals(Object o){

        if( o == null ) { return false; }
        if(! (o instanceof User)){
            return false;
        }
        User other = (User) o;

        if (!this.getId().equals(other.getId()) ||
            !this.getName().equals(other.getName()) ||
            !this.getCity().equals(other.getCity()) ||
            !this.getEmail().equals(other.getEmail()) ||
            !this.getUsertype().equals(other.getUsertype()) ) {

            return false;

        }

        return true;
    }

}
