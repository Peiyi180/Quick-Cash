package com.csci3130g13.g13quickcash;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Job implements Serializable {

    protected String employerID = "";
    protected String businessType = "";
    protected String businessName = "";
    protected String city = "";
    protected String positionName = "";
    protected double salary = 0.0;
    protected String jobDesc = "";
    protected String prefEmployeeType = "";
    protected String prefSkill = "";
    protected String startDate = "";
    protected String endDate = "";
    protected double workHours = 0.0;
    protected long startDate_sec = 0;
    protected long endDate_sec = 0;
    protected boolean isHired = false;
    protected String employeeID = "N/A";
    protected String key = "";

    public Job (){}
    public Job (Employer emp){

            employerID = emp.getId();
            businessType = emp.getBusinessType();
            businessName = emp.getBusinessName();
            city = emp.getCity();

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public boolean isHired() {
        return isHired;
    }

    public void setHired(boolean hired) {
        isHired = hired;
    }

    public long getStartDate_sec() {
        return startDate_sec;
    }

    public void setStartDate_sec(long startDate_sec) {
        this.startDate_sec = startDate_sec;
    }

    public long getEndDate_sec() {
        return endDate_sec;
    }

    public void setEndDate_sec(long endDate_sec) {
        this.endDate_sec = endDate_sec;
    }

    public String getEmployerID() {
        return employerID;
    }

    public void setEmployerID(String employerID) {
        this.employerID = employerID;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getPrefEmployeeType() {
        return prefEmployeeType;
    }

    public void setPrefEmployeeType(String prefEmployeeType) {
        this.prefEmployeeType = prefEmployeeType;
    }

    public String getPrefSkill() {
        return prefSkill;
    }

    public void setPrefSkill(String prefSkill) {
        this.prefSkill = prefSkill;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getWorkHours() {
        return workHours;
    }

    public void setWorkHours(double workHours) {
        this.workHours = workHours;
    }

    public Date fetchFullStartDate(){

        return fetchFullDate(startDate);

    }

    public Date fetchFullEndDate(){

        return fetchFullDate(endDate);

    }

    private Date fetchFullDate(String date){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date fullDate = null;

        try{
            fullDate = dateFormat.parse(date);
        } catch (ParseException e){
            e.printStackTrace();
        }

        return fullDate;

    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return Double.compare(job.salary, salary) == 0 && Double.compare(job.workHours, workHours) == 0
                && Objects.equals(employerID, job.employerID) && Objects.equals(businessType, job.businessType)
                && Objects.equals(businessName, job.businessName) && Objects.equals(city, job.city)
                && Objects.equals(positionName, job.positionName) && Objects.equals(jobDesc, job.jobDesc)
                && Objects.equals(prefEmployeeType, job.prefEmployeeType) && Objects.equals(prefSkill, job.prefSkill)
                && Objects.equals(startDate, job.startDate) && Objects.equals(endDate, job.endDate);

    }

    @Override
    public int hashCode() {
        return Objects.hash(employerID, businessType, businessName, city, positionName, salary,
                jobDesc, prefEmployeeType, prefSkill, startDate, endDate, workHours);
    }

}
