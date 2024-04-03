package com.example.medihealth.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

import com.example.medihealth.activitys.prescription_schedule.RemindScheduler;
import com.example.medihealth.models.Schedule;

public class SnoozeRemindReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Schedule schedule = (Schedule) intent.getSerializableExtra("schedule");
        if (schedule != null) {
            RemindScheduler.snoozeRemind(context, schedule);
            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            manager.cancel(Math.toIntExact(schedule.getId()));
        }
    }
}
