package com.example.medihealth.models;

import java.time.LocalDateTime;
import java.util.List;

public class PrescriptionStat extends Prescription {
    private float process;

    public PrescriptionStat(float process) {
        this.process = process;
    }

    public PrescriptionStat(Long id, String title, boolean isActive, DrugUser drugUser, List<PrescriptionItem> prescriptionItems, List<Schedule> schedules, float process) {
        super(id, title, isActive, drugUser, prescriptionItems, schedules);
        this.process = process;
    }

    public float getProcess() {
        return process;
    }

    public void setProcess(float process) {
        this.process = process;
    }
}
