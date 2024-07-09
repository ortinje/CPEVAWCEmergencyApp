package com.bscpe.cpevawcemergencyapp;

public class GenUserData {
    private String key, username, contact;

    public GenUserData (String key, String username, String contact) {
        this.key = key;
        this.username = username;
        this.contact = contact;
    }

    //read data from firebase
    public GenUserData() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
