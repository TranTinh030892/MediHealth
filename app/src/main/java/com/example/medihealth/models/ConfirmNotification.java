package com.example.medihealth.models;

import java.time.LocalDate;

public class ConfirmNotification {
    private Long id;
    private boolean isCheck;
    private LocalDate date;

    public ConfirmNotification() {
    }

    public ConfirmNotification(Long id, boolean isCheck, LocalDate date) {
        this.id = id;
        this.isCheck = isCheck;
        this.date = date;
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
}
