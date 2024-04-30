package com.example.medihealth.models;

public class AppointmentDTO {
    private String userId;
    private String relativePhoneNumber;
    private String doctorId;
    private String specialist;
    private String time;
    private String bookDate;
    private String appointmentDate;
    private String symptom;

    public AppointmentDTO() {
    }

    public AppointmentDTO(String userId, String relativePhoneNumber, String doctorId,
                          String specialist, String time, String bookDate, String appointmentDate, String symptom) {
        this.userId = userId;
        this.relativePhoneNumber = relativePhoneNumber;
        this.doctorId = doctorId;
        this.specialist = specialist;
        this.time = time;
        this.bookDate = bookDate;
        this.appointmentDate = appointmentDate;
        this.symptom = symptom;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRelativePhoneNumber() {
        return relativePhoneNumber;
    }

    public void setRelativePhoneNumber(String relativePhoneNumber) {
        this.relativePhoneNumber = relativePhoneNumber;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }
}
