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

    private String reminderTime;
    private String reminderDate;
    private boolean reminder;

    public Appointment() {
    }

    public Appointment(UserModel userModel, String bookDate, String appointmentDate, String specialist,
                       Doctor doctor, String time, String symptom, int stateAppointment, String reminderTime, String reminderDate, boolean isReminder) {
        this.userModel = userModel;
        this.bookDate = bookDate;
        this.appointmentDate = appointmentDate;
        this.specialist = specialist;
        this.doctor = doctor;
        this.time = time;
        this.symptom = symptom;
        this.stateAppointment = stateAppointment;
        this.reminderTime = reminderTime;
        this.reminderDate = reminderDate;
        this.reminder = isReminder;
    }

    public Appointment(Relative relative, String bookDate, String appointmentDate, String specialist, Doctor doctor, String time, String symptom, int stateAppointment,
                       String reminderTime, String reminderDate, boolean isReminder) {
        this.relative = relative;
        this.bookDate = bookDate;
        this.appointmentDate = appointmentDate;
        this.specialist = specialist;
        this.doctor = doctor;
        this.time = time;
        this.symptom = symptom;
        this.stateAppointment = stateAppointment;
        this.reminderTime = reminderTime;
        this.reminderDate = reminderDate;
        this.reminder = isReminder;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public Relative getRelative() {
        return relative;
    }

    public void setRelative(Relative relative) {
        this.relative = relative;
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

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    public boolean getReminder() {
        return reminder;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }
}
