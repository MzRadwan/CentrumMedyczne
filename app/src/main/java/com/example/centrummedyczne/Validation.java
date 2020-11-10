package com.example.centrummedyczne;

public class Validation {

    public static boolean validatePesel(String pesel){
        return pesel.matches("\\d{11}");
    }

    public static boolean validateEmail(String email){
        return email.matches("^(.+)@(.+)$");
    }

    public static boolean validatePostalCode(String code){
        return code.matches("\\d{2}-\\d{3}");
    }
}
