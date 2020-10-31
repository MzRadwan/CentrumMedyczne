package com.example.centrummedyczne;

import com.google.firebase.firestore.DocumentReference;

public class Clinic {

    private DocumentReference address_id;
    private String clinic_name;

    public Clinic(){}

    public Clinic(DocumentReference address_id, String clinic_name){
        this.address_id = address_id;
        this.clinic_name = clinic_name;
    }

    public String getClinic_name() {
        return clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }

    public DocumentReference getAddress_id() {
        return address_id;
    }

    public void setAddress_id(DocumentReference address_id) {
        this.address_id = address_id;
    }
}
