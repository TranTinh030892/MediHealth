package com.example.medihealth.activitys.prescription_schedule;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.medihealth.models.Schedule;
import com.example.medihealth.receiver.RemindReceiver;

import java.time.LocalTime;
import java.util.Calendar;

public class RemindScheduler {

    public final static int SNOOZE_TIME = 10;

    public static void scheduleRemind(Context context, Schedule schedule) {
        LocalTime time = schedule.getTime();
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, time.getHour());
        calendar.set(Calendar.MINUTE, time.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long timeToPushNotification = calendar.getTimeInMillis() - now.getTimeInMillis();
        if (timeToPushNotification >= 0) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(context, RemindReceiver.class);
            intent.putExtra("schedule", schedule);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    Math.toIntExact(schedule.getId()),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
            );
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Log.e("REMIND_SCHEDULE", String.format("Created, notification of schedule_id: %d will show in : %dms", schedule.getId(), timeToPushNotification));
        } else {
            Log.e("REMIND_SCHEDULE", String.format("schedule_id: %d, timeout: %d", schedule.getId(), timeToPushNotification));
        }
    }

    public static void cancelRemind(Context context, Schedule schedule) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, RemindReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                Math.toIntExact(schedule.getId()),
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );
        alarmManager.cancel(pendingIntent);
        Log.i("REMIND_SCHEDULE", String.format("Canceled schedule_id: %d", schedule.getId()));
    }

    public static void snoozeRemind(Context context, Schedule schedule) {
        LocalTime time = schedule.getTime();
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, LocalTime.now().getMinute() + SNOOZE_TIME);
        long timeToPushNotification = calendar.getTimeInMillis() - now.getTimeInMillis();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, RemindReceiver.class);
        intent.putExtra("schedule", schedule);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                Math.toIntExact(schedule.getId()),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
        );
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Log.e("REMIND_SCHEDULE", String.format("Snoozed, notification of schedule_id: %d will show in : %dms", schedule.getId(), timeToPushNotification));
    }
}
