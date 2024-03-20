package com.example.medihealth.activitys.book_appointment;

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
import com.example.medihealth.models.Doctor;
import com.example.medihealth.models.UserModel;
import com.example.medihealth.utils.AndroidUtil;
import com.example.medihealth.utils.FirebaseUtil;
import com.google.firebase.firestore.FirebaseFirestore;

public class Confirm_Appointment extends AppCompatActivity implements View.OnClickListener {
    Appointment appointment;
    private ImageButton btnBack, btnSupport;
    private Button btnEnter, btnCancel;
    TextView textfullName_Customer, textBirth, textGender, textDate, textSpecialist,
    textfullName_Doctor, textNumberOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_appointment);
        appointment = AndroidUtil.getAppointmentModelFromIntent(getIntent());
        initView();
        setDataApointment(appointment);
        setOnclick();
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
    private void setDataApointment(Appointment appointment) {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                UserModel userModel = task.getResult().toObject(UserModel.class);
                textfullName_Customer.setText(userModel.getFullName());
                textBirth.setText(userModel.getBirth());
                textGender.setText(userModel.getGender());
                textDate.setText(appointment.getDate());
                textSpecialist.setText(appointment.getSpecialist());
                if (appointment.getOrder() < 9) {
                    textNumberOrder.setText("0"+appointment.getOrder());
                }
                else textNumberOrder.setText(String.valueOf(appointment.getOrder()));
            }
            else {
                Log.e("ERROR","Lỗi kết nối mạng");
            }
        });
        FirebaseUtil.getDoctorDetailsById(appointment.getDoctorId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Doctor doctor = task.getResult().toObject(Doctor.class);
                textfullName_Doctor.setText(doctor.getFullName());
            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_enter){
            saveDataAppoitment(appointment);
            showDialogConfirm(Gravity.BOTTOM);
        }
        if (v.getId() == R.id.btn_cancel){
            Intent intent = new Intent(Confirm_Appointment.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void saveDataAppoitment(Appointment appointment) {
        FirebaseFirestore.getInstance().collection("Appointment").add(appointment)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Log.d("SUCCESSFULL","Save Appointment successfull");
                    }
                    else {
                        Log.e("ERROR","Lôi kết nối mạng");
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
        dialog.show();
    }
}