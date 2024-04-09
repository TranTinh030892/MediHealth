package com.example.medihealth.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class ConfirmNotification implements Serializable {
    private Long id;
    @SerializedName("check")
    private boolean isCheck;
    private LocalDate date;
    private LocalTime time;
    private String des;

    public ConfirmNotification() {
    }

    public ConfirmNotification(Long id, boolean isCheck, LocalDate date, LocalTime time, String des) {
        this.id = id;
        this.isCheck = isCheck;
        this.date = date;
        this.time = time;
        this.des = des;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
