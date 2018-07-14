package com.meteor.homework4.model;

public class Person {
    private String name, contactInfo;

    public Person(String name, String contactInfo) {
        this.name=name;
        this.contactInfo=contactInfo;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo=contactInfo;
    }

    public String getInformation() {
        return this.name+"\n"+this.contactInfo;
    }

}
