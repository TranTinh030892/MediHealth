package com.example.medihealth.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medihealth.R;
import com.example.medihealth.models.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Set;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    GoogleSignInClient googleSignInClient;
    SharedPreferences sharedPreferences;
    FirebaseAuth mAuth;
    ImageView imageAccount;
    ImageButton btnBack;
    TextView title;
    EditText fullName, phoneNumber, address,  height, weight;
    TextView birth;
    RadioButton male, female;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
        initView();
        initOnclick();
    }
    private void initView() {
        imageAccount = findViewById(R.id.image_account);
        title = findViewById(R.id.title);
        setImageAndTitle();
        btnBack = findViewById(R.id.back_btn);
        fullName = findViewById(R.id.fullName_user);
        fullName.requestFocus();
        phoneNumber = findViewById(R.id.phoneNumber_user);
        address = findViewById(R.id.address_user);
        birth = findViewById(R.id.birth_user);
        height = findViewById(R.id.height_user);
        weight = findViewById(R.id.weight_user);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        save = findViewById(R.id.btn_save);
    }

    private void setImageAndTitle() {
        String profileString = sharedPreferences.getString("profile", "empty");
        if (!profileString.equals("empty")){
            String[] array = profileString.split(";");
            if (array.length > 0) {
                Picasso.get().load(array[1]).into(imageAccount);
                title.setText("Xin chào "+array[0]);
            } else {
                Log.e("ERROR", "profileString rỗng");
            }
        }
    }

    private void initOnclick() {
        save.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        birth.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save){
            // kiểm tra điều kiện dữ liệu
            String name = fullName.getText().toString();
            String gender = "";
            if (male.isChecked()){
                gender = "Nam";
            }
            else gender = "Nữ";
            String phone = phoneNumber.getText().toString();
            String addressUser = address.getText().toString();
            String birthUser = birth.getText().toString();
            int userHeight = Integer.parseInt(height.getText().toString());
            int userWeight = Integer.parseInt(weight.getText().toString());
            if (name.equals("") || gender.equals("") || phone.equals("")
            || addressUser.equals("") || birthUser.equals("") || height.getText().toString().equals("")
            || weight.getText().toString().equals("")){
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin hồ sơ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (userHeight <=0){
                Toast.makeText(this, "Chiều cao không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (userWeight <=0){
                Toast.makeText(this, "Cân nặng không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            // Lưu dữ liệu
            FirebaseUser currentUser = mAuth.getCurrentUser();
            UserModel userModel = new UserModel(name,gender,phone,addressUser,birthUser,userHeight
            ,userWeight, Timestamp.now(),currentUser.getUid());
            FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid())
                    .set(userModel).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Log.d("SUCCESSFULL","Save UserInfor Success");
                        }
                        else {
                            Log.e("ERROR","Lỗi kết nối mạng");
                        }
                    });
            // Chuyển activity
            Intent intent = new Intent(Profile.this,MainActivity.class);
            startActivity(intent);
        }
        else if (v.getId() == R.id.back_btn){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("profile","empty");
            editor.apply();
            signOut();
        }
        else if (v.getId() == R.id.birth_user){
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
        if (dayOfMonth < 10) dayOfMonthStr = "0" + dayOfMonthStr;
        if (monthOfYear < 10) monthOfYearStr = "0" + monthOfYearStr;
        String date = dayOfMonthStr + "/" + monthOfYearStr + "/" + year;
        birth.setText(date);
    }
    private void signOut() {
        mAuth.signOut();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Khởi tạo GoogleSignInClient
        googleSignInClient = GoogleSignIn.getClient(this,gso);
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(Profile.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}