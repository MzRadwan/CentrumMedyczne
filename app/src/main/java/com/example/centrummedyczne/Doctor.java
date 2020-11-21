package com.example.centrummedyczne;

import com.google.firebase.firestore.DocumentReference;

public class Doctor {

    private String PESEL, photo_url, degree, first_name, last_name, mobile, personal_info;
    private DocumentReference address_id, clinic_id;
    private float average_rate, appointment_price;

    public Doctor(){}

    public Doctor(DocumentReference address_id, DocumentReference clinic_id,
                  String PESEL, String photo_url,
                  String degree, String first_name, String last_name, String mobile, String personal_info , float average_rate, float appointment_price) {
        this.address_id = address_id;
        this.appointment_price = appointment_price;
        this.average_rate = average_rate;
        this.clinic_id = clinic_id;
        this.degree = degree;
        this.first_name = first_name;
        this.last_name = last_name;
        this.mobile = mobile;
        this.personal_info = personal_info;
        this.PESEL = PESEL;
        this.photo_url = photo_url;

    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public float getAppointment_price() {
        return appointment_price;
    }

    public void setAppointment_price(float appointment_price) {
        this.appointment_price = appointment_price;
    }

    public float getAverage_rate() {
        return average_rate;
    }

    public void setAverage_rate(float average_rate) {
        this.average_rate = average_rate;
    }

    public DocumentReference getClinic_id() {
        return clinic_id;
    }

    public void setClinic_id(DocumentReference clinic_id) {
        this.clinic_id = clinic_id;
    }

    public DocumentReference getAddress_id() {
        return address_id;
    }

    public void setAddress_id(DocumentReference address_id) {
        this.address_id = address_id;
    }

    public String getPersonal_info() {
        return personal_info;
    }

    public void setPersonal_info(String personal_info) {
        this.personal_info = personal_info;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLast_name() {
        return last_name;
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

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getPESEL() {
        return PESEL;
    }

    public void setPESEL(String PESEL) {
        this.PESEL = PESEL;
    }
}
