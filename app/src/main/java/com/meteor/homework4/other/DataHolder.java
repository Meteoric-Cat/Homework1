package com.meteor.homework4.other;

public class DataHolder {                                                                           //singleton
    private String name, contactInfo;

    private final static DataHolder instance=new DataHolder();

    private DataHolder() {
        this.name="";
        this.contactInfo="";
    }

    public static DataHolder getInstance() {
        return instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo=contactInfo;
    }

}
