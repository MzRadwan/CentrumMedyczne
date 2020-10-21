package com.example.centrummedyczne;

public class Validation {

    public static boolean validatePesel(String pesel){
        if(pesel.matches("\\d{11}"))
            return true;
        else
            return false;
    }

    public static boolean validateEmail(String email){
        if(email.matches("^(.+)@(.+)$"))
            return true;
        else
            return false;
    }

    public static boolean validatePostalCode(String code){
        if(code.matches("\\d{2}-\\d{3}"))
            return true;
        else
            return false;
    }
}
