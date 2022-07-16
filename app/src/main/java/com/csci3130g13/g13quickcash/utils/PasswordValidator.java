package com.csci3130g13.g13quickcash.utils;

public class PasswordValidator {
    private PasswordValidator() {
        throw new IllegalStateException("Non-initialize Util class");
    }

    public static boolean isLengthValid(String password) {
        return password.length() >= 8;
    };

    public static boolean isAlphanumeric(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*[0-9])^.*$");
    };

    public static boolean hasSpecialCharacter(String password) {
        String specialCharacters = "[].@?-+=<>;:`_^'!#$%&*(),/~";
        for (char c: specialCharacters.toCharArray()) {
            if (password.indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    };

    public static boolean hasNoWhiteSpace(String password){
            return password.matches("^[^\\s]*$");
    }

}
