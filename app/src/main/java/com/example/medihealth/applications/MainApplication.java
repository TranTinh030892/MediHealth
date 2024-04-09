package com.example.medihealth.applications;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.medihealth.notifications.FCM_Notification.MyApplicationFCM;
import com.example.medihealth.services.RemindService;
import com.example.medihealth.utils.FirebaseUtil;

public class MainApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private Handler handler;

    private Runnable runLogout = new Runnable() {
        @Override
        public void run() {
            FirebaseUtil.setState(false);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplicationFCM.init(this);
        registerActivityLifecycleCallbacks(this);
        handler = new Handler(getMainLooper());
        if (!isServiceRunning(RemindService.class)) {
            doService();
        }
    }

    private void doService() {
        Intent intent = new Intent(this, RemindService.class);
        startService(intent);
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        handler.removeCallbacks(runLogout);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        handler.removeCallbacks(runLogout);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        handler.removeCallbacks(runLogout);
        FirebaseUtil.setState(true);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        handler.postDelayed(runLogout, 1000);
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
