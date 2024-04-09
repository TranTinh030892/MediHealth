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
import com.example.medihealth.models.AppointmentDTO;
import com.example.medihealth.models.Employee;
import com.example.medihealth.models.UserModel;


public class AndroidUtil {

   public static  void showToast(Context context,String message){
       Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

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
    public static void passAppointmentDTOAsIntent(Intent intent, AppointmentDTO model){
        intent.putExtra("userName",model.getUserName());
        intent.putExtra("bookDate",model.getBookDate());
        intent.putExtra("appointmentDate",model.getAppointmentDate());
        intent.putExtra("specialist",model.getSpecialist());
        intent.putExtra("doctorId",model.getDoctorId());
        intent.putExtra("doctorName",model.getDoctorName());
        intent.putExtra("order",model.getOrder());
        intent.putExtra("symptom",model.getSymptom());
        intent.putExtra("stateAppointment",model.getStateAppointment());
    }
    public static AppointmentDTO getAppointmentDTOFromIntent(Intent intent){
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setUserName(intent.getStringExtra("userName"));
        appointmentDTO.setBookDate(intent.getStringExtra("bookDate"));
        appointmentDTO.setAppointmentDate(intent.getStringExtra("appointmentDate"));
        appointmentDTO.setSpecialist(intent.getStringExtra("specialist"));
        appointmentDTO.setDoctorId(intent.getStringExtra("doctorId"));
        appointmentDTO.setDoctorName(intent.getStringExtra("doctorName"));
        appointmentDTO.setOrder(intent.getIntExtra("order",0));
        appointmentDTO.setSymptom(intent.getStringExtra("symptom"));
        appointmentDTO.setStateAppointment(intent.getIntExtra("stateAppointment",-1));
        return appointmentDTO;
    }
    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }
}
