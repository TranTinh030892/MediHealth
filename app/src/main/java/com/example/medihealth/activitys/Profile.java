package com.example.medihealth.activitys;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
            if (name.equals("") || gender.equals("") || phone.equals("")
            || addressUser.equals("") || birthUser.equals("")){
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin hồ sơ", Toast.LENGTH_SHORT).show();
                return;
            }
            // Lưu dữ liệu
            FirebaseUser currentUser = mAuth.getCurrentUser();
            UserModel userModel = new UserModel(name,gender,phone,addressUser,birthUser
            , Timestamp.now(),currentUser.getUid());
            FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid())
                    .set(userModel).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Log.d("SUCCESSFULL","Save UserInfor Success");
                        }
                        else {
                            Log.e("ERROR","ERROR");
                            return;
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
            getBirthDay();
        }
    }

    private void getBirthDay() {
        int year = 0, month = 0, day = 0;
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String dayOfMonthStr = String.valueOf(dayOfMonth),
                                monthOfYearStr = String.valueOf(monthOfYear+1);
                        if (dayOfMonth < 10) dayOfMonthStr = "0" + dayOfMonthStr;
                        if (monthOfYear < 10) monthOfYearStr = "0" + monthOfYearStr;
                        String date = dayOfMonthStr + "/" + monthOfYearStr + "/" + year;
                        birth.setText(date);
                    }
                }, year, month, day);
        // Hiển thị DatePickerDialog
        datePickerDialog.show();
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