package com.example.medihealth.notifications.FCM_Notification;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MyApplicationFCM extends Application {
    public static final String CHANNEL_ID = "push_notification_id2";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressLint("ObsoleteSdkInt")
    public static void init(Application application) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID, "PushNotification2", NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = application.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }
}
