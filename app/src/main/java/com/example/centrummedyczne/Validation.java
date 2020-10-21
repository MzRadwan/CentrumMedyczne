package com.example.centrummedyczne;

public class Validation {

    public static boolean validatePesel(String pesel){
        if(pesel.matches("\\d{11}"))
            return true;
        else
            return false;
    }
}
