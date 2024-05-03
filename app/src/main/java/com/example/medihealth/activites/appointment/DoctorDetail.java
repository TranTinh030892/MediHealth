package com.example.medihealth.activites.appointment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.medihealth.R;

public class DoctorDetail extends AppCompatActivity {
    ImageView imageDoctor;
    ImageButton btnBack;
    TextView fullName, gender, specialist, price,degree,number_appointment,experience,achievement,work;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        initView();
    }

    private void initView() {
        btnBack = findViewById(R.id.back_btn);
        imageDoctor = findViewById(R.id.block1_col1);
        fullName = findViewById(R.id.fullName_doctor);
        degree = findViewById(R.id.degree);
        gender = findViewById(R.id.gender);
        specialist = findViewById(R.id.specialist);
        price = findViewById(R.id.price);
        number_appointment = findViewById(R.id.number_appointment);
        experience = findViewById(R.id.experience);
        achievement = findViewById(R.id.achievement);
        work = findViewById(R.id.work);
    }
}