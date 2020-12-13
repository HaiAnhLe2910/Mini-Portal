package com.example.fhict_app;

public class PersonItem {
    private String givenName;
    private String surName;
    private String phone;
    private String office;
    private String email;


    PersonItem(String givenName, String surName, String phone, String office, String email) {
        this.givenName = givenName;
        this.surName = surName;
        this.phone = phone;
        this.office = office;
        this.email = email;
    }

    public String getName(){return givenName + " " + surName; }
    public String getPhone(){return phone;}
    public String getOffice(){return office;}
    public String getEmail(){return email;}


}
