package com.example.medihealth.models;

import com.google.firebase.Timestamp;

public class Employee {
    private String fullName;
    private String userId;
    String tokenId;

    public Employee() {
    }

    public Employee(String fullName, String userId, String tokenId) {
        this.fullName = fullName;
        this.userId = userId;
        this.tokenId = tokenId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
