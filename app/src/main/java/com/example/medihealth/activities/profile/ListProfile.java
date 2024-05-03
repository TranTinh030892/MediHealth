package com.example.medihealth.activities.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medihealth.R;
import com.example.medihealth.adapters.profile.RelativeAdapter;
import com.example.medihealth.models.CustomToast;
import com.example.medihealth.models.Relative;
import com.example.medihealth.models.UserModel;
import com.example.medihealth.utils.AndroidUtil;
import com.example.medihealth.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class ListProfile extends AppCompatActivity implements View.OnClickListener{
    SharedPreferences sharedPreferences;
    ImageButton btnBack;
    ImageView btnAdd,accountImage;
    CardView account;
    TextView accountFullname,accountBirth,accountPhoneNumber;
    RecyclerView listRelative;
    RelativeAdapter relativeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_profile);
        sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
        initView();
        setOnclick();
        setupAccountImage();
        getInforAccount();
        getListRelative();
    }

    private void initView() {
        btnBack = findViewById(R.id.back_btn);
        btnAdd = findViewById(R.id.btn_add);
        account = findViewById(R.id.user);
        accountImage = findViewById(R.id.image_Account);
        accountFullname = findViewById(R.id.fullName);
        accountBirth = findViewById(R.id.birth);
        accountPhoneNumber = findViewById(R.id.phoneNumber);
        listRelative = findViewById(R.id.list_profile);
    }
    private void setOnclick() {
        btnBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        account.setOnClickListener(this);
    }
    private void setupAccountImage() {
        String profileString = sharedPreferences.getString("profile", "empty");
        if (!profileString.equals("empty")){
            String[] array = profileString.split(";");
            if (array.length > 0) {
                Log.e("CHECK",array[1]);
                Picasso.get().load(array[1]).into(accountImage);
            } else {
                Log.e("ERROR", "profileString rỗng");
            }
        }
    }
    private void getInforAccount() {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                UserModel userModel = task.getResult().toObject(UserModel.class);
                setupViewAccount(userModel);
            }
            else {
                Log.e("ERROR", "Lỗi kết nối");
            }
        });
    }

    private void setupViewAccount(UserModel userModel) {
        accountFullname.setText(userModel.getFullName());
        accountBirth.setText("Ngày sinh: "+userModel.getBirth());
        accountPhoneNumber.setText("Điện thoại: "+userModel.getPhoneNumber());
    }
    private void getListRelative() {
        String currentUserId = FirebaseUtil.currentUserId();
        Query query = FirebaseUtil.getRelativeCollectionReference()
                .whereEqualTo("userId",currentUserId);
        FirestoreRecyclerOptions<Relative> options = new FirestoreRecyclerOptions.Builder<Relative>()
                .setQuery(query, Relative.class).build();
        setupRecyclerView(options);
    }

    private void setupRecyclerView(FirestoreRecyclerOptions<Relative> options) {
        relativeAdapter = new RelativeAdapter(options, getApplicationContext(), new RelativeAdapter.IRelativeViewHolder() {
            @Override
            public void onClickItem(int positon) {
                Relative relative = relativeAdapter.getItem(positon);
                Intent intent = new Intent(ListProfile.this, EditProfileRelative.class);
                AndroidUtil.passRelativeModelAsIntent(intent,relative);
                startActivity(intent);
            }

            @Override
            public void onDataLoaded(int size) {

            }
        });
        listRelative.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        listRelative.setAdapter(relativeAdapter);
        relativeAdapter.startListening();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.user){
            Intent intent = new Intent(this, EditProfileUser.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.back_btn){
            finish();
        }
        if (v.getId() == R.id.btn_add){
            Intent intent = new Intent(this, AddRelative.class);
            startActivity(intent);
        }
    }
}