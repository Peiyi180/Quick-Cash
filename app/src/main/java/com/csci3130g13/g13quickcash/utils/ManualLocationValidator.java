package com.csci3130g13.g13quickcash.utils;

public class ManualLocationValidator {

    public static boolean isLengthValid(String country, String city) {
        return country.length() >= 4 && city.length() >= 3;
    }

    public static boolean isAlphanumeric(String country, String city) {
        if (!country.matches("^(?=.*[A-Za-z])(?=.*[0-9])^.*$")
                && !city.matches("^(?=.*[A-Za-z])(?=.*[0-9])^.*$"))
        {return true;}
        return false;
    }

    public static boolean hasNoSpecialCharacter(String country, String city) {
        String specialCharacters = "[].@?-+=<>;:`_^'!#$%&*(),/~";
        for (char c: specialCharacters.toCharArray()) {
            if (country.indexOf(c) != -1 || city.indexOf(c) != -1 ) {
                return false;
            }
        }
        return true;
    }
}
