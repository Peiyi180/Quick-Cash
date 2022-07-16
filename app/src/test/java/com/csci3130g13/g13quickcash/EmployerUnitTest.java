package com.csci3130g13.g13quickcash;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Map;

public class EmployerUnitTest {


    @Test
    public void checkMetrics_JobHistory() {

        Employer emp = new Employer("Mr. Krabs");
        Map<String, String> squidJobs = emp.getJobs();
        squidJobs.put("-Mydkwiejfrksdf", "squidward");
        assertEquals(emp.getJobs().get("-Mydkwiejfrksdf"),"squidward");

    }

    @Test
    public void checkMetrics_reputation(){

        Employer emp = new Employer("Mr. Krabs");
        emp.setReputation(55);
        assertEquals(55, emp.getReputation());

    }

    @Test
    public void checkMetrics_totalIncome(){

        Employer emp = new Employer("Mr. Krabs");
        emp.setTotalExpenditure(100);
        assertEquals(100, emp.getTotalExpenditure());

    }


}
