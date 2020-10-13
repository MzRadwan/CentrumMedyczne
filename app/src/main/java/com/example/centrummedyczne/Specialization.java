package com.example.centrummedyczne;

public class Specialization {

    private String specialization_name, specialization_details, docID;

    public Specialization(){
        //empty constructor
    }


    public Specialization(String specialization_name, String specialization_details, String docID) {
        this.specialization_name = specialization_name;
        this.specialization_details = specialization_details;
        this.docID = docID;
    }

    public String getSpecialization_details() {
        return specialization_details;
    }

    public void setSpecialization_details(String specialization_details) {
        this.specialization_details = specialization_details;
    }

    public String getSpecialization_name() {
        return specialization_name;
    }

    public void setSpecialization_name(String specialization_name) {
        this.specialization_name = specialization_name;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }
}
