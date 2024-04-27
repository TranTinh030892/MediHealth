package com.example.medihealth.activitys.profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medihealth.R;
import com.example.medihealth.activitys.Login;
import com.example.medihealth.activitys.MainActivity;
import com.example.medihealth.models.CustomToast;
import com.example.medihealth.models.UserModel;
import com.example.medihealth.utils.FirebaseUtil;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {
    ImageButton btnBack,btnEdit;
    EditText fullName, phoneNumber, address, height, weight;
    RadioButton  male, female;
    TextView birth;
    CardView bottomLayout;
    RelativeLayout btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_user);
        initView();
        setOnlick();
        setupUserDetail();
    }

    private void initView() {
        btnBack = findViewById(R.id.back_btn);
        btnEdit = findViewById(R.id.btn_edit);
        fullName = findViewById(R.id.fullName_user);
        phoneNumber = findViewById(R.id.phoneNumber_user);
        address = findViewById(R.id.address_user);
        birth = findViewById(R.id.birth_user);
        height = findViewById(R.id.height_user);
        weight = findViewById(R.id.weight_user);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        bottomLayout = findViewById(R.id.bottom_Layout);
        btnSave = findViewById(R.id.enterBook);
    }
    private void setOnlick() {
        btnBack.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        birth.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }
    private void setupUserDetail() {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                UserModel userModel = task.getResult().toObject(UserModel.class);
                if (userModel != null){
                    setupViewDetail(userModel);
                }
            }
            else {
                Log.e("ERROR","Lỗi kết nối");
            }
        });
    }

    private void setupViewDetail(UserModel userModel) {
        fullName.setText(userModel.getFullName());
        phoneNumber.setText(userModel.getPhoneNumber());
        address.setText(userModel.getAddress());
        birth.setText(userModel.getBirth());
        height.setText(String.valueOf(userModel.getHeight()));
        weight.setText(String.valueOf(userModel.getWeight()));
        if (userModel.getGender().equals("Nam")){
            male.setChecked(true);
        }
        if (userModel.getGender().equals("Nữ")){
            female.setChecked(true);
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_btn){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.btn_edit){
            setupView(true);
        }
        if (v.getId() == R.id.enterBook){
            saveUserDetail();
            setupView(false);
        }
        if (v.getId() == R.id.birth_user){
            showDialogCalendar(Gravity.CENTER);
        }
    }

    private void showDialogCalendar(int center) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_datepicker);
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
        DatePicker datePicker;
        RelativeLayout btnCancel, btnSelect;
        datePicker = dialog.findViewById(R.id.datePicker);
        btnCancel = dialog.findViewById(R.id.cancel);
        btnSelect = dialog.findViewById(R.id.select);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBirthDay(datePicker);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void getBirthDay(DatePicker datePicker) {
        int dayOfMonth = datePicker.getDayOfMonth();
        int monthOfYear = datePicker.getMonth();
        int year = datePicker.getYear();
        String dayOfMonthStr = String.valueOf(dayOfMonth),
                monthOfYearStr = String.valueOf(monthOfYear+1);
        if (dayOfMonth < 10) dayOfMonthStr = "0" + dayOfMonth;
        if (monthOfYear + 1 < 10) monthOfYearStr = "0" + (monthOfYear+1);
        String selectedDate = dayOfMonthStr + "/" + monthOfYearStr + "/" + year;
        birth.setText(selectedDate);
    }
    private void setupView(boolean b) {
        fullName.setEnabled(b); phoneNumber.setEnabled(b);address.setEnabled(b);birth.setEnabled(b);
        height.setEnabled(b);weight.setEnabled(b);male.setEnabled(b);female.setEnabled(b);
        if (b){
            bottomLayout.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.GONE);
        }
        else {
            bottomLayout.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
        }
    }
    private void saveUserDetail() {
        String name = fullName.getText().toString();
        String gender = "";
        if (male.isChecked()) {
            gender = "Nam";
        } else {
            gender = "Nữ";
        }
        String phone = phoneNumber.getText().toString();
        String addressUser = address.getText().toString();
        String birthUser = birth.getText().toString();
        String heightStr = height.getText().toString();
        String weightStr = weight.getText().toString();

        if (name.isEmpty() || gender.isEmpty() || phone.isEmpty() ||
                addressUser.isEmpty() || birthUser.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty()) {
            CustomToast.showToast(getApplicationContext(),"Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT);
            return;
        }

        int userHeight = Integer.parseInt(heightStr);
        int userWeight = Integer.parseInt(weightStr);

        if (userHeight <= 0) {
            CustomToast.showToast(getApplicationContext(),"Chiều cao không hợp lệ",Toast.LENGTH_SHORT);
            return;
        }

        if (userWeight <= 0) {
            CustomToast.showToast(getApplicationContext(),"Cân nặng không hợp lệ",Toast.LENGTH_SHORT);
            return;
        }
        // Lưu dữ liệu
        String currentUserId = FirebaseUtil.currentUserId();
        UserModel userModel = new UserModel(name, gender, phone, addressUser, birthUser, userHeight,
                userWeight, Timestamp.now(), currentUserId);
        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               CustomToast.showToast(getApplicationContext(),"Lưu thành công",Toast.LENGTH_SHORT);
           }
        });
    }
}