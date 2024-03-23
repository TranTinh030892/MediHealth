package com.example.medihealth.activitys.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.medihealth.R;
import com.example.medihealth.fragments.EmployeeFragment.Employee_Appointment_Fragment;
import com.example.medihealth.fragments.EmployeeFragment.Employee_Chat_Fragment;
import com.example.medihealth.fragments.EmployeeFragment.Employee_Menu_Fragment;
import com.example.medihealth.utils.FirebaseUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Employee_MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private BottomNavigationView bottomNavigationView, noticeBg;
    TextView noticeNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main);
        initView();
        setLoadingFormUserSharedPreferences();
        setBotomNavigation();
        showNoticeBg();
    }
    private void setLoadingFormUserSharedPreferences() {
        sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("loadingFormUser", 1102);
        editor.apply();
    }

    private void initView() {
        bottomNavigationView = findViewById(R.id.menuBottom);
        noticeBg = findViewById(R.id.notice);
        noticeNumber = findViewById(R.id.notice_Number);
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

        bottomNavigationView.setSelectedItemId(R.id.item_chat);
    }
    private void showNoticeBg() {
        FirebaseUtil.getAppointmentCollectionReference()
                .whereEqualTo("stateAppointment", 0)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Log.e("TotalCount", "Error listening for changes: ", error);
                        return;
                    }

                    if (querySnapshot != null) {
                        int totalCount = querySnapshot.size();
                        if (totalCount > 0) {
                            noticeBg.setVisibility(View.VISIBLE);
                            noticeNumber.setText(String.valueOf(totalCount));
                        }
                        else {
                            noticeBg.setVisibility(View.GONE);
                        }
                    }
                    else {
                        Log.e("TotalCount", "QuerySnapshot is null");
                    }
                });
    }
}