package com.example.medihealth.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

public class Schedule implements Serializable {
    private Long id;
    private LocalTime time;
    private Prescription prescription;
    private List<ConfirmNotification> listCN;
    @SerializedName("active")
    private boolean isActive;

    public Schedule() {
    }

    public Schedule(Long id, LocalTime time, Prescription prescription, List<ConfirmNotification> listCN) {
        this.id = id;
        this.time = time;
        this.listCN = listCN;
        this.prescription = prescription;
    }

    public Schedule(LocalTime time) {
        this.time = time;
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

    public List<ConfirmNotification> getListCN() {
        return listCN;
    }

    public void setListCN(List<ConfirmNotification> listCN) {
        this.listCN = listCN;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
