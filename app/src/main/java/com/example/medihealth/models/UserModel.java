package com.example.medihealth.models;

import com.google.firebase.Timestamp;

public class UserModel {
    private String fullName;
    private String gender;
    private String phoneNumber;
    private String address;
    private String birth;
    private Timestamp createdTimestamp;
    private String userId;

    public UserModel() {
    }

    public UserModel(String fullName, String gender, String phoneNumber, String address,
                     String birth, Timestamp createdTimestamp, String userId) {
        this.fullName = fullName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birth = birth;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
    }

    public UserModel(String fullName, Timestamp createdTimestamp, String userId) {
        this.fullName = fullName;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
