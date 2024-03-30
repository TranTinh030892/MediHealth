package com.example.medihealth.activitys.prescription_schedule;

import android.content.Context;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import java.util.concurrent.TimeUnit;

public class ScheduleManager {

    public static void scheduleWork(Context context) {
        // Tạo WorkRequest
        PeriodicWorkRequest.Builder workRequestBuilder =
                new PeriodicWorkRequest.Builder(AlarmWorker.class, 1, TimeUnit.MINUTES);

        // Lên lịch công việc
        PeriodicWorkRequest workRequest = workRequestBuilder.build();
        WorkManager.getInstance(context).enqueue(workRequest);
    }
}
