package com.example.centrummedyczne;

public class Address {

    private String city, apartment, building_number, postal_code, street;

    public Address(){}

    public Address(String city, String apartment, String building_number, String postal_code, String street){
        this.city = city;
        this.apartment = apartment;
        this.building_number = building_number;
        this.postal_code = postal_code;
        this.street = street;
    }

    public String getFullAddress(){
        String cmAddress = "";
        cmAddress += city + ", "
                + street + " " + building_number;
        if (apartment != null){
            cmAddress += "/" + apartment;
        }
        return cmAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getBuilding_number() {
        return building_number;
    }

    public void setBuilding_number(String building_number) {
        this.building_number = building_number;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }
}
