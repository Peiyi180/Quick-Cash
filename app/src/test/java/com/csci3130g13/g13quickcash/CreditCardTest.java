package com.csci3130g13.g13quickcash;

import org.junit.Test;

import static org.junit.Assert.*;


public class CreditCardTest {

    @Test
    public void setSameCardNumberTest(){
        Employer user1 = new Employer("tester1");
        Employer user2 = new Employer("tester2");
        user1.setCreditCardNumber("1234 1234 1234 1234");
        user2.setCreditCardNumber("1234 1234 1234 1234");
        assertEquals(user1.getCreditCardNumber(), user2.getCreditCardNumber());
    }

    @Test
    public void setDifferentCardNumberTest(){
        Employer user1 = new Employer("tester1");
        Employer user2 = new Employer("tester2");
        user1.setCreditCardNumber("1234 1234 1234 1234");
        user2.setCreditCardNumber("2341 1244 1245 1294");
        assertNotEquals(user1.getCreditCardNumber(), user2.getCreditCardNumber());
    }

    @Test
    public void setEmptyCardNumberTest(){
        Employer user1 = new Employer("tester1");
        user1.setCreditCardNumber("");
        assertEquals(user1.getCreditCardNumber(), "");
    }

    @Test
    public void getCardNumberTest(){
        Employer user1 = new Employer("tester1");
        user1.setCreditCardNumber("1234 1234 1234 1234");
        assertEquals(user1.getCreditCardNumber(), "1234 1234 1234 1234");
    }



}
