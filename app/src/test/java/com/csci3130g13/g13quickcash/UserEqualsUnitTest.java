package com.csci3130g13.g13quickcash;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for the overridden equals() method of User
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserEqualsUnitTest {

    @Test
    public void testAllFieldsSame(){
        User u1= new Employer("amy123");
        User u2= new Employer("amy123");
        u1.setName("amy");
        u2.setName("amy");
        u1.setCity("boston");
        u2.setCity("boston");
        u1.setEmail("amy123@dal.ca");
        u2.setEmail("amy123@dal.ca");

        assertEquals(u1, u2);
    }

    @Test
    public void testDifferentName(){

        User u1= new Employer("amy123");
        User u2= new Employer("amy123");
        u1.setName("amy");
        u2.setName("karen");
        u1.setCity("boston");
        u2.setCity("boston");
        u1.setEmail("amy123@dal.ca");
        u2.setEmail("amy123@dal.ca");

        assertNotEquals(u1, u2);
    }

    @Test
    public void testDifferentID(){

        User u1= new Employer("amy123");
        User u2= new Employer("amy1234");
        u1.setName("amy");
        u2.setName("amy");
        u1.setCity("boston");
        u2.setCity("boston");
        u1.setEmail("amy123@dal.ca");
        u2.setEmail("amy123@dal.ca");

        assertNotEquals(u1, u2);
    }

    @Test
    public void testDifferentEmail(){

        User u1= new Employer("amy123");
        User u2= new Employer("amy123");
        u1.setName("amy");
        u2.setName("amy");
        u1.setCity("boston");
        u2.setCity("boston");
        u1.setEmail("amy1223@dal.ca");
        u2.setEmail("amy123@dal.ca");

        assertNotEquals(u1, u2);
    }

    @Test
    public void testDifferentCity(){

        User u1= new Employer("amy123");
        User u2= new Employer("amy123");
        u1.setName("amy");
        u2.setName("amy");
        u1.setCity("boston");
        u2.setCity("new york");
        u1.setEmail("amy123@dal.ca");
        u2.setEmail("amy123@dal.ca");

        assertNotEquals(u1, u2);
    }


    @Test
    public void testDifferentUserType(){

        User u1= new Employer("amy123");
        User u2= new Employee("amy123");
        u1.setName("amy");
        u2.setName("amy");
        u1.setCity("boston");
        u2.setCity("boston");
        u1.setEmail("amy123@dal.ca");
        u2.setEmail("amy123@dal.ca");

        assertNotEquals(u1, u2);
    }

    @Test
    public void testDifferentEmployerFields(){

        Employer u1= new Employer("amy123");
        Employer u2= new Employer("amy123");
        u1.setName("amy");
        u2.setName("amy");
        u1.setCity("boston");
        u2.setCity("boston");
        u1.setEmail("amy123@dal.ca");
        u2.setEmail("amy123@dal.ca");
        u1.setHirePreference1("secretary");
        u2.setHirePreference1("assistant");
        assertNotEquals(u1, u2);

    }


    @Test
    public void testDifferentEmployeeFields(){

        Employee u1= new Employee("amy123");
        Employee u2= new Employee("amy123");
        u1.setName("amy");
        u2.setName("amy");
        u1.setCity("boston");
        u2.setCity("boston");
        u1.setEmail("amy123@dal.ca");
        u2.setEmail("amy123@dal.ca");
        u1.setProfession("secretary");
        u2.setProfession("assistant");
        assertNotEquals(u1, u2);

    }



}