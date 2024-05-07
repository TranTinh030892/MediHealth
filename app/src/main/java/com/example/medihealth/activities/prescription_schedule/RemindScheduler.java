package com.example.medihealth.activities.prescription_schedule;

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
    private static final String TAG = "REMIND_SCHEDULER";
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

        // timeout -> set for the next day
        if (timeToPushNotification < 0) {
            calendar.add(Calendar.DATE, 1);
            timeToPushNotification = calendar.getTimeInMillis() - now.getTimeInMillis();
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, RemindReceiver.class);
        intent.putExtra("schedule_id", schedule.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                Math.toIntExact(schedule.getId()),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
        );
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Log.i(TAG, String.format("Created, notification of schedule_id: %d will show in: %dms", schedule.getId(), timeToPushNotification));
    }

    public static void cancelRemind(Context context, long scheduleId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, RemindReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                Math.toIntExact(scheduleId),
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );
        alarmManager.cancel(pendingIntent);
        Log.i(TAG, String.format("Canceled schedule_id: %d", scheduleId));
    }

    public static void snoozeRemind(Context context, long scheduleId) {
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, SNOOZE_TIME);
        long timeToPushNotification = calendar.getTimeInMillis() - now.getTimeInMillis();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, RemindReceiver.class);
        intent.putExtra("schedule_id", scheduleId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                Math.toIntExact(scheduleId),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
        );
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Log.v(TAG, String.format("Snoozed, notification of schedule_id: %d will show in :%dms", scheduleId, timeToPushNotification));
    }
}
