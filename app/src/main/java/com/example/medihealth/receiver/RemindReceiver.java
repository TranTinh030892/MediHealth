package com.example.medihealth.receiver;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.medihealth.R;
import com.example.medihealth.activities.prescription_schedule.PrescriptionDetailManagement;
import com.example.medihealth.activities.prescription_schedule.RemindScheduler;
import com.example.medihealth.activities.show_schedule_totay.ScheduleTodayDetailActivity;
import com.example.medihealth.apiservices.ScheduleService;
import com.example.medihealth.models.ResponseObject;
import com.example.medihealth.models.Schedule;
import com.example.medihealth.retrofitcustom.LocalDateAdapter;
import com.example.medihealth.retrofitcustom.LocalDateTimeAdapter;
import com.example.medihealth.retrofitcustom.LocalTimeAdapter;
import com.example.medihealth.retrofitcustom.RetrofitClient;
import com.example.medihealth.services.RemindService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemindReceiver extends BroadcastReceiver {

    private final static String TAG = "REMIND_RECEIVER";
    private static final int CLICK_CODE = 0;
    private static final int SNOOZE_CODE = 1;


    public interface MyCallback {
        void onResponse(Schedule schedule);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Long scheduleId = intent.getLongExtra("schedule_id", -1);
        if (scheduleId == -1) {
            Log.e(TAG, "schedule is null.");
            return;
        }
        getSchedule(scheduleId, schedule -> {
            boolean check = schedule.isActive()
                    && schedule.getPrescription().isActive()
                    && schedule.getPrescription().getDrugUser().isActive();
            Log.i(TAG, String.format("schedule_id: %d, check: %b", schedule.getId(), check));
            if (check) {
                createNotification(context, schedule);
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void createNotification(Context context, Schedule schedule) {
        Intent notificationIntent = new Intent(context, ScheduleTodayDetailActivity.class);
        notificationIntent.putExtra("schedule", schedule);
        PendingIntent contentIntent = PendingIntent.getActivity(
                context,
                CLICK_CODE,
                notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_MUTABLE
        );

        Intent snoozeIntent = new Intent(context, SnoozeRemindReceiver.class);
        snoozeIntent.putExtra("schedule", schedule);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(
                context,
                SNOOZE_CODE,
                snoozeIntent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_MUTABLE
        );

        NotificationCompat.Action snoozeAction = new NotificationCompat.Action.Builder(
                R.drawable.icon_alarm_2,
                String.format("Báo lại sau %d phút", RemindScheduler.SNOOZE_TIME),
                snoozePendingIntent
        ).build();

        // Tạo và cấu hình thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, RemindService.CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_alarm_2)
                .setContentTitle("Đến giờ uống thuốc")
                .setContentText(String.format(
                        "%s, có lịch dùng %s",
                        schedule.getPrescription().getDrugUser().getName(),
                        schedule.getPrescription().getTitle())
                )
                .addAction(snoozeAction)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true);

        // Hiển thị thông báo
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(Math.toIntExact(schedule.getId()), builder.build());
        Log.v(TAG, String.format("Notification: {schedule_id: %d, time: %TR}", schedule.getId(), schedule.getTime()));
    }

    private void getSchedule(Long id, MyCallback myCallback) {
        ScheduleService service = RetrofitClient.createService(ScheduleService.class);
        service.getById(id).enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                            .create();
                    Schedule schedule = gson.fromJson(
                            new Gson().toJson(response.body().getData()),
                            new TypeToken<Schedule>() {
                            }.getType()
                    );
                    myCallback.onResponse(schedule);
                } else {
                    Log.e(TAG, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e(TAG, Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}
