package com.example.medihealth.utils.stat;

import androidx.annotation.NonNull;

import java.time.LocalDate;

public class MonthInfo {
    private String monthLabel;
    private LocalDate dayofmonth;

    public MonthInfo() {
    }

    public MonthInfo(String monthLabel, LocalDate dayofmonth) {
        this.monthLabel = monthLabel;
        this.dayofmonth = dayofmonth;
    }

    public String getMonthLabel() {
        return monthLabel;
    }

    public void setMonthLabel(String monthLabel) {
        this.monthLabel = monthLabel;
    }

    public LocalDate getDayofmonth() {
        return dayofmonth;
    }

    public void setDayofmonth(LocalDate dayofmonth) {
        this.dayofmonth = dayofmonth;
    }

    @NonNull
    @Override
    public String toString() {
        return this.monthLabel;
    }
}
