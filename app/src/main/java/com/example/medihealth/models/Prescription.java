package com.example.medihealth.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Prescription implements Serializable {
    private Long id;
    private String title;
    @SerializedName("active")
    private boolean isActive;
    private DrugUser drugUser;
    private LocalDateTime createdAt;
    private List<PrescriptionItem> prescriptionItems;
    private List<Schedule> schedules;

    public Prescription() {
    }

    public Prescription(Long id, String title, boolean isActive, DrugUser drugUser, LocalDateTime createdAt, List<PrescriptionItem> prescriptionItems, List<Schedule> schedules) {
        this.id = id;
        this.title = title;
        this.isActive = isActive;
        this.drugUser = drugUser;
        this.createdAt = createdAt;
        this.prescriptionItems = prescriptionItems;
        this.schedules = schedules;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public DrugUser getDrugUser() {
        return drugUser;
    }

    public void setDrugUser(DrugUser drugUser) {
        this.drugUser = drugUser;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<PrescriptionItem> getPrescriptionItems() {
        return prescriptionItems;
    }

    public void setPrescriptionItems(List<PrescriptionItem> prescriptionItems) {
        this.prescriptionItems = prescriptionItems;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    @NonNull
    @Override
    public String toString() {
        return this.title;
    }
}
