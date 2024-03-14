package com.example.medihealth.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}
