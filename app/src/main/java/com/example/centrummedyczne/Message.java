package com.example.centrummedyczne;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class Message {

    private String text;
    private Date date;
    private boolean to_doctor;
    private DocumentReference doctor_id, patient_id;

    public Message(){

    }

    public Message(String text, Date date, boolean to_doctor,
                   DocumentReference doctor_id, DocumentReference patient_id){
        this.text = text;
        this.date = date;
        this.doctor_id = doctor_id;
        this.patient_id = patient_id;
        this.to_doctor = to_doctor;
    }

    public DocumentReference getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(DocumentReference patient_id) {
        this.patient_id = patient_id;
    }

    public DocumentReference getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(DocumentReference doctor_id) {
        this.doctor_id = doctor_id;
    }

    public boolean isTo_doctor() {
        return to_doctor;
    }

    public void setTo_doctor(boolean to_doctor) {
        this.to_doctor = to_doctor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
