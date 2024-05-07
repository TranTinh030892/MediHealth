package com.example.medihealth.activities.show_schedule_totay;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medihealth.R;
import com.example.medihealth.apiservices.ConfirmNotificationService;
import com.example.medihealth.models.Schedule;
import com.example.medihealth.retrofitcustom.RetrofitClient;
import com.example.medihealth.utils.show_schedule_today.ResponseMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SkipScheduleActivity extends AppCompatActivity {
    private Schedule schedule;
    private EditText editTextCause;
    private Button btnSend;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_cause);

        schedule = (Schedule) getIntent().getSerializableExtra("schedule");
        editTextCause = findViewById(R.id.editTextCause);
        btnSend = findViewById(R.id.button_send);
        imgBack = findViewById(R.id.image_back);

        if (schedule != null) {
            editTextCause.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().trim().length() > 0) {
                        btnSend.setBackgroundResource(R.drawable.button_background_active);
                        btnSend.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        btnSend.setBackgroundResource(R.drawable.button_background_notactive);
                        btnSend.setTextColor(getResources().getColor(R.color.black));
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String cause = editTextCause.getText().toString().trim();

                    if (!cause.isEmpty()) {
                        ConfirmNotificationService confirmNotificationService = RetrofitClient.createService(ConfirmNotificationService.class);
                        confirmNotificationService.saveConfirmNotification_Cancel(schedule.getId(), schedule.getTime(), cause).enqueue(new Callback<ResponseMessage>() {
                            @Override
                            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(SkipScheduleActivity.this, "Đã xác nhận", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SkipScheduleActivity.this, ScheduleTodayActivity.class));
                                } else {
                                    Toast.makeText(SkipScheduleActivity.this, "Lỗi xác nhận", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                                Toast.makeText(SkipScheduleActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(SkipScheduleActivity.this, "Hãy nhập lý do để gửi", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SkipScheduleActivity.this, ScheduleTodayDetailActivity.class);
                    intent.putExtra("schedule", schedule);
                    startActivity(intent);
                }
            });
        }
    }
}


