package com.example.medihealth.models;

public class AppointmentDTO {
    private String userName;
    private String bookDate;
    private String appointmentDate;
    private String specialist;
    private String doctorId;
    private String doctorName;
    private String time;
    private String symptom;
    private int stateAppointment;

    public AppointmentDTO() {
    }
    public AppointmentDTO(String userName,String bookDate, String appointmentDate, String specialist,String doctorId,
                          String doctorName, String time, String symptom, int stateAppointment) {
        this.userName = userName;
        this.bookDate = bookDate;
        this.appointmentDate = appointmentDate;
        this.specialist = specialist;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.time = time;
        this.symptom = symptom;
        this.stateAppointment = stateAppointment;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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
}
