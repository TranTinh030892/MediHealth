package com.example.medihealth.activitys.appointment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medihealth.R;
import com.example.medihealth.activitys.MainActivity;
import com.example.medihealth.models.Appointment;
import com.example.medihealth.models.AppointmentDTO;
import com.example.medihealth.models.Doctor;
import com.example.medihealth.models.UserModel;
import com.example.medihealth.utils.AndroidUtil;
import com.example.medihealth.utils.FirebaseUtil;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Calendar;

public class Confirm_Appointment extends AppCompatActivity implements View.OnClickListener {
    AppointmentDTO appointmentDTO;
    private ImageButton btnBack, btnSupport;
    private Button btnEnter, btnCancel;
    TextView textfullName_Customer, textBirth, textGender, textDate, textSpecialist,
    textfullName_Doctor, textNumberOrder;
    UserModel userModel;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                userModel = task.getResult().toObject(UserModel.class);
            }
            else {
                Log.e("ERROR","Lỗi kết nối mạng");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_appointment);
        initView();
        setOnclick();
        appointmentDTO = AndroidUtil.getAppointmentDTOFromIntent(getIntent());
        setDataApointment(appointmentDTO);
    }
    private void setOnclick() {
        btnCancel.setOnClickListener(this);
        btnEnter.setOnClickListener(this);
    }

    private void initView() {
        btnBack = findViewById(R.id.back_btn);
        btnSupport = findViewById(R.id.support);
        btnEnter = findViewById(R.id.btn_enter);
        btnCancel = findViewById(R.id.btn_cancel);
        textfullName_Customer = findViewById(R.id.fullName_Customer);
        textBirth = findViewById(R.id.birth);
        textGender = findViewById(R.id.gender);
        textDate = findViewById(R.id.date);
        textSpecialist = findViewById(R.id.specialist);
        textfullName_Doctor = findViewById(R.id.nameDoctor);
        textNumberOrder = findViewById(R.id.numberOrder);
    }
    private void setDataApointment(AppointmentDTO appointmentDTO) {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                UserModel userModel = task.getResult().toObject(UserModel.class);
                textfullName_Customer.setText(userModel.getFullName());
                textBirth.setText(userModel.getBirth());
                textGender.setText(userModel.getGender());
                textDate.setText(appointmentDTO.getAppointmentDate());
                textSpecialist.setText(appointmentDTO.getSpecialist());
                if (appointmentDTO.getOrder() < 9) {
                    textNumberOrder.setText("0"+appointmentDTO.getOrder());
                }
                else textNumberOrder.setText(String.valueOf(appointmentDTO.getOrder()));
                textfullName_Doctor.setText(appointmentDTO.getDoctorName());
            }
            else {
                Log.e("ERROR","Lỗi kết nối mạng");
            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_enter){
            saveDataAppoitment(appointmentDTO);
            showDialogConfirm(Gravity.BOTTOM);
        }
        if (v.getId() == R.id.btn_cancel){
            Intent intent = new Intent(Confirm_Appointment.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void saveDataAppoitment(AppointmentDTO appointmentDTO) {
        String doctorId = appointmentDTO.getDoctorId();
        FirebaseUtil.getDoctorDetailsById(doctorId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Doctor doctor = task.getResult().toObject(Doctor.class);

                Appointment appointment = new Appointment(userModel,appointmentDTO.getBookDate(),
                        appointmentDTO.getAppointmentDate(), appointmentDTO.getSpecialist(),doctor,appointmentDTO.getOrder(),
                        appointmentDTO.getSymptom(),appointmentDTO.getStateAppointment());
                FirebaseFirestore.getInstance().collection("appointment")
                        .add(appointment).addOnCompleteListener(task1 -> {
                            if (task.isSuccessful()){
                                Log.e("Successfull","Lưu cuộc hẹn thành công");
                            }
                            else {
                                Log.e("ERROR","Lỗi kết nối mạng");
                            }
                        });
            }
        });
    }

    private void showDialogConfirm(int bottom) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_notify_successfull);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.BOTTOM;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(false);

        Button btnHome, btnDetail;
        btnHome = dialog.findViewById(R.id.btn_Home);
        btnDetail = dialog.findViewById(R.id.btn_detail);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Confirm_Appointment.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Confirm_Appointment.this, MainActivity.class);
                intent.putExtra("requestCode",113);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        dialog.show();
    }
}