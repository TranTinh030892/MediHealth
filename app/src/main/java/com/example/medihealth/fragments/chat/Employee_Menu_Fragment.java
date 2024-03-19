package com.example.medihealth.fragments.chat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medihealth.R;
import com.example.medihealth.activitys.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Employee_Menu_Fragment extends Fragment implements View.OnClickListener {
    GoogleSignInClient googleSignInClient;
    Dialog dialog, dialogSwitch;
    SharedPreferences sharedPreferences;
    RelativeLayout btnSetNotice,btnLogout;
    ImageView iconMenu;
    TextView titleMenu, fullNameEmployee, birthUser;
    public Employee_Menu_Fragment() {
        // Required empty public constructor
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View iteamView = inflater.inflate(R.layout.fragment_menu, container, false);
        dialog = new Dialog(getContext());
        sharedPreferences = getContext().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
        initView(iteamView);
        setInforEmployee();
        setOnclick();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Khởi tạo GoogleSignInClient
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        return iteamView;
    }

    private void setInforEmployee() {
        String inforFormEmployee = sharedPreferences.getString("inforFormEmployee", "empty");
        if (!inforFormEmployee.equals("empty")){
            String[] array = inforFormEmployee.split(";");
            if (array.length > 0) {
                fullNameEmployee.setText(array[0]);
                birthUser.setVisibility(View.GONE);
            } else {
                Log.e("ERROR", "profileString rỗng");
            }
        }
    }

    private void initView(View iteamView) {
        fullNameEmployee = iteamView.findViewById(R.id.fullName_user);
        birthUser = iteamView.findViewById(R.id.gender_birth);
        btnSetNotice = iteamView.findViewById(R.id.btn_menu4);
        btnLogout = iteamView.findViewById(R.id.btn_logout);
        iconMenu = iteamView.findViewById(R.id.icon_menu4);
        titleMenu = iteamView.findViewById(R.id.title_menu4);
        iconMenu.setBackgroundResource(R.drawable.notification);
        titleMenu.setText("Cài đặt thông báo");
    }

    private void setOnclick() {
        btnLogout.setOnClickListener(this);
        btnSetNotice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_logout){
            showDialogLoadingLogout(Gravity.CENTER);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sigout();
                }
            }, 2000);
        }
        if (v.getId() == R.id.btn_menu4){
            showDialogSwich(Gravity.CENTER);
        }
    }

    private void sigout() {
        removeAllSharedPreferences();
        FirebaseAuth.getInstance().signOut();
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void removeAllSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profile","empty");
        editor.putString("inforFormUser","empty");
        editor.apply();
    }

    private void showDialogLoadingLogout(int center) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_loading);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = center;
        window.setAttributes(windowAttributes);

        TextView textTitle;
        textTitle = dialog.findViewById(R.id.title);
        textTitle.setText("Đang đăng xuất...");

        dialog.show();
    }
    private void showDialogSwich(int center) {
        sharedPreferences = getContext().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        dialogSwitch = new Dialog(getActivity()); // Khởi tạo dialogSwitch
        dialogSwitch.requestWindowFeature(Window.FEATURE_NO_TITLE); // Đảm bảo gọi requestFeature() trước khi thêm nội dung
        dialogSwitch.setContentView(R.layout.custom_swich);
        Window window = dialogSwitch.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = center;
        window.setAttributes(windowAttributes);
        Switch switchNotice;
        switchNotice = dialogSwitch.findViewById(R.id.notificationSwitch);

        // cài đặt trạng thái switch theo isCloseNotice
        String isCloseNotice = sharedPreferences.getString("isCloseNotice", "No");
        if (isCloseNotice.equals("Yes")){
            switchNotice.setChecked(false);
        }
        else switchNotice.setChecked(true);

        // cài đặt isCloseNotice theo switch
        switchNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchNotice.isChecked()){
                    editor.putString("isCloseNotice","No");
                }
                else editor.putString("isCloseNotice","Yes");
                editor.apply();
            }
        });
        dialogSwitch.show();
    }
}