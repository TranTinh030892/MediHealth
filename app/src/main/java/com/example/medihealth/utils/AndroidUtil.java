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
import com.example.medihealth.models.Drug;
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
    public static void passDrugModelAsIntent(Intent intent, Drug model){
        intent.putExtra("name",model.getName());
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
        drug.setIngredients(intent.getStringExtra("ingredients"));
        drug.setFunction(intent.getStringExtra("function"));
        drug.setExpiry(intent.getStringExtra("expiry"));
        drug.setSideEffects(intent.getStringExtra("sideEffects"));
        drug.setContraindicated(intent.getStringExtra("contraindicated"));
        drug.setInteractions(intent.getStringExtra("interactions"));
        return drug;
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
    public static String getBMI(int height, int weight){
        double heightDouble = (double) height/100;
        double BMI = (double) weight/(heightDouble * heightDouble);
        double roundedNumber = Math.round(BMI * 10) / 10.0;
        return String.valueOf(roundedNumber);
    }
}
