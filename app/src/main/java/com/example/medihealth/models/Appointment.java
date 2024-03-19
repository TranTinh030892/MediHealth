package com.example.medihealth.models;

public class Appointment {
    private String userId;
    private String date;
    private String specialist;
    private String doctorId;
    private int order;
    private String symptom;
    private int stateAppointment;

    public Appointment() {
    }

    public Appointment(String userId, String date, String specialist,
                       String doctorId, int order, String symptom, int stateAppointment) {
        this.userId = userId;
        this.date = date;
        this.specialist = specialist;
        this.doctorId = doctorId;
        this.order = order;
        this.symptom = symptom;
        this.stateAppointment = stateAppointment;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public int getStateAppointment() {
        return stateAppointment;
    }

    public void setStateAppointment(int stateAppointment) {
        this.stateAppointment = stateAppointment;
    }
}
