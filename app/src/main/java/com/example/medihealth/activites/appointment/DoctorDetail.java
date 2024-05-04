package com.example.medihealth.activites.appointment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.medihealth.R;
import com.example.medihealth.models.Doctor;
import com.example.medihealth.utils.AndroidUtil;
import com.example.medihealth.utils.FirebaseUtil;
import com.google.firebase.firestore.Query;

import java.text.DecimalFormat;

public class DoctorDetail extends AppCompatActivity {
    Doctor doctor;
    ImageView imageDoctor;
    ImageButton btnBack;
    TextView fullName, gender, specialist, price,degree,number_appointment,experience,achievement,work;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        doctor = AndroidUtil.getDoctorModelFromIntent(getIntent());
        initView();
        setupDetailDoctor(doctor);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
    private void setupDetailDoctor(Doctor doctor) {
        if (doctor.getGender().equals("Nam") || doctor.getGender().equals("")) {
            imageDoctor.setImageResource(R.drawable.doctor);
        } else {
            imageDoctor.setImageResource(R.drawable.doctor2);
        }
        fullName.setText(doctor.getFullName());
        degree.setText(doctor.getDegree());
        gender.setText(doctor.getGender());
        specialist.setText(doctor.getSpecialist());
        price.setText(formatPrice(doctor.getPrice())+" đ");
        experience.setText(doctor.getExperience());
        if (!doctor.getAchievement().equals("")){
            String formatAchievement = doctor.getAchievement().replace(". ", ".<br>");
            achievement.setText(Html.fromHtml(formatAchievement));
        }
        else achievement.setText(doctor.getAchievement());

        if (!doctor.getWork().equals("")){
            String formatWork = doctor.getWork().replace(". ", ".<br>");
            work.setText(Html.fromHtml(formatWork));
        }
        else work.setText(doctor.getWork());

        Query query = FirebaseUtil.getAppointmentCollectionReference()
                .whereEqualTo("doctor.doctorId",doctor.getDoctorId());
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                int number = task.getResult().size();
                number_appointment.setText(String.valueOf(number));
            }
            else {
                Log.e("ERROR","Lỗi kết nối");
            }
        });
    }
    private String formatPrice(int price){
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedNumber = decimalFormat.format(price);
        return formattedNumber;
    }
}