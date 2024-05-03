package com.example.medihealth.activities.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.example.medihealth.activities.MainActivity;
import com.example.medihealth.models.CustomToast;
import com.example.medihealth.models.Relative;
import com.example.medihealth.utils.AndroidUtil;
import com.example.medihealth.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class EditProfileRelative extends AppCompatActivity implements View.OnClickListener{
    ImageButton btnBack,btnEdit,iconDelete;
    EditText fullName, phoneNumber, address, height, weight,relationship;
    RadioButton male, female;
    TextView birth,btnRelationship;
    RelativeLayout btnSave,btnDelete;
    Relative relative;
    String relativeDocumentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_relative);
        relative = AndroidUtil.getRelativeModelFromIntent(getIntent());
        initView();
        setOnlick();
        setupRelativeDetail(relative);
        getDocument();
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
        btnSave = findViewById(R.id.enterBook);
        btnDelete = findViewById(R.id.delete);
        relationship = findViewById(R.id.relationship);
        iconDelete = findViewById(R.id.icon_delete);
        btnRelationship = findViewById(R.id.btnRelationship);
    }
    private void setOnlick() {
        btnBack.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        birth.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        iconDelete.setOnClickListener(this);
    }
    private void setupRelativeDetail(Relative relative) {
        btnRelationship.setText(relative.getRelationship());
        fullName.setText(relative.getFullName());
        phoneNumber.setText(relative.getPhoneNumber());
        address.setText(relative.getAddress());
        birth.setText(relative.getBirth());
        if (relative.getGender().equals("Nam")){
            male.setChecked(true);
        }
        else female.setChecked(true);
        height.setText(String.valueOf(relative.getHeight()));
        weight.setText(String.valueOf(relative.getWeight()));
        relationship.setText(relative.getRelationship());
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_btn){
            finish();
        }
        if (v.getId() == R.id.btn_edit){
            getDocument();
            setupView(true);
        }
        if (v.getId() == R.id.enterBook){
            setupView(false);
            saveDetailRelative();
        }
        if (v.getId() == R.id.icon_delete){
            setupView(false);
        }
        if (v.getId() == R.id.birth_user){
            showDialogCalendar(Gravity.CENTER);
        }
        if (v.getId() == R.id.delete){
            showDialogConfirm(Gravity.CENTER);
        }
    }

    private void showDialogConfirm(int center) {
        final Dialog dialog = new Dialog(this);
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

        TextView titleTop, titleBelow;
        RelativeLayout btnCancel, btnEnter;
        titleTop = dialog.findViewById(R.id.title_top);
        titleBelow = dialog.findViewById(R.id.title_below);
        btnCancel = dialog.findViewById(R.id.cancel);
        btnEnter = dialog.findViewById(R.id.agree);
        titleTop.setText("Xác nhận");
        titleBelow.setText("Bạn có chắc chắn muốn xóa ?");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteRelative();
            }
        });
        dialog.show();
    }

    private void deleteRelative() {
        FirebaseUtil.getRelativeCollectionReference().document(relativeDocumentId).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                CustomToast.showToast(getApplicationContext(),"Xóa thành công",Toast.LENGTH_SHORT);
                finish();
            }
            else{
                CustomToast.showToast(getApplicationContext(),"Xóa không thành công",Toast.LENGTH_SHORT);
            }
        });
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

    private void getDocument() {
        Query query = FirebaseUtil.getRelativeCollectionReference().whereEqualTo("phoneNumber",phoneNumber.getText().toString());
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String documentId = documentSnapshot.getId();
                    relativeDocumentId = documentId;
                }
            }
        });
    }

    private void saveDetailRelative() {
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
        String relationshipStr = relationship.getText().toString();
        if (name.isEmpty() || gender.isEmpty() || phone.isEmpty() ||
                addressUser.isEmpty() || birthUser.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty()|| relationshipStr.isEmpty()) {
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
        String currentUserId = FirebaseUtil.currentUserId();
        Relative relative = new Relative(name, gender, phone, addressUser, birthUser, userHeight, userWeight, relationshipStr, currentUserId);
        FirebaseUtil.getRelativeCollectionReference().document(relativeDocumentId).set(relative).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                CustomToast.showToast(getApplicationContext(),"Lưu thành công",Toast.LENGTH_SHORT);
                setupRelativeDetail(relative);
            }
            else{
                CustomToast.showToast(getApplicationContext(),"Lưu không thành công",Toast.LENGTH_SHORT);
            }
        });
    }
    private void setupView(boolean b) {
        fullName.setEnabled(b); phoneNumber.setEnabled(b);address.setEnabled(b);birth.setEnabled(b);
        height.setEnabled(b);weight.setEnabled(b);male.setEnabled(b);female.setEnabled(b);relationship.setEnabled(b);
        if (b){
            btnDelete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
            iconDelete.setVisibility(View.VISIBLE);
        }
        else {
            btnDelete.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);
            iconDelete.setVisibility(View.GONE);
        }
    }
}