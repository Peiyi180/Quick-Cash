package com.csci3130g13.g13quickcash;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *  Class for Object representation of various properties of an Employer.
 */

public class Employer extends User {


    protected String businessType = "";
    protected String businessName = "";
    protected String hirePreference1 = "";
    protected String creditCardNumber = "";
    protected long totalExpenditure = 0;


    public Employer(String id){

        super(id);
        this.usertype = "employer";

    }

    public Employer(){}

    public void addJob(String jobKey, String employeeID){

        this.getJobs().put(jobKey, employeeID);

    }

    public long getTotalExpenditure() {
        return totalExpenditure;
    }

    public void setTotalExpenditure(long totalExpenditure) {
        this.totalExpenditure = totalExpenditure;
    }

    public String getCreditCardNumber() { return creditCardNumber;}

    public void setCreditCardNumber(String card) { this.creditCardNumber = card;}

    /** @return Full-time or Part-time preference. */

    public String getHirePreference1() {
        return hirePreference1;
    }

    /** @param hirePreference1 Full-time or Part-time preference. */

    public void setHirePreference1(String hirePreference1) {
        this.hirePreference1 = hirePreference1;
    }

    /** @return The sphere of business of the Employer. */

    public String getBusinessType() {
        return businessType;
    }

    /** @param businessType The sphere of business of the Employer.*/

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }


    /** @return whether Employer e equals object o.
     * @param o the Object being compared.
     */


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Employer employer = (Employer) o;
        return Objects.equals(businessType, employer.businessType) && Objects.equals(businessName, employer.businessName) && Objects.equals(hirePreference1, employer.hirePreference1) && Objects.equals(creditCardNumber, employer.creditCardNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(businessType, businessName, hirePreference1, creditCardNumber);
    }


}
