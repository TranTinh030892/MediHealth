package com.example.medihealth.activities.show_schedule_totay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class ConfirmScheduleActivity extends AppCompatActivity {

    private Schedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_schedule);

        schedule = (Schedule) getIntent().getSerializableExtra("schedule");
        if (schedule != null) {
            ConfirmNotificationService confirmNotificationService = RetrofitClient.createService(ConfirmNotificationService.class);
            confirmNotificationService.saveConfirmNotification_Confirm(schedule.getId(), schedule.getTime()).enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ConfirmScheduleActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ConfirmScheduleActivity.this, ScheduleTodayActivity.class));
                    } else {
                        Toast.makeText(ConfirmScheduleActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ConfirmScheduleActivity.this, ScheduleTodayDetailActivity.class);
                        intent.putExtra("schedule", schedule);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {
                    Log.e("API_Error", "Failed to connect to API: " + t.getMessage(), t);
                    Toast.makeText(ConfirmScheduleActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConfirmScheduleActivity.this, ScheduleTodayDetailActivity.class);
                    intent.putExtra("schedule", schedule);
                    startActivity(intent);
                }
            });
        }
    }
}
