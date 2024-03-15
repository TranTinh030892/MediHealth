package com.example.medihealth.fragments.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.medihealth.R;
import com.example.medihealth.activitys.Login;
import com.example.medihealth.activitys.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu_Fragment extends Fragment implements View.OnClickListener {
    Dialog dialog;
    SharedPreferences sharedPreferences;
    GoogleSignInClient googleSignInClient;
    RelativeLayout btnLogout;
    public Menu_Fragment() {
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
        View itemView =  inflater.inflate(R.layout.fragment_menu, container, false);
        dialog = new Dialog(getContext());
        sharedPreferences = getContext().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Khởi tạo GoogleSignInClient
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        initView(itemView);
        setOnclick();
        return itemView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
    }

    @Override
    public void onStop() {
        super.onStop();
        dialog.dismiss();
    }

    private void initView(View itemView) {
        btnLogout = itemView.findViewById(R.id.btn_logout);
        setbtnLogout();
    }

    private void setbtnLogout() {
        FirebaseUser curentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (curentUser == null){
            btnLogout.setVisibility(View.GONE);
        }
    }

    private void setOnclick() {
        btnLogout.setOnClickListener(this);
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

    private void sigout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profile","empty");
        editor.apply();
        FirebaseAuth.getInstance().signOut();
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

}