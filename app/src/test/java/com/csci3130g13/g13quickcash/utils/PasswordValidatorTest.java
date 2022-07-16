package com.csci3130g13.g13quickcash.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class PasswordValidatorTest {

    @Test
    public void checkLength() {
//      Password length is at least 8 digits
        String passGood = "12345678";
        String passBad = "1234567";
        assertTrue(PasswordValidator.isLengthValid(passGood)); // legal password
        assertFalse(PasswordValidator.isLengthValid(passBad)); // not legal password
    }

    @Test
    public void checkAlphanumeric() {
//      Password should be alphanumeric
        String passGood = "12345678A";
        String passBad = "12345678";
        assertTrue(PasswordValidator.isAlphanumeric(passGood)); // legal password
        assertFalse(PasswordValidator.isAlphanumeric(passBad)); // not legal password
    }

    @Test
    public void checkSpecialCharacter() {
//      Password should contain at least one special character
        String specialCharacters = ".@?-+=<>;:`_^'!#$%&*(),/~";
        String passBad = "12345678A";

        for (char sc: specialCharacters.toCharArray()) {
            String passGood = "12345678A" + sc;
            assertTrue(PasswordValidator.hasSpecialCharacter(passGood)); // legal password
        }
        assertFalse(PasswordValidator.hasSpecialCharacter(passBad)); // not legal password
    }

    @Test
    public void hasWhiteSpace() {
//      Password should not contain whitespace
        String passGood = ".12345678A";
        String passBad = ".12345 678A";
        assertTrue(PasswordValidator.hasNoWhiteSpace(passGood)); // legal password
        assertFalse(PasswordValidator.hasNoWhiteSpace(passBad)); // not legal password
    }

}
