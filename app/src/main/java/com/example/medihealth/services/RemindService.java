package com.example.medihealth.services;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.medihealth.R;
import com.example.medihealth.activitys.prescription_schedule.SyncService;

import java.util.Timer;
import java.util.TimerTask;

public class RemindService extends Service {

    private static final String TAG = RemindService.class.getSimpleName();
    private static final int ID = 114;
    public static final String CHANNEL_ID = "4953";
    Timer timer;
    private final long INTERVAL = 60000;

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
                .setContentTitle("Dịch vụ nhắc lịch uống thuốc đang hoạt động")
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setAutoCancel(false);
        startForeground(ID, builder.build());

        doSync();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    private void createNotificationChannel() {
        Log.e(CHANNEL_ID, "Created Remind Notification chanel");
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system. You can't change the importance
        // or other notification behaviors after this.
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void doSync() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SyncService.sync(RemindService.this);
                Log.d(TAG, "Calling API...");
                // ApiCaller.callApi();
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
