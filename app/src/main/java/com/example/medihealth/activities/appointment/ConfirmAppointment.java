package com.example.medihealth.activities.appointment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.medihealth.R;
import com.example.medihealth.activities.MainActivity;
import com.example.medihealth.models.Appointment;
import com.example.medihealth.models.AppointmentConfirm;
import com.example.medihealth.models.AppointmentDTO;
import com.example.medihealth.models.Doctor;
import com.example.medihealth.models.Relative;
import com.example.medihealth.models.UserModel;
import com.example.medihealth.utils.AndroidUtil;
import com.example.medihealth.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class ConfirmAppointment extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences sharedPreferences;
    TextView personName, personGenderAndBirth, personPhone, relationship, specialist, doctorName,
            bookDate, symptom, reminderDate, reminderTime, time, date, price;
    SwitchCompat btnRemider;
    ImageView imageAccount;
    ImageButton btnBack;
    RelativeLayout change, btnBook, backAppointment;
    AppointmentDTO appointmentDTO;
    AppointmentConfirm appointmentConfirm;
    boolean isReminder = true;
    UserModel userModel;
    Relative relative;
    Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_appointment);
        sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
        appointmentDTO = AndroidUtil.getAppointmentDTOModelFromIntent(getIntent());
        appointmentConfirm = AndroidUtil.getAppointmentConfirmModelFromIntent(getIntent());
        getUserModel();
        getRelativeModel(appointmentDTO.getRelativePhoneNumber());
        getDoctorModel(appointmentDTO.getDoctorId());
        initView();
        setOnclick();
        setupUserImage(appointmentConfirm);
        setupDetailAppoitment(appointmentConfirm);
    }

    private void setupUserImage(AppointmentConfirm appointmentConfirm) {
        if (!appointmentConfirm.getRelationship().equals("Tôi")) return;
        String profileString = sharedPreferences.getString("profile", "empty");
        if (!profileString.equals("empty")) {
            String[] array = profileString.split(";");
            if (array.length > 0) {
                Picasso.get().load(array[1]).into(imageAccount);
            } else {
                Log.e("ERROR", "profileString rỗng");
            }
        }
    }

    private void initView() {
        imageAccount = findViewById(R.id.image_account);
        personName = findViewById(R.id.fullName_user);
        personPhone = findViewById(R.id.phoneNumber);
        relationship = findViewById(R.id.relative);
        personGenderAndBirth = findViewById(R.id.gender_birth);
        specialist = findViewById(R.id.specialist);
        doctorName = findViewById(R.id.doctor);
        bookDate = findViewById(R.id.time);
        symptom = findViewById(R.id.symptom);
        change = findViewById(R.id.change);
        price = findViewById(R.id.price);
        reminderDate = findViewById(R.id.reminder_date);
        reminderTime = findViewById(R.id.reminder_time);
        btnRemider = findViewById(R.id.notificationSwitch);
        btnBack = findViewById(R.id.back_btn);
        backAppointment = findViewById(R.id.btn_back);
        btnBook = findViewById(R.id.btn_book);
    }

    private void setOnclick() {
        change.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnBook.setOnClickListener(this);
        btnRemider.setOnClickListener(this);
        backAppointment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_book) {
            saveAppointment();
        }
        if (v.getId() == R.id.back_btn) {
            finish();
        }
        if (v.getId() == R.id.btn_back) {
            finish();
        }
        if (v.getId() == R.id.change) {
            showDialogRemider(Gravity.CENTER);
        }
        if (v.getId() == R.id.notificationSwitch) {
            if (!btnRemider.isChecked()) {
                isReminder = false;
            } else {
                isReminder = true;
            }
        }
    }

    private void showDialogRemider(int center) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_reminder);
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
        RelativeLayout btnCancel, btnEnter, blockTime, blockDate;

        time = dialog.findViewById(R.id.time);
        date = dialog.findViewById(R.id.date);
        btnCancel = dialog.findViewById(R.id.cancel);
        btnEnter = dialog.findViewById(R.id.enter);
        blockTime = dialog.findViewById(R.id.block_time);
        blockDate = dialog.findViewById(R.id.block_date);
        time.setText(reminderTime.getText().toString());
        date.setText(reminderDate.getText().toString());
        blockTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTimePicker(Gravity.CENTER);
            }
        });
        blockDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDatePicker(Gravity.CENTER);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupRemider();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setupRemider() {
        reminderTime.setText(time.getText().toString());
        reminderDate.setText(date.getText().toString());
    }

    private void showDialogDatePicker(int center) {
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
                getDate(datePicker);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getDate(DatePicker datePicker) {
        int dayOfMonth = datePicker.getDayOfMonth();
        int monthOfYear = datePicker.getMonth();
        int year = datePicker.getYear();
        String dayOfMonthStr = String.valueOf(dayOfMonth),
                monthOfYearStr = String.valueOf(monthOfYear + 1);
        if (dayOfMonth < 10) dayOfMonthStr = "0" + dayOfMonth;
        if (monthOfYear + 1 < 10) monthOfYearStr = "0" + (monthOfYear + 1);
        String selectedDate = dayOfMonthStr + "/" + monthOfYearStr + "/" + year;
        date.setText(selectedDate);
    }

    private void showDialogTimePicker(int center) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_timepicker);
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
        TimePicker timePicker;
        RelativeLayout btnCancel, btnSelect;
        timePicker = dialog.findViewById(R.id.timePicker);
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
                getTime(timePicker);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getTime(TimePicker timePicker) {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        String hourStr = String.valueOf(hour);
        if (hour < 10) hourStr = "0" + hourStr;
        String minuteStr = String.valueOf(minute);
        if (minute < 10) minuteStr = "0" + minuteStr;
        String timeStr = hourStr + ":" + minuteStr;
        time.setText(timeStr);
    }

    private void saveAppointment() {
        Appointment appointment = null;
        if (!(userModel == null && relative == null) && doctor != null) {
            if (userModel != null) {
                appointment = new Appointment(userModel, appointmentDTO.getBookDate(), appointmentDTO.getAppointmentDate(),
                        appointmentDTO.getSpecialist(), doctor, appointmentDTO.getTime(), appointmentDTO.getSymptom(), 0,
                        reminderTime.getText().toString(), reminderDate.getText().toString(), isReminder);
            }
            if (relative != null) {
                appointment = new Appointment(relative, appointmentDTO.getBookDate(), appointmentDTO.getAppointmentDate(),
                        appointmentDTO.getSpecialist(), doctor, appointmentDTO.getTime(), appointmentDTO.getSymptom(), 0,
                        reminderTime.getText().toString(), reminderDate.getText().toString(), isReminder);
            }
        }
        if (appointment != null) {
            FirebaseUtil.getAppointmentCollectionReference().add(appointment).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    showDialogNotice(Gravity.BOTTOM);
                } else {
                    Log.e("ERROR", "Lỗi kết nối");
                }
            });
        }
    }

    private void showDialogNotice(int bottom) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_notify_successfull);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = bottom;
        window.setAttributes(windowAttributes);
        if (Gravity.BOTTOM == bottom) {
            dialog.setCancelable(false);
        } else {
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
                intent.putExtra("requestCode", 131);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    private void getDoctorModel(String doctorId) {
        Query query = FirebaseUtil.allDoctorCollectionReference().whereEqualTo("doctorId", doctorId);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String documentId = documentSnapshot.getId();
                    FirebaseUtil.allDoctorCollectionReference().document(documentId).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            doctor = task.getResult().toObject(Doctor.class);
                        } else {
                            Log.e("ERROR", "Lỗi kết nối");
                        }
                    });
                }
            }
        });
    }

    private void getRelativeModel(String relativePhone) {
        Query query = FirebaseUtil.getRelativeCollectionReference().whereEqualTo("phoneNumber", relativePhone);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String documentId = documentSnapshot.getId();
                    FirebaseUtil.getRelativeCollectionReference().document(documentId).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            relative = task.getResult().toObject(Relative.class);
                        } else {
                            Log.e("ERROR", "Lỗi kết nối");
                        }
                    });
                }
            }
        });
    }

    private void getUserModel() {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userModel = task.getResult().toObject(UserModel.class);
            } else {
                Log.e("ERROR", "Lỗi kết nối");
            }
        });
    }

    private void setupDetailAppoitment(AppointmentConfirm appointmentConfirm) {
        personName.setText(appointmentConfirm.getPersonName());
        personPhone.setText("Điện thoại: " + appointmentConfirm.getPhoneNumber());
        relationship.setText(appointmentConfirm.getRelationship());
        personGenderAndBirth.setText(appointmentConfirm.getGender() + " - " + appointmentConfirm.getBirth());
        specialist.setText(appointmentConfirm.getSpecialist());
        doctorName.setText(appointmentConfirm.getDoctorName());
        bookDate.setText(appointmentConfirm.getTime() + " - " + appointmentConfirm.getAppointmentDate());
        symptom.setText(appointmentConfirm.getSymptom());
        price.setText(AndroidUtil.formatPrice(appointmentConfirm.getPrice()) + " đ");
        reminderDate.setText(appointmentConfirm.getAppointmentDate());
    }
}