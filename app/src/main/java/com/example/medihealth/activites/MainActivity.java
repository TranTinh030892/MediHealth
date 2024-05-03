package com.example.medihealth.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medihealth.R;
import com.example.medihealth.fragments.CustomerFragment.Appointment_Fragment;
import com.example.medihealth.fragments.CustomerFragment.Home_Fragment;
import com.example.medihealth.fragments.CustomerFragment.Menu_Fragment;
import com.example.medihealth.fragments.CustomerFragment.Notification_Fragment;
import com.example.medihealth.fragments.CustomerFragment.Profile_Fragment;
import com.example.medihealth.models.CustomToast;
import com.example.medihealth.trang.login.activity.Login;
import com.example.medihealth.utils.AndroidUtil;
import com.example.medihealth.utils.FirebaseUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Dialog dialog;
    SharedPreferences sharedPreferences;
    private BottomNavigationView bottomNavigationView, notificationBG;
    TextView notificationNumber;
    private FrameLayout pageLayout;
    private Button btnHome;
    private boolean isFirstBackPress = true;
    private final Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
        setupIndexSelectedDoctor("indexDoctorSelected",0);
        setupIndexSelectedDoctor("indexTimeSelected",-1);
        initView();
        setOnclick();
        showNotificationBg();
        showResultConnectNetwork();
        setLoadingFormUserSharedPreferences();
        setBotomNavigation();
        getIntentFromConfirmAppointment();
    }
    @Override
    public void onBackPressed() {
        if (isFirstBackPress) {
            CustomToast.showToast(getApplicationContext(),"Bấm Back lần nữa để thoát",Toast.LENGTH_SHORT);
            isFirstBackPress = false;
            handler.postDelayed(resetFirstBackPress, 2000);
        } else {
            handler.removeCallbacks(resetFirstBackPress);
            super.onBackPressed();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
    private final Runnable resetFirstBackPress = new Runnable() {
        @Override
        public void run() {
            isFirstBackPress = true;
        }
    };
    private void setLoadingFormUserSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("loadingFormUser", 1102);
        editor.apply();
    }

    private void initView() {
        bottomNavigationView = findViewById(R.id.menuBottom);
        notificationBG = findViewById(R.id.notification_bg);
        notificationNumber = findViewById(R.id.notice_Number);
        btnHome = findViewById(R.id.btnHome);
        pageLayout = findViewById(R.id.page_home);
    }
    private void setOnclick() {
        btnHome.setOnClickListener(this);
    }
    private void showNotificationBg() {
        String currentUserId = FirebaseUtil.currentUserId();
        FirebaseUtil.getNotificationsCollectionReference()
                .whereEqualTo("seen", false)
                .whereEqualTo("userId",currentUserId)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Log.e("TotalCount", "Error listening for changes: ", error);
                        return;
                    }

                    if (querySnapshot != null) {
                        int totalCount = querySnapshot.size();
                        if (totalCount > 0) {
                            notificationBG.setVisibility(View.VISIBLE);
                            notificationNumber.setText(String.valueOf(totalCount));
                        }
                        else {
                            notificationBG.setVisibility(View.GONE);
                        }
                    }
                    else {
                        Log.e("TotalCount", "QuerySnapshot is null");
                    }
                });
    }
    private void setBotomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if(item.getItemId() == R.id.item_user){
                    if (FirebaseUtil.currentUserId() == null){
                        showDialogLogin(Gravity.CENTER);
                        return  false;
                    }
                    selectedFragment = new Profile_Fragment();
                }
                if(item.getItemId() == R.id.item_appointment){
                    if (FirebaseUtil.currentUserId() == null){
                        showDialogLogin(Gravity.CENTER);
                        return  false;
                    }
                    selectedFragment = new Appointment_Fragment();
                }
                if(item.getItemId() == R.id.item_home){
                    selectedFragment = new Home_Fragment();
                }
                if(item.getItemId() == R.id.item_notification){
                    if (FirebaseUtil.currentUserId() == null){
                        showDialogLogin(Gravity.CENTER);
                        return  false;
                    }
                    selectedFragment = new Notification_Fragment();
                }
                if(item.getItemId() == R.id.item_menu_bar){
                    selectedFragment = new Menu_Fragment();
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.page_home, selectedFragment).commit();
                }
                return true;
            }
        });

        // Chọn Fragment mặc định khi mở ứng dụng
        bottomNavigationView.setSelectedItemId(R.id.item_home);
    }
    private void showResultConnectNetwork(){
        if (!AndroidUtil.isInternetAvailable(getApplicationContext())){
            CustomToast.showToast(getApplicationContext(),"Không có kết nối mạng", Toast.LENGTH_LONG);
        }
    }
    private void showDialogLogin(int center) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_notice_login_logout);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = center;
        window.setAttributes(windowAttributes);
        if (Gravity.BOTTOM == center){
            dialog.setCancelable(false);
        }
        else{
            dialog.setCancelable(true);
        }
        RelativeLayout btnCancel, btnLogin;
        btnCancel = dialog.findViewById(R.id.cancel);
        btnLogin = dialog.findViewById(R.id.agree);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnHome){
            Fragment mainFragment = new Home_Fragment();
            bottomNavigationView.setSelectedItemId(R.id.item_home);
            getSupportFragmentManager().beginTransaction().replace(R.id.page_home, mainFragment).commit();
        }
    }
    private void getIntentFromConfirmAppointment() {
        Intent intent = getIntent();
        if (intent != null){
            int requestCode = intent.getIntExtra("requestCode",1);
            if (requestCode == 113){
                Fragment fragmentSelected = new Notification_Fragment();
                bottomNavigationView.setSelectedItemId(R.id.item_notification);
                getSupportFragmentManager().beginTransaction().replace(R.id.page_home, fragmentSelected).commit();
            }
            else if (requestCode == 131){
                Fragment fragmentSelected = new Appointment_Fragment();
                bottomNavigationView.setSelectedItemId(R.id.item_appointment);
                getSupportFragmentManager().beginTransaction().replace(R.id.page_home, fragmentSelected).commit();
            }
        }
    }
    private void setupIndexSelectedDoctor(String key, int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }
}