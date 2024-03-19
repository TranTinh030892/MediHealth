package com.example.medihealth.activitys.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.medihealth.R;
import com.example.medihealth.fragments.chat.Employee_Appointment_Fragment;
import com.example.medihealth.fragments.chat.Employee_Chat_Fragment;
import com.example.medihealth.fragments.chat.Employee_Menu_Fragment;
import com.example.medihealth.fragments.main.BookAppointment_Fragment;
import com.example.medihealth.fragments.main.Home_Fragment;
import com.example.medihealth.fragments.main.Menu_Fragment;
import com.example.medihealth.fragments.main.Notification_Fragment;
import com.example.medihealth.fragments.main.Profile_Fragment;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Employee_MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    GoogleSignInClient googleSignInClient;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout pageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main);
        initView();
        setLoadingFormUserSharedPreferences();
        setBotomNavigation();
    }
    private void setLoadingFormUserSharedPreferences() {
        sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("loadingFormUser", 1102);
        editor.apply();
    }

    private void initView() {
        bottomNavigationView = findViewById(R.id.menuBottom);
        pageLayout = findViewById(R.id.page_home);
    }
    private void setBotomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if(item.getItemId() == R.id.item_chat){
                    selectedFragment = new Employee_Chat_Fragment();
                }
                if(item.getItemId() == R.id.item_appointment){
                    selectedFragment = new Employee_Appointment_Fragment();
                }
                if(item.getItemId() == R.id.item_menu){
                    selectedFragment = new Employee_Menu_Fragment();
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.page_home, selectedFragment).commit();
                }
                return true;
            }
        });

        // Chọn Fragment mặc định khi mở ứng dụng
        bottomNavigationView.setSelectedItemId(R.id.item_chat);
    }
}