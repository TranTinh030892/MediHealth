package com.example.medihealth.activitys.prescription_schedule;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Objects;

public class InstallReceiver extends BroadcastReceiver {
    private static final int JOB_ID = 1411;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(String.valueOf(JOB_ID), "check");
        if (Objects.equals(intent.getAction(), Intent.ACTION_MY_PACKAGE_REPLACED)) {
            ComponentName componentName = new ComponentName(context, SyncService.class);
            JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .setPeriodic(15 * 60 * 1000)
                    .build();
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(jobInfo);
        }
    }
}
