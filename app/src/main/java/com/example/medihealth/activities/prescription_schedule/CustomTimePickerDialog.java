package com.example.medihealth.activities.prescription_schedule;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.example.medihealth.R;

import java.time.LocalTime;

public class CustomTimePickerDialog extends Dialog {

    TextView tvTitle;
    TimePicker tpTime;
    Button btnNegative;
    Button btnPositive;

    public CustomTimePickerDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_timepicker);
        this.tvTitle = findViewById(R.id.tv_title);
        this.tpTime = findViewById(R.id.tp_time);
        this.btnNegative = findViewById(R.id.btn_negative);
        this.btnPositive = findViewById(R.id.btn_positive);

        tpTime.setHour(LocalTime.now().getHour());
        tpTime.setMinute(LocalTime.now().getMinute());

        setCancelable(false);
        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
    }

    public void setIs24HourView(boolean is24HourView) {
        this.tpTime.setIs24HourView(is24HourView);
    }

    public void setTime(int hour, int minute) {
        this.tpTime.setHour(hour);
        this.tpTime.setMinute(minute);
    }

    public void setTitle(String title) {
        this.tvTitle.setText(title);
    }

    public void setNegativeButton(String content, View.OnClickListener onClickListener) {
        this.btnNegative.setText(content);
        this.btnNegative.setOnClickListener(onClickListener);
    }

    public void setPositiveButton(String content, onClickPositiveButtonListener onClickListener) {
        this.btnPositive.setText(content);
        this.btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(tpTime.getHour(), tpTime.getMinute());
            }
        });
    }

    public interface onClickPositiveButtonListener {
        void onClick(int hour, int minute);
    }
}
