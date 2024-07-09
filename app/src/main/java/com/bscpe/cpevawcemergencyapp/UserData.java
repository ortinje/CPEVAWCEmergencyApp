package com.bscpe.cpevawcemergencyapp;

import java.io.Serializable;
import java.util.Map;

public class UserData implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private int age;
    private String address;
    private String birthdate;
    private String civilStatus;
    private String contactNum;
    private String email;
    private String sex;
    private HealthInfo healthInfo;
    private EmergencyContact emergencyContact;

    public UserData() {
        // Default constructor for Firebase
    }

    // Getters and setters for all the properties

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // Add getters and setters for other properties

    public static class HealthInfo implements Serializable {
        private String bloodType;
        private String foodAllergies;
        private String height;
        private String medicalHistory;
        private String surgeries;
        private String weight;

        // Getters and setters for HealthInfo properties
    }

    public static class EmergencyContact implements Serializable {
        private Map<String, Long> contacts;

        // Getters and setters for EmergencyContact properties
    }
}

