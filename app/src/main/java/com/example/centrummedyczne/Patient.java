package com.example.centrummedyczne;

import com.google.firebase.firestore.DocumentReference;

public class Patient {

    DocumentReference address_id;
    String PESEL, first_name, last_name, mobile;

    public Patient(){}

    public Patient(String PESEL, String first_name, String last_name, String mobile, DocumentReference address_id){
        this.address_id = address_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.mobile = mobile;
        this.PESEL = PESEL;
    }

    public DocumentReference getAddress_id() {
        return address_id;
    }

    public void setAddress_id(DocumentReference address_id) {
        this.address_id = address_id;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getPESEL() {
        return PESEL;
    }

    public void setPESEL(String PESEL) {
        this.PESEL = PESEL;
    }
}
