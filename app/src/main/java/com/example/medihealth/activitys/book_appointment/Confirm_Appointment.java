package com.example.medihealth.activitys.book_appointment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medihealth.R;

public class Confirm_Appointment extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnBack, btnSupport;
    private Button btnEnter, btnCancel;
    TextView textfullName_Customer, textBirth, textGender, textDate, textSpecialist,
    textfullName_Doctor, textNumberOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_appointment);
        initView();
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_enter){
            showDialogConfirm(Gravity.BOTTOM);
        }
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
            }
        });
        dialog.show();
    }
}