package com.example.medihealth.activities.show_schedule_totay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.adapters.show_schedule_today.PrescriptionItemApdater;
import com.example.medihealth.models.Schedule;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ScheduleTodayDetailActivity extends AppCompatActivity {

    private RecyclerView scheItemRecyclerView;
    private PrescriptionItemApdater apdater;
    private ImageView imgBack;
    private TextView txtPrescriptionName;
    private TextView txtNameDrugUser;
    private TextView txtSchedule;
    private Button btnConfirm;
    private Button btnCancel;

    private Schedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_schedule);

        initializeViews();

        schedule = (Schedule) getIntent().getSerializableExtra("schedule");

        if (schedule != null) {
            displayScheduleDetails();
            checkTime();
        } else {
            txtPrescriptionName.setText("Không có lịch");
        }

        setClickListeners();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Cập nhật Intent mới

        schedule = (Schedule) getIntent().getSerializableExtra("schedule");

        if (schedule != null) {
            displayScheduleDetails();
            checkTime();
        } else {
            txtPrescriptionName.setText("Không có lịch");
        }
    }

    private void initializeViews() {
        txtPrescriptionName = findViewById(R.id.txtPrescriptionName);
        txtNameDrugUser = findViewById(R.id.txtNameDrugUser);
        txtSchedule = findViewById(R.id.txtSchedule);
        btnConfirm = findViewById(R.id.button_confirm);
        btnCancel = findViewById(R.id.button_cancel);
        imgBack = findViewById(R.id.image_back);
    }

    private void displayScheduleDetails() {
        txtPrescriptionName.setText(schedule.getPrescription().getTitle());
        txtNameDrugUser.setText("Người uống: " + schedule.getPrescription().getDrugUser().getName());
        LocalTime time = schedule.getTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        txtSchedule.setText("Thời gian: " + time.format(formatter));
        scheItemRecyclerView = findViewById(R.id.scheItemRecyclerView);
        scheItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        apdater = new PrescriptionItemApdater(schedule.getPrescription().getPrescriptionItems());
        scheItemRecyclerView.setAdapter(apdater);
    }

    private void checkTime() {
        LocalTime currentTime = LocalTime.now();
        if (currentTime.isAfter(schedule.getTime())) {
            btnConfirm.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
        } else {
            btnConfirm.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
        }
    }

    private void setClickListeners() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScheduleTodayDetailActivity.this, ScheduleTodayActivity.class));
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScheduleTodayDetailActivity.this, ConfirmScheduleActivity.class);
                intent.putExtra("schedule", schedule);
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScheduleTodayDetailActivity.this, SkipScheduleActivity.class);
                intent.putExtra("schedule", schedule);
                startActivity(intent);
            }
        });
    }
}
