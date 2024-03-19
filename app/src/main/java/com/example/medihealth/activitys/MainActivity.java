package com.example.medihealth.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.medihealth.R;
import com.example.medihealth.fragments.main.BookAppointment_Fragment;
import com.example.medihealth.fragments.main.Home_Fragment;
import com.example.medihealth.fragments.main.Menu_Fragment;
import com.example.medihealth.fragments.main.Notification_Fragment;
import com.example.medihealth.fragments.main.Profile_Fragment;
import com.example.medihealth.models.CustomToast;
import com.example.medihealth.utils.AndroidUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    GoogleSignInClient googleSignInClient;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout pageLayout;
    private Button btnHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        showResultConnectNetwork();
        setLoadingFormUserSharedPreferences();
        setBotomNavigation();
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mainFragment = new Home_Fragment();
                bottomNavigationView.setSelectedItemId(R.id.item_home);
                getSupportFragmentManager().beginTransaction().replace(R.id.page_home, mainFragment).commit();
            }
        });
    }

    private void setLoadingFormUserSharedPreferences() {
        sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("loadingFormUser", 1102);
        editor.apply();
    }

    private void initView() {
        bottomNavigationView = findViewById(R.id.menuBottom);
        btnHome = findViewById(R.id.btnHome);
        pageLayout = findViewById(R.id.page_home);
    }
    private void setBotomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if(item.getItemId() == R.id.item_user){
                    selectedFragment = new Profile_Fragment();
                }
                if(item.getItemId() == R.id.item_appointment){
                    selectedFragment = new BookAppointment_Fragment();
                }
                if(item.getItemId() == R.id.item_home){
                    selectedFragment = new Home_Fragment();
                }
                if(item.getItemId() == R.id.item_notification){
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
}