package com.example.medihealth.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.example.medihealth.activities.prescription_schedule.RemindScheduler;
import com.example.medihealth.models.Schedule;

public class SnoozeRemindReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long scheduleId = intent.getLongExtra("schedule_id", -1);
        if (scheduleId != -1) {
            RemindScheduler.snoozeRemind(context, scheduleId);
            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            manager.cancel((int) scheduleId);
            return;
        }
        Log.e("SnoozeRemindReceiver", "schedule_id is null");
    }
}
