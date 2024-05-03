package com.example.medihealth.activities.profile;

import android.app.Dialog;
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

import androidx.appcompat.app.AppCompatActivity;

import com.example.medihealth.R;
import com.example.medihealth.models.CustomToast;
import com.example.medihealth.models.Relative;
import com.example.medihealth.utils.FirebaseUtil;

public class AddRelative extends AppCompatActivity implements View.OnClickListener {
    ImageButton btnBack;
    TextView birthRelative;
    EditText fullName, phoneNumber, address, height, weight, relationship;
    RadioButton male, female;
    RelativeLayout save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_relative);
        innitView();
        setOnclick();
    }

    private void innitView() {
        btnBack = findViewById(R.id.back_btn);
        fullName = findViewById(R.id.fullName_user);
        fullName.requestFocus();
        phoneNumber = findViewById(R.id.phoneNumber_user);
        address = findViewById(R.id.address_user);
        birthRelative = findViewById(R.id.birth_user);
        height = findViewById(R.id.height_user);
        weight = findViewById(R.id.weight_user);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        relationship = findViewById(R.id.relationship);
        save = findViewById(R.id.btn_saveRelative);
    }

    private void setOnclick() {
        btnBack.setOnClickListener(this);
        birthRelative.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_btn) {
            finish();
        }
        if (v.getId() == R.id.birth_user) {
            showDatePickerRalative(Gravity.CENTER);
        }
        if (v.getId() == R.id.btn_saveRelative) {
            saveRelative();
        }
    }

    private void showDatePickerRalative(int center) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_datepicker);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = center;
        window.setAttributes(windowAttributes);
        if (Gravity.BOTTOM == center) {
            dialog.setCancelable(false);
        } else {
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
                getDateRelative(datePicker);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getDateRelative(DatePicker datePicker) {
        int dayOfMonth = datePicker.getDayOfMonth();
        int monthOfYear = datePicker.getMonth();
        int year = datePicker.getYear();
        String dayOfMonthStr = String.valueOf(dayOfMonth),
                monthOfYearStr = String.valueOf(monthOfYear + 1);
        if (dayOfMonth < 10) dayOfMonthStr = "0" + dayOfMonth;
        if (monthOfYear + 1 < 10) monthOfYearStr = "0" + (monthOfYear + 1);
        String selectedDate = dayOfMonthStr + "/" + monthOfYearStr + "/" + year;
        birthRelative.setText(selectedDate);
    }

    private void saveRelative() {
        String name = fullName.getText().toString();
        String gender = "";
        if (male.isChecked()) {
            gender = "Nam";
        } else {
            gender = "Nữ";
        }
        String phone = phoneNumber.getText().toString();
        String addressUser = address.getText().toString();
        String birthUser = birthRelative.getText().toString();
        String heightStr = height.getText().toString();
        String weightStr = weight.getText().toString();
        String relationshipStr = relationship.getText().toString();
        if (name.isEmpty() || gender.isEmpty() || phone.isEmpty() ||
                addressUser.isEmpty() || birthUser.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty() || relationshipStr.isEmpty()) {
            CustomToast.showToast(getApplicationContext(), "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT);
            return;
        }

        int userHeight = Integer.parseInt(heightStr);
        int userWeight = Integer.parseInt(weightStr);

        if (userHeight <= 0) {
            CustomToast.showToast(getApplicationContext(), "Chiều cao không hợp lệ", Toast.LENGTH_SHORT);
            return;
        }

        if (userWeight <= 0) {
            CustomToast.showToast(getApplicationContext(), "Cân nặng không hợp lệ", Toast.LENGTH_SHORT);
            return;
        }
        String currentUserId = FirebaseUtil.currentUserId();
        Relative relative = new Relative(name, gender, phone, addressUser, birthUser, userHeight, userWeight, relationshipStr, currentUserId);
        FirebaseUtil.getRelativeCollectionReference().add(relative).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                finish();
                CustomToast.showToast(getApplicationContext(), "Lưu thành công", Toast.LENGTH_SHORT);
            } else {
                CustomToast.showToast(getApplicationContext(), "Lưu không thành công", Toast.LENGTH_SHORT);
            }
        });
    }
}