package com.example.medihealth.activitys.appointment;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medihealth.R;
import com.example.medihealth.activitys.MainActivity;
import com.example.medihealth.models.Appointment;
import com.example.medihealth.models.AppointmentConfirm;
import com.example.medihealth.models.AppointmentDTO;
import com.example.medihealth.models.CustomToast;
import com.example.medihealth.models.Doctor;
import com.example.medihealth.models.Relative;
import com.example.medihealth.models.UserModel;
import com.example.medihealth.utils.AndroidUtil;
import com.example.medihealth.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ConfirmAppointment extends AppCompatActivity implements View.OnClickListener{
    TextView personName,personGenderAndBirth,relationship,specialist,doctorName,
            bookDate,symptom,reminderDate,reminderTime;
    RelativeLayout btnBack,change,btnBook;
    AppointmentDTO appointmentDTO;
    AppointmentConfirm appointmentConfirm;
    String relativeDocumentId;
    UserModel userModel;
    Relative relative;
    Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_appointment);
        appointmentDTO = AndroidUtil.getAppointmentDTOModelFromIntent(getIntent());
        appointmentConfirm = AndroidUtil.getAppointmentConfirmModelFromIntent(getIntent());
        getUserModel();
        getRelativeModel(appointmentDTO.getRelativePhoneNumber());
        getDoctorModel(appointmentDTO.getDoctorId());
        initView();
        setOnclick();
        setupDetailAppoitment(appointmentConfirm);
    }

    private void initView() {
        personName = findViewById(R.id.fullName_user);
        relationship = findViewById(R.id.relative);
        personGenderAndBirth = findViewById(R.id.gender_birth);
        specialist = findViewById(R.id.specialist);
        doctorName = findViewById(R.id.doctor);
        bookDate = findViewById(R.id.time);
        symptom = findViewById(R.id.symptom);
        change = findViewById(R.id.change);
        reminderDate = findViewById(R.id.reminder_date);
        reminderTime = findViewById(R.id.reminder_time);
        btnBack = findViewById(R.id.btn_back);
        btnBook = findViewById(R.id.btn_book);
    }
    private void setOnclick() {
        change.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnBook.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_book){
            saveAppointment();
        }
        if (v.getId() == R.id.btn_back){
            finish();
        }
//        if (v.getId() == R.id.btn_back){
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        }
//        if (v.getId() == R.id.btn_book){
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.putExtra("requestCode",131);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        }
    }

    private void saveAppointment() {
        Appointment appointment = null;
        if (!(userModel == null && relative == null) && doctor != null){
            if (userModel != null){
                appointment = new Appointment(userModel,appointmentDTO.getBookDate(),appointmentDTO.getAppointmentDate(),
                        appointmentDTO.getSpecialist(),doctor,appointmentDTO.getTime(),appointmentDTO.getSymptom(),0);
            }
            if (relative != null){
                appointment = new Appointment(relative,appointmentDTO.getBookDate(),appointmentDTO.getAppointmentDate(),
                        appointmentDTO.getSpecialist(),doctor,appointmentDTO.getTime(),appointmentDTO.getSymptom(),0);
            }
        }
        if (appointment != null){
            FirebaseUtil.getAppointmentCollectionReference().add(appointment).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    showDialogNotice(Gravity.BOTTOM);
                }
                else {
                    Log.e("ERROR","Lỗi kết nối");
                }
            });
        }
    }

    private void showDialogNotice(int bottom) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_notify_successfull);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = bottom;
        window.setAttributes(windowAttributes);
        if (Gravity.BOTTOM == bottom){
            dialog.setCancelable(false);
        }
        else{
            dialog.setCancelable(true);
        }
        Button btnHome, btnDetail;
        btnHome = dialog.findViewById(R.id.btn_Home);
        btnDetail = dialog.findViewById(R.id.btn_detail);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(ConfirmAppointment.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(ConfirmAppointment.this, MainActivity.class);
                intent.putExtra("requestCode",131);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    private void getDoctorModel(String doctorId) {
        Query query = FirebaseUtil.allDoctorCollectionReference().whereEqualTo("doctorId",doctorId);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String documentId = documentSnapshot.getId();
                    FirebaseUtil.allDoctorCollectionReference().document(documentId).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            doctor = task.getResult().toObject(Doctor.class);
                        }
                        else {
                            Log.e("ERROR","Lỗi kết nối");
                        }
                    });
                }
            }
        });
    }

    private void getRelativeModel(String relativePhone) {
        Query query = FirebaseUtil.getRelativeCollectionReference().whereEqualTo("phoneNumber",relativePhone);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String documentId = documentSnapshot.getId();
                    FirebaseUtil.getRelativeCollectionReference().document(documentId).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            relative = task.getResult().toObject(Relative.class);
                        }
                        else {
                            Log.e("ERROR","Lỗi kết nối");
                        }
                    });
                }
            }
        });
    }

    private void getUserModel() {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                userModel = task.getResult().toObject(UserModel.class);
            }
            else {
                Log.e("ERROR","Lỗi kết nối");
            }
        });
    }

    private void setupDetailAppoitment(AppointmentConfirm appointmentConfirm) {
        personName.setText(appointmentConfirm.getPersonName());
        relationship.setText(appointmentConfirm.getRelationship());
        personGenderAndBirth.setText(appointmentConfirm.getGender()+" - "+appointmentConfirm.getBirth());
        specialist.setText(appointmentConfirm.getSpecialist());
        doctorName.setText(appointmentConfirm.getDoctorName());
        bookDate.setText(appointmentConfirm.getTime()+" - "+appointmentConfirm.getAppointmentDate());
        symptom.setText(appointmentConfirm.getSymptom());
        reminderDate.setText(appointmentConfirm.getAppointmentDate());
    }

    private void setupDoctorName(AppointmentDTO appointmentDTO) {
        String doctorId = appointmentDTO.getDoctorId();
        FirebaseUtil.getDoctorDetailsById(doctorId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Doctor doctor = task.getResult().toObject(Doctor.class);
                doctorName.setText(doctor.getFullName());
            }
            else {
                Log.e("ERROR","Lỗi kết nối");
            }
        });
    }

    private void setupDetailPerson(AppointmentDTO appointmentDTO) {
        String userId = appointmentDTO.getUserId();
        String relativePhoneNumber = appointmentDTO.getRelativePhoneNumber();
        if (userId != null){
            FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    UserModel userModel = task.getResult().toObject(UserModel.class);
                    personName.setText(userModel.getFullName());
                    relationship.setText("Tôi");
                    personGenderAndBirth.setText(userModel.getGender()+" - "+userModel.getBirth());
                }
                else {
                    Log.e("ERROR","Lỗi kết nối");
                }
            });
        }
        if (relativePhoneNumber != null){
            Query query = FirebaseUtil.getRelativeCollectionReference().whereEqualTo("phoneNumber",relativePhoneNumber);
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String documentId = documentSnapshot.getId();
                        FirebaseUtil.getRelativeCollectionReference().document(documentId).get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                relative = task.getResult().toObject(Relative.class);
                            }
                            else {
                                Log.e("ERROR","Lỗi kết nối");
                            }
                        });
                    }
                }
            });
        }
    }
}