package com.example.centrummedyczne;

import com.google.firebase.firestore.DocumentReference;

public class DoctorHasSpecialization {

    DocumentReference doctor_id, specialization_id;

    public DoctorHasSpecialization(){}


    public DoctorHasSpecialization(DocumentReference doctor_id, DocumentReference specialization_id){
        this.doctor_id = doctor_id;
        this.specialization_id = specialization_id;
    }

    public DocumentReference getSpecialization_id() {
        return specialization_id;
    }

    public void setSpecialization_id(DocumentReference specialization_id) {
        this.specialization_id = specialization_id;
    }

    public DocumentReference getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(DocumentReference doctor_id) {
        this.doctor_id = doctor_id;
    }
}
