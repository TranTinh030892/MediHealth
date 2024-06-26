package com.example.medihealth.services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.medihealth.R;
import com.example.medihealth.activities.prescription_schedule.SyncService;

import java.util.Timer;
import java.util.TimerTask;

public class RemindService extends Service {

    private static final String TAG = RemindService.class.getSimpleName();
    private static final int ID = 114;
    public static final String CHANNEL_ID = "4953";
    Timer timer;
    private final long INTERVAL = 8 * 60 * 60 * 1000; // update sau mỗi 8h

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("MediHealth")
                .setContentText("Dịch vụ nhắc lịch uống thuốc đang hoạt động")
                .setSmallIcon(R.drawable.ic_notification_active_20)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true);

        startForeground(ID, builder.build());
        Log.v(TAG, "Remind Service started...");

        doSync();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "RemindService",
                NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Chanel for remind service");
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        Log.e(TAG, "Created Remind Notification chanel");
    }

    private void doSync() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SyncService.sync(RemindService.this);
            }
        }, 0, INTERVAL);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}
