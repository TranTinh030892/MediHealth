package com.example.medihealth.receiver;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.medihealth.R;
import com.example.medihealth.activitys.prescription_schedule.PrescriptionDetailManagement;
import com.example.medihealth.activitys.prescription_schedule.RemindScheduler;
import com.example.medihealth.models.Schedule;
import com.example.medihealth.services.RemindService;

public class RemindReceiver extends BroadcastReceiver {

    private final static String TAG = "REMIND_RECEIVER";
    private static final int CLICK_CODE = 0;
    private static final int SNOOZE_CODE = 1;

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {

        Schedule schedule = (Schedule) intent.getSerializableExtra("schedule");
        Intent notificationIntent = new Intent(context, PrescriptionDetailManagement.class);
        notificationIntent.putExtra("prescription", schedule.getPrescription());
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
                .setWhen(0)
                .setAutoCancel(true);

        // Hiển thị thông báo
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(Math.toIntExact(schedule.getId()), builder.build());
        Log.e(TAG, String.format("Notification: {schedule_id: %d, time: %TR}", schedule.getId(), schedule.getTime()));
    }
}
