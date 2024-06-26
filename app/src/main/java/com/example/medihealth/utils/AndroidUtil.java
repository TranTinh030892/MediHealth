package com.example.medihealth.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.medihealth.models.Appointment;
import com.example.medihealth.models.AppointmentConfirm;
import com.example.medihealth.models.AppointmentDTO;
import com.example.medihealth.models.Doctor;
import com.example.medihealth.models.Drug;
import com.example.medihealth.models.Employee;
import com.example.medihealth.models.Relative;
import com.example.medihealth.models.UserModel;

import java.text.DecimalFormat;
import java.util.Calendar;


public class AndroidUtil {

    public static void passUserModelAsIntent(Intent intent, UserModel model){
       intent.putExtra("username",model.getFullName());
       intent.putExtra("userId",model.getUserId());
    }

    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel userModel = new UserModel();
        userModel.setFullName(intent.getStringExtra("username"));
        userModel.setUserId(intent.getStringExtra("userId"));
        return userModel;
    }

    public static void passEmployeeModelAsIntent(Intent intent, Employee model){
        intent.putExtra("fullName",model.getFullName());
        intent.putExtra("userId",model.getUserId());
    }
    public static Employee getEmployeeModelFromIntent(Intent intent){
        Employee employee = new Employee();
        employee.setFullName(intent.getStringExtra("fullName"));
        employee.setUserId(intent.getStringExtra("userId"));
        employee.setTokenId(intent.getStringExtra("tokenId"));
        return employee;
    }
    public static void passAppoitmentDTOModelAsIntent(Intent intent, AppointmentDTO model){
        intent.putExtra("userId",model.getUserId());
        intent.putExtra("relativePhoneNumber",model.getRelativePhoneNumber());
        intent.putExtra("specialist",model.getSpecialist());
        intent.putExtra("doctorId",model.getDoctorId());
        intent.putExtra("time",model.getTime());
        intent.putExtra("bookDate",model.getBookDate());
        intent.putExtra("appointmentDate",model.getAppointmentDate());
        intent.putExtra("symptom",model.getSymptom());
    }
    public static AppointmentDTO getAppointmentDTOModelFromIntent(Intent intent){
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setUserId(intent.getStringExtra("userId"));
        appointmentDTO.setRelativePhoneNumber(intent.getStringExtra("relativePhoneNumber"));
        appointmentDTO.setSpecialist(intent.getStringExtra("specialist"));
        appointmentDTO.setDoctorId(intent.getStringExtra("doctorId"));
        appointmentDTO.setTime(intent.getStringExtra("time"));
        appointmentDTO.setBookDate(intent.getStringExtra("bookDate"));
        appointmentDTO.setAppointmentDate(intent.getStringExtra("appointmentDate"));
        appointmentDTO.setSymptom(intent.getStringExtra("symptom"));
        return appointmentDTO;
    }
    public static void passAppoitmentConfirmModelAsIntent(Intent intent, AppointmentConfirm model){
        intent.putExtra("personName",model.getPersonName());
        intent.putExtra("personPhone",model.getPhoneNumber());
        intent.putExtra("relationship",model.getRelationship());
        intent.putExtra("gender",model.getGender());
        intent.putExtra("birth",model.getBirth());
        intent.putExtra("specialist",model.getSpecialist());
        intent.putExtra("doctorName",model.getDoctorName());
        intent.putExtra("price",model.getPrice());
        intent.putExtra("time",model.getTime());
        intent.putExtra("appointmentDate",model.getAppointmentDate());
        intent.putExtra("symptom",model.getSymptom());
    }
    public static AppointmentConfirm getAppointmentConfirmModelFromIntent(Intent intent){
        AppointmentConfirm appointmentConfirm = new AppointmentConfirm();
        appointmentConfirm.setPersonName(intent.getStringExtra("personName"));
        appointmentConfirm.setPhoneNumber(intent.getStringExtra("personPhone"));
        appointmentConfirm.setRelationship(intent.getStringExtra("relationship"));
        appointmentConfirm.setGender(intent.getStringExtra("gender"));
        appointmentConfirm.setBirth(intent.getStringExtra("birth"));
        appointmentConfirm.setSpecialist(intent.getStringExtra("specialist"));
        appointmentConfirm.setDoctorName(intent.getStringExtra("doctorName"));
        appointmentConfirm.setPrice(intent.getIntExtra("price",1000000));
        appointmentConfirm.setTime(intent.getStringExtra("time"));
        appointmentConfirm.setAppointmentDate(intent.getStringExtra("appointmentDate"));
        appointmentConfirm.setSymptom(intent.getStringExtra("symptom"));
        return appointmentConfirm;
    }
    public static void passRelativeModelAsIntent(Intent intent, Relative model){
        intent.putExtra("fullName",model.getFullName());
        intent.putExtra("gender",model.getGender());
        intent.putExtra("phoneNumber",model.getPhoneNumber());
        intent.putExtra("address",model.getAddress());
        intent.putExtra("birth",model.getBirth());
        intent.putExtra("height",model.getHeight());
        intent.putExtra("weight",model.getWeight());
        intent.putExtra("relationship",model.getRelationship());
    }
    public static Relative getRelativeModelFromIntent(Intent intent){
        Relative relative = new Relative();
        relative.setFullName(intent.getStringExtra("fullName"));
        relative.setGender(intent.getStringExtra("gender"));
        relative.setBirth(intent.getStringExtra("birth"));
        relative.setPhoneNumber(intent.getStringExtra("phoneNumber"));
        relative.setAddress(intent.getStringExtra("address"));
        relative.setHeight(intent.getIntExtra("height",160));
        relative.setWeight(intent.getIntExtra("weight",50));
        relative.setRelationship(intent.getStringExtra("relationship"));
        return relative;
    }
    public static void passDrugModelAsIntent(Intent intent, Drug model){
        intent.putExtra("name",model.getName());
        intent.putExtra("image",model.getImage());
        intent.putExtra("ingredients",model.getIngredients());
        intent.putExtra("function",model.getFunction());
        intent.putExtra("expiry",model.getExpiry());
        intent.putExtra("sideEffects",model.getSideEffects());
        intent.putExtra("contraindicated",model.getContraindicated());
        intent.putExtra("interactions",model.getInteractions());
    }

    public static Drug getDrugModelFromIntent(Intent intent){
        Drug drug = new Drug();
        drug.setName(intent.getStringExtra("name"));
        drug.setImage(intent.getStringExtra("image"));
        drug.setIngredients(intent.getStringExtra("ingredients"));
        drug.setFunction(intent.getStringExtra("function"));
        drug.setExpiry(intent.getStringExtra("expiry"));
        drug.setSideEffects(intent.getStringExtra("sideEffects"));
        drug.setContraindicated(intent.getStringExtra("contraindicated"));
        drug.setInteractions(intent.getStringExtra("interactions"));
        return drug;
    }
    public static void passDoctorModelAsIntent(Intent intent, Doctor model){
        intent.putExtra("doctorId",model.getDoctorId());
        intent.putExtra("fullName",model.getFullName());
        intent.putExtra("gender",model.getGender());
        intent.putExtra("phoneNumber",model.getPhoneNumber());
        intent.putExtra("degree",model.getDegree());
        intent.putExtra("specialist",model.getSpecialist());
        intent.putExtra("experience",model.getExperience());
        intent.putExtra("achievement",model.getAchievement());
        intent.putExtra("work",model.getWork());
        intent.putExtra("price",model.getPrice());
    }

    public static Doctor getDoctorModelFromIntent(Intent intent){
        Doctor doctor = new Doctor();
        doctor.setDoctorId(intent.getStringExtra("doctorId"));
        doctor.setFullName(intent.getStringExtra("fullName"));
        doctor.setGender(intent.getStringExtra("gender"));
        doctor.setPhoneNumber(intent.getStringExtra("phoneNumber"));
        doctor.setDegree(intent.getStringExtra("degree"));
        doctor.setSpecialist(intent.getStringExtra("specialist"));
        doctor.setExperience(intent.getStringExtra("experience"));
        doctor.setAchievement(intent.getStringExtra("achievement"));
        doctor.setWork(intent.getStringExtra("work"));
        doctor.setPrice(intent.getIntExtra("price",100000));
        return doctor;
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }
    public static String getBMI(int height, int weight){
        double heightDouble = (double) height/100;
        double BMI = (double) weight/(heightDouble * heightDouble);
        double roundedNumber = Math.round(BMI * 10) / 10.0;
        return String.valueOf(roundedNumber);
    }
    public static String formatPrice(int price){
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedNumber = decimalFormat.format(price);
        return formattedNumber;
    }
    public static String currentTime (){
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        String hourStr = String.valueOf(currentHour);
        String minuteStr = String.valueOf(currentMinute);
        if (currentHour < 10)hourStr = "0"+currentHour;
        if (currentMinute < 10)minuteStr = "0"+currentMinute;
        return hourStr+":"+minuteStr;
    }
}
