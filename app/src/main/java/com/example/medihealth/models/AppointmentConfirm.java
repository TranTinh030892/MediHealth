package com.example.medihealth.models;

public class AppointmentConfirm {
    String personName;
    String relationship;
    String gender;
    String birth;
    String specialist;
    String doctorName;
    String time;
    String appointmentDate;
    String symptom;

    public AppointmentConfirm() {
    }

    public AppointmentConfirm(String personName, String relationship, String gender,
                              String birth, String specialist, String doctorName, String time, String appointmentDate, String symptom) {
        this.personName = personName;
        this.relationship = relationship;
        this.gender = gender;
        this.birth = birth;
        this.specialist = specialist;
        this.doctorName = doctorName;
        this.time = time;
        this.appointmentDate = appointmentDate;
        this.symptom = symptom;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
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
