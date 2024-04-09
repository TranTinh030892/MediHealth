package com.example.medihealth.models;

public class Appointment {
    private UserModel userModel;
    private String bookDate;
    private String appointmentDate;
    private String specialist;
    private Doctor doctor;
    private int order;
    private String symptom;
    private int stateAppointment;

    public Appointment() {
    }

    public Appointment(UserModel userModel,String bookDate, String appointmentDate, String specialist, Doctor doctor, int order,
                       String symptom, int stateAppointment) {
        this.userModel = userModel;
        this.bookDate = bookDate;
        this.appointmentDate = appointmentDate;
        this.specialist = specialist;
        this.doctor = doctor;
        this.order = order;
        this.symptom = symptom;
        this.stateAppointment = stateAppointment;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
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
