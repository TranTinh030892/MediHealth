package com.example.medihealth.notifications.FCM_Notification;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.medihealth.R;
import com.example.medihealth.activitys.chat.EmployeeChat_Activity;
import com.example.medihealth.models.UserModel;
import com.example.medihealth.utils.AndroidUtil;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static int notificationId = 1;
    private UserModel userModel;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // Xử lý dữ liệu từ thông báo FCM
        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            String userId = remoteMessage.getData().get("userId");
            if(userId != null){
                FirebaseFirestore.getInstance().collection("users").document(userId).get()
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                userModel = task.getResult().toObject(UserModel.class);
                                showNotificationChat(title, body,userModel);
                            }
                        });
            }
            else{
                // Hien thi thong bao tu he thong
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private void showNotificationChat(String title, String body,UserModel user) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(MyApplicationFCM.CHANNEL_ID, "Channel_Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, EmployeeChat_Activity.class);
        AndroidUtil.passUserModelAsIntent(intent,user);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Tăng ID thông báo cho mỗi thông báo mới
        notificationId++;


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, MyApplicationFCM.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.notification)
                .setColor(ContextCompat.getColor(this, R.color.Red))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true); // Đảm bảo thông báo sẽ tự động biến mất sau khi click

        // Sử dụng ID thông báo duy nhất cho mỗi thông báo
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

}
