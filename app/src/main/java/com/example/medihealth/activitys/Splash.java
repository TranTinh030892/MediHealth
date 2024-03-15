package com.example.medihealth.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.medihealth.R;
import com.example.medihealth.activitys.chat.Employee_MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Splash extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String currentUserId = user.getUid();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getAllUserIdAndCheck(currentUserId);
                }
            },2000);
        } else {
            Intent intent = new Intent(Splash.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
    private void getAllUserIdAndCheck(String currentUserId){
        FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                List<String> listAllUserId = new ArrayList<>();
                QuerySnapshot querySnapshot = task.getResult();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    listAllUserId.add(document.getId());
                }
                checkUserOrEmployee(currentUserId, listAllUserId);
            } else {
                Log.e("ERROR","ERROR");
            }
        });
    }
    private void checkUserOrEmployee(String currentUserId, List<String> listAllUserId){
        Intent intent;
        if (listAllUserId.contains(currentUserId)) {
            intent = new Intent(Splash.this, MainActivity.class);
        } else {
            intent = new Intent(Splash.this, Employee_MainActivity.class);
        }
        intent.putExtra("requestCodeLoadingFormUser", 1102);
        startActivity(intent);
        finish();
    }
}