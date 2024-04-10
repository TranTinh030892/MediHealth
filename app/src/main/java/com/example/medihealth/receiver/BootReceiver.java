package com.example.medihealth.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.medihealth.services.RemindService;

import java.util.Objects;

public class BootReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeIntentLaunch")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)) {
            Log.i("BootReceiver", "Restart remind service...");
            Intent serviceIntent = new Intent(context, RemindService.class);
            context.startService(serviceIntent);
        }
    }
}
