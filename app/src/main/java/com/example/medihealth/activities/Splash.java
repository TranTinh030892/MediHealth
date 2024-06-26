package com.example.medihealth.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medihealth.R;
import com.example.medihealth.activities.chat.Employee_MainActivity;
import com.example.medihealth.models.UserModel;
import com.example.medihealth.trang.login.activity.Login;
import com.example.medihealth.trang.profile.activity.Profile;
import com.example.medihealth.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class Splash extends AppCompatActivity {
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String email = user.getEmail();
                    if (email.contains("nhanvien")) {
                        checkRole(user.getUid());
                    } else checkProfile(user.getUid());
                }
            }, 2000);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    private void checkProfile(String userId) {
        Query query = FirebaseUtil.allUserCollectionReference()
                .whereEqualTo("userId", userId);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    if (snapshot != null) {
                        int size = snapshot.size();
                        if (size == 0) {
                            Intent intent = new Intent(Splash.this, Profile.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else checkRole(userId);
                    } else {
                        Log.e("ERROR", "Không có kết quả trả về");
                    }
                } else {
                    Log.d("TAG", "Lỗi khi lấy dữ liệu: ", task.getException());
                }
            }
        });
    }

    private void checkRole(String currentUserid) {
        FirebaseFirestore.getInstance().collection("role")
                .document(currentUserid)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("ERROR", "Listen failed.", e);
                        }
                        if (documentSnapshot != null && documentSnapshot.contains("isRole")) {
                            Number isRoleNumber = documentSnapshot.getLong("isRole");
                            if (isRoleNumber != null) {
                                int isRole = isRoleNumber.intValue();
                                Intent intent = null;
                                if (isRole == 3) {
                                    intent = new Intent(Splash.this, MainActivity.class);
                                    setSharedPreferencesDataUser();
                                } else if (isRole == 2) {
                                    intent = new Intent(Splash.this, Employee_MainActivity.class);
                                    intent.putExtra("requestCodeEmployee", 1103);
                                }
                                if (intent != null) {
                                    intent.putExtra("requestCodeLoadingFormUser", 1102);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        } else {
                            Log.e("ERROR", "failed");
                        }
                    }
                });
    }

    private void setSharedPreferencesDataUser() {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                UserModel currentUser = task.getResult().toObject(UserModel.class);
                if (currentUser != null) {
                    sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("inforFormUser", currentUser.getFullName() + ";" + currentUser.getGender() + " - " + currentUser.getBirth() + ";" +
                            String.valueOf(currentUser.getHeight()) + ";" + String.valueOf(currentUser.getWeight()) + ";" +
                            getBMI(currentUser.getHeight(), currentUser.getWeight()));
                    editor.apply();
                } else {
                    Log.e("ERROR", "currentUser is null");
                }
            } else {
                Log.e("ERROR", "task is not successful or result is null");
            }
        });
    }

    private String getBMI(int height, int weight) {
        double heightDouble = (double) height / 100;
        double BMI = (double) weight / (heightDouble * heightDouble);
        double roundedNumber = Math.round(BMI * 10) / 10.0;
        return String.valueOf(roundedNumber);
    }
}