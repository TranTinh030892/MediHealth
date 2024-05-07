package com.example.medihealth.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

public class Schedule implements Serializable {
    private Long id;
    private LocalTime time;
    @SerializedName("active")
    private boolean isActive;
    private Prescription prescription;
    private List<ConfirmNotification> confirmNotifications;

    public Schedule() {
    }

    public Schedule(Long id, LocalTime time, boolean isActive, Prescription prescription, List<ConfirmNotification> confirmNotifications) {
        this.id = id;
        this.time = time;
        this.isActive = isActive;
        this.prescription = prescription;
        this.confirmNotifications = confirmNotifications;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public List<ConfirmNotification> getConfirmNotifications() {
        return confirmNotifications;
    }

    public void setConfirmNotifications(List<ConfirmNotification> confirmNotifications) {
        this.confirmNotifications = confirmNotifications;
    }
}
