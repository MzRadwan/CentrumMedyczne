package com.example.centrummedyczne;

public class Specialization {

    private String s_name, s_details;

    public Specialization(){
        //empty constructor
    }


    public Specialization(String s_name, String s_details) {
        this.s_name = s_name;
        this.s_details = s_details;
    }

    public String getS_details() {
        return s_details;
    }

    public void setS_details(String s_details) {
        this.s_details = s_details;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }
}
