package com.example.medihealth.models;

import java.io.Serializable;

public class DrugUser implements Serializable {
    private Long id;
    private String name;
    private String userId;

    public DrugUser() {
    }

    public DrugUser(Long id, String name, String userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
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

    public String toString() {
        return name;
    }
}
