package com.example.medihealth.utils.stat;

import androidx.annotation.NonNull;

import java.time.LocalDate;

public class WeekInfo {
    private String weekLabel;
    private LocalDate startDate;
    private LocalDate endDate;

    public WeekInfo(String weekLabel, LocalDate startDate, LocalDate endDate) {
        this.weekLabel = weekLabel;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getWeekLabel() {
        return weekLabel;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @NonNull
    @Override
    public String toString() {
        return this.weekLabel;
    }
}

