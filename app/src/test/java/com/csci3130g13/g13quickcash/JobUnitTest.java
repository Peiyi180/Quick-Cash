package com.csci3130g13.g13quickcash;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JobUnitTest {

    @Test
    public void convertDate() throws ParseException {

        Job newJob = new Job(new Employer());
        newJob.setStartDate("5/2/2014");

        Date result = newJob.fetchFullStartDate();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date expectedDate = sdf.parse("5/2/2014");

        assertEquals(expectedDate, result);

    }


    @Test
    public void checkEmployeeID(){

        Job job1 = new Job(new Employer());
        job1.setEmployeeID("squidward");
        job1.setHired(true);
        assertTrue(job1.isHired());
        assertEquals(job1.getEmployeeID(), "squidward");

    }


}
