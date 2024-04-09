package com.example.medihealth.activities.prescription_schedule;

import android.content.Context;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class ScheduleManager {

    public static void scheduleWork(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // Yêu cầu kết nối mạng
                .build();
        // Tạo WorkRequest
        PeriodicWorkRequest.Builder workRequestBuilder =
                new PeriodicWorkRequest.Builder(AlarmWorker.class, 5, TimeUnit.MINUTES)
                        .setConstraints(constraints);

        // Lên lịch công việc
        PeriodicWorkRequest workRequest = workRequestBuilder.build();
        WorkManager.getInstance(context).enqueue(workRequest);
    }
}
