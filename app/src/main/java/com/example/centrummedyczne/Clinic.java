package com.example.centrummedyczne;

import com.google.firebase.firestore.DocumentReference;

public class Clinic {

    private DocumentReference address_id;

    public Clinic(){}

    public Clinic(DocumentReference address_id){
        this.address_id = address_id;
    }

    public DocumentReference getAddress_id() {
        return address_id;
    }

    public void setAddress_id(DocumentReference address_id) {
        this.address_id = address_id;
    }
}
