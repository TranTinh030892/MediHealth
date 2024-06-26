package com.example.medihealth.models;

public class AppointmentConfirm {
    String personName;
    String relationship;
    String phoneNumber;
    String gender;
    String birth;
    String specialist;
    String doctorName;
    int price;
    String time;
    String appointmentDate;
    String symptom;

    public AppointmentConfirm() {
    }

    public AppointmentConfirm(String personName, String relationship, String phoneNumber,
                              String gender, String birth, String specialist, String doctorName, int price, String time, String appointmentDate, String symptom) {
        this.personName = personName;
        this.relationship = relationship;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birth = birth;
        this.specialist = specialist;
        this.doctorName = doctorName;
        this.price = price;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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
