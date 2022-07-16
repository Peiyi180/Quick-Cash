package com.csci3130g13.g13quickcash;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Map;

public class EmployeeUnitTest {


    @Test
    public void checkMetrics_JobHistory() {

        Employee emp = new Employee("squidward");
        Map<String, String> squidJobs = emp.getJobs();
        squidJobs.put("-Mydkwiejfrksdf", "Mr. Krabs");
        assertEquals(emp.getJobs().get("-Mydkwiejfrksdf"),"Mr. Krabs");

    }

    @Test
    public void checkMetrics_reputation(){

        Employee emp = new Employee("squidward");
        emp.setReputation(55);
        assertEquals(55, emp.getReputation());

    }

    @Test
    public void checkMetrics_totalIncome(){

        Employee emp = new Employee("squidward");
        emp.setTotalIncome(100);
        assertEquals(100, emp.getTotalIncome());

    }

    @Test
    public void set_paypal_client_id_test() {
        Employee emp = new Employee("testUser");
        emp.setPaypalClientID("989898");
        assertEquals("989898", emp.getPaypalClientID());
    }
}
