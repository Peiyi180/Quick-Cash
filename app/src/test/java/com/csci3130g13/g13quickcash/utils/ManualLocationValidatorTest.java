package com.csci3130g13.g13quickcash.utils;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ManualLocationValidatorTest {

    @Test
    public void checkInputLength() {
//      Country length is at least 4 letters
//      City length is at least 3 letters
        String countryGood = "Chad";
        String countryBad = "Hey";
        String cityGood = "Ufa";
        String cityBad = "Uf";
        assertTrue(ManualLocationValidator.isLengthValid(countryGood, cityGood)); // legal country and city input
        assertFalse(ManualLocationValidator.isLengthValid(countryBad, cityBad)); // not legal country and city input
    }


    @Test
    public void checkInputAlphanumeric() {
//      country or city should not be alphanumeric
        String countryGood = "Chad";
        String countryBad = "Chad7";
        String cityGood = "Ufa";
        String cityBad = "Ufa7";
        assertTrue(ManualLocationValidator.isAlphanumeric(countryGood, cityGood)); // legal country and city input
        assertFalse(ManualLocationValidator.isAlphanumeric(countryBad, cityBad)); // not legal country and city input

    }


    @Test
    public void checkInputSpecialCharacter() {
//      country or city should not contain a special character
        String specialCharacters = ".@?-+=<>;:`_^'!#$%&*(),/~";
        String countryGood = "Chad";
        String cityGood = "Ufa";

        for (char sc: specialCharacters.toCharArray()) {
            String countryBad = "Chad" + sc;;
            String cityBad = "Ufa" + sc;;
            assertFalse(ManualLocationValidator.hasNoSpecialCharacter(countryBad, cityBad)); // not legal country and city input
        }
        assertTrue(ManualLocationValidator.hasNoSpecialCharacter(countryGood, cityGood)); // legal country and city input
    }



}
