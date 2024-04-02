package com.example.medihealth.activitys.prescription_schedule;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class AlarmWorker extends Worker {

    private static final String TAG = "113";

    public AlarmWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e("Worker", "START");
        SyncService.sync(this.getApplicationContext());
        return Result.success();
    }
}
