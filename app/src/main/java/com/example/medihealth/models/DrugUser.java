package com.example.medihealth.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DrugUser implements Serializable {
    private Long id;
    private String name;
    private String userId;
    @SerializedName("active")
    private boolean isActive;

    public DrugUser() {
    }

    public DrugUser(Long id, String name, String userId, boolean isActive) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String toString() {
        return name;
    }
}
