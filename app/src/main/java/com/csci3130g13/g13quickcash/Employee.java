package com.csci3130g13.g13quickcash;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *  Class for Object representation of various properties of an Employee.
 */

public class Employee extends User {

    protected String profession = "";
    protected String jobPreference1 = "";
    protected long totalIncome = 0;
    protected Map<String, String> alertSettings = new HashMap<>();
    protected String paypalClientID = "";


    public Employee(String id){

        super(id);
        this.usertype = "employee";

    }

    public Employee(){};


    public Map<String, String> getAlertSettings() {
        return alertSettings;
    }

    public void setAlertSettings(Map<String, String> alertSettings) {
        this.alertSettings = alertSettings;
    }

    public void addJob(String jobKey, String employerID){

        this.getJobs().put(jobKey, employerID);

    }

    public long getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(long totalIncome) {
        this.totalIncome = totalIncome;
    }

    /** @return The sphere of profession of the Employee. */

    public String getProfession() {
        return profession;
    }

    /** @param profession The sphere of profession of the Employee. */

    public void setProfession(String profession) {
        this.profession = profession;
    }

    /** @return Full-time or Part-time preference. */

    public String getJobPreference1() {
        return jobPreference1;
    }

    /** @param jobPreference1 Full-time or Part-time preference. */

    public void setJobPreference1(String jobPreference1) {
        this.jobPreference1 = jobPreference1;
    }

    /**
     * Get Paypal client ID
     *
     * @return paypalClientID
     */
    public String getPaypalClientID() {
        return paypalClientID;
    }

    /**
     * Get Paypal client ID
     *
     * @param paypalClientID - paypal client id
     */
    public void setPaypalClientID(String paypalClientID) {
        this.paypalClientID = paypalClientID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProfession(), getJobPreference1(), getTotalIncome(), getPaypalClientID());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Employee employee = (Employee) o;
        return getTotalIncome() == employee.getTotalIncome() && Objects.equals(getProfession(), employee.getProfession()) && Objects.equals(getJobPreference1(), employee.getJobPreference1()) && Objects.equals(getPaypalClientID(), employee.getPaypalClientID());
    }
}
