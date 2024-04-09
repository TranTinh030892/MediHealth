package com.example.medihealth.fragments.CustomerFragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.adapters.appointment.NotificationAdapter;
import com.example.medihealth.models.NotificationModel;
import com.example.medihealth.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class Notification_Fragment extends Fragment {
    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    public Notification_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView =  inflater.inflate(R.layout.fragment_notification, container, false);
        initView(itemView);
        setupNotificationsRecyclerView(FirebaseUtil.currentUserId());
        return itemView;
    }

    private void initView(View itemView) {
        recyclerView = itemView.findViewById(R.id.listNotifications);
    }
    private void setupNotificationsRecyclerView(String currentId) {
        Query query = FirebaseUtil.getNotificationsCollectionReference()
                .whereEqualTo("userId",currentId);
        FirestoreRecyclerOptions<NotificationModel> options = new FirestoreRecyclerOptions.Builder<NotificationModel>()
                .setQuery(query,NotificationModel.class).build();
        notificationAdapter = new NotificationAdapter(options, getContext(), new NotificationAdapter.INotificationViewHolder() {
            @Override
            public void onClickItem(int positon, DocumentSnapshot documentSnapshot) {
                showDialogInforNotification(Gravity.CENTER,positon,documentSnapshot);
                updateStateSeenNotification(positon,documentSnapshot);
            }

            @Override
            public void onDataLoaded(int size) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(notificationAdapter);
        notificationAdapter.startListening();
    }
    private void showDialogInforNotification(int center,int positon, DocumentSnapshot documentSnapshot) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_detail_notification);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = center;
        window.setAttributes(windowAttributes);
        if (Gravity.BOTTOM == center){
            dialog.setCancelable(false);
        }
        else{
            dialog.setCancelable(true);
        };

        ImageView closeDialog;
        TextView textBody;
        closeDialog = dialog.findViewById(R.id.icon_close);
        textBody = dialog.findViewById(R.id.notification_body);
        setupTextBody(textBody,positon);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setupTextBody(TextView textBody, int positon) {
        if (notificationAdapter != null){
            NotificationModel notificationModel = notificationAdapter.getItem(positon);
            String content = notificationModel.getBody();
            textBody.setText(content);
        }
    }

    private void updateStateSeenNotification(int positon, DocumentSnapshot documentSnapshot) {
        NotificationModel notificationModel = notificationAdapter.getItem(positon);
        notificationModel.setSeen(true);
        FirebaseUtil.getNotificationDetailsById(documentSnapshot.getId()).set(notificationModel)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.e("SUCCESSFULL","Đã xem một notification");
                    }
                    else {
                        Log.e("ERROR","Lỗi kết nối mạng");
                    }
                });
    }
}