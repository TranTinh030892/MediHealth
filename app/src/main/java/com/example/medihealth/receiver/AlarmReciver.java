package com.example.medihealth.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.medihealth.models.Token;
import com.example.medihealth.utils.FirebaseUtil;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.List;

public class AlarmReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("reminder")) {
            String reminderDate = intent.getStringExtra("reminderDate");
            String detail = intent.getStringExtra("detailAppointment");
            if (reminderDate.equals(getCurrentDate())) {
                String[] str = detail.trim().split(";");
                sendRemindertoCustomerTokenId(str[2], str[0], str[1]);
            }
        }
    }

    private String getCurrentDate() {
        final Calendar calendar = Calendar.getInstance();
        int year, month, day;
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String monthStr = String.valueOf(month), dayStr = String.valueOf(day);

        if (month + 1 < 10) monthStr = "0" + (month + 1);
        if (day < 10) dayStr = "0" + day;
        String result = dayStr + "/" + monthStr + "/" + year;
        return result;
    }

    private void sendRemindertoCustomerTokenId(String userId, String time, String date) {
        Query query = FirebaseUtil.getTokenId().whereEqualTo("userId", userId);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    Token token = documentSnapshot.toObject(Token.class);
                    if (token != null) {
                        List<String> tokenList = token.getTokenList();
                        for (String tokenString : tokenList) {
                            String title = "Nhắc lịch hẹn khám";
                            String body = "Quý khách có một lịch hẹn khám tại medihelth vào lúc " + time + " ngày " + date;
                            FirebaseUtil.sendMessageNotificationToCustomerTokenId("customer", tokenString, title, body);
                        }
                    }
                }
            } else {
                Log.e("ERROR", "Lỗi kết nối");
            }
        });
    }
}
