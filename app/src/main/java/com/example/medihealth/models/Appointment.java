package com.example.medihealth.models;

public class Appointment {
    private UserModel userModel;
    private Relative relative;
    private String bookDate;
    private String appointmentDate;
    private String specialist;
    private Doctor doctor;
    private String time;
    private String symptom;
    private int stateAppointment;

    public Appointment() {
    }

    public Appointment(UserModel userModel,String bookDate, String appointmentDate, String specialist, Doctor doctor, String time,
                       String symptom, int stateAppointment) {
        this.userModel = userModel;
        this.bookDate = bookDate;
        this.appointmentDate = appointmentDate;
        this.specialist = specialist;
        this.doctor = doctor;
        this.time = time;
        this.symptom = symptom;
        this.stateAppointment = stateAppointment;
    }

    public Appointment(Relative relative, String bookDate, String appointmentDate, String specialist,
                       Doctor doctor, String time, String symptom, int stateAppointment) {
        this.relative = relative;
        this.bookDate = bookDate;
        this.appointmentDate = appointmentDate;
        this.specialist = specialist;
        this.doctor = doctor;
        this.time = time;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public Relative getRelative() {
        return relative;
    }

    public void setRelative(Relative relative) {
        this.relative = relative;
    }
}
