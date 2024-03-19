package com.example.medihealth.models;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class NotificationDismissReceiver extends BroadcastReceiver {
    SharedPreferences sharedPreferences;
    @Override
    public void onReceive(Context context, Intent intent) {
        // Tắt thông báo
        sharedPreferences = context.getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("isCloseNotice","Yes");
        editor.apply();
        // Tắt thông báo
        int notificationId = intent.getIntExtra("notificationId", 0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }
}
