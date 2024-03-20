package com.example.medihealth.activitys.book_appointment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medihealth.R;
import com.example.medihealth.activitys.MainActivity;
import com.example.medihealth.adapters.book_appointment.DoctorAdapter;
import com.example.medihealth.models.Appointment;
import com.example.medihealth.models.CustomToast;
import com.example.medihealth.models.Doctor;
import com.example.medihealth.utils.AndroidUtil;
import com.example.medihealth.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class Infor_Appoitment_Activity extends AppCompatActivity implements View.OnClickListener {
    private DoctorAdapter.DoctorViewHolder selectedViewHolder = null;
    private EditText editTextSymptom;
    private ImageButton btnDateDialog, btnSpecialist, btnBack;
    private ImageView imageAccount;
    RelativeLayout  btnInforDoctor, btnEnter;
    ProgressBar progressBarLoadDoctors ,progressBarLoadNumberOrder ;
    private TextView editTextDate, editTextSpecialist, textViewName,fullNameUser,
            birthUser,textViewNumberOrder, textNotify, notifyNumberOrder;
    RecyclerView recyclerView;
    DoctorAdapter adapterModel;
    SharedPreferences sharedPreferences;
    final Calendar c = Calendar.getInstance();
    private int year, month, day;
    private int indexDoctorSelected = -1;
    private boolean selected = false;
    private int sizeListDoctors = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_appointment);
        sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
        initReference();
        setInforUser();
        getDataDoctors();
        setOnclick();
        setNumberOrder(editTextDate.getText().toString(),0);
    }

    private void setInforUser() {
        String inforFormUser = sharedPreferences.getString("inforFormUser", "empty");
        if (!inforFormUser.equals("empty")){
            String[] array = inforFormUser.split(";");
            if (array.length > 0) {
                fullNameUser.setText(array[0]);
                birthUser.setText(array[1]);
            } else {
                Log.e("ERROR", "profileString rỗng");
            }
        }
    }

    private void getDataDoctors() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Ẩn progressBar sau khi đã trì hoãn
                progressBarLoadDoctors.setVisibility(View.GONE);
                // Gọi phương thức setListDoctors()
                setListDoctors();
            }
        }, 1000);
    }

    private void setBackgroundItem(int position) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View itemView = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(itemView);
            if (viewHolder instanceof DoctorAdapter.DoctorViewHolder) {
                DoctorAdapter.DoctorViewHolder doctorViewHolder = (DoctorAdapter.DoctorViewHolder) viewHolder;
                if (i == position) {
                    setSelected(doctorViewHolder);
                } else {
                    clearSelection(doctorViewHolder);
                }
            }
        }
        Doctor doctor = adapterModel.getItem(position);
        textViewName.setText(doctor.getFullName());
    }

    private void setSelected(DoctorAdapter.DoctorViewHolder holder) {
        // Đặt hiệu ứng cho ViewHolder được chọn
        Drawable selectedBackground = ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_form_doctor);
        holder.itemView.setBackground(selectedBackground);
        selectedViewHolder = holder; // Lưu trữ ViewHolder được chọn
    }

    private void clearSelection(DoctorAdapter.DoctorViewHolder holder) {
        // Xóa hiệu ứng của ViewHolder trước đó
        holder.itemView.setBackground(null);
    }

    private void setListDoctors() {
        String specialistInput = editTextSpecialist.getText().toString().trim();
        Query query = null;
        if(specialistInput.equals("")){
            query = FirebaseUtil.allDoctorCollectionReference()
                    .whereNotEqualTo("fullName","Mặc định");
        }
        else {
            query = FirebaseUtil.allDoctorCollectionReference()
                    .whereEqualTo("specialist",specialistInput);
        }
        FirestoreRecyclerOptions<Doctor> options = new FirestoreRecyclerOptions.Builder<Doctor>()
                .setQuery(query,Doctor.class).build();

        adapterModel = new DoctorAdapter(options, getApplicationContext(), new DoctorAdapter.IDoctorViewHolder() {
            @Override
            public void onClickItem(int positon) {
                indexDoctorSelected = positon;
                selected = true;
                setBackgroundItem(positon);
                if (editTextSpecialist.getText().toString().trim().equals("")){
                    return;
                }
                textViewNumberOrder.setText("");
                progressBarLoadNumberOrder.setVisibility(View.VISIBLE);
                setViewNumberOrder(0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Ẩn progressBar sau khi đã trì hoãn
                        progressBarLoadNumberOrder.setVisibility(View.GONE);
                        // Gọi phương thức setListDoctors()
                        setNumberOrder(editTextDate.getText().toString(),positon);
                    }
                }, 1000);
            }
            @Override
            public void onDataLoaded(int size) {
                sizeListDoctors = size;
                if (size == 0){
                    textNotify.setVisibility(View.VISIBLE);
                    textViewNumberOrder.setText("---");
                }
                else textNotify.setVisibility(View.GONE);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(adapterModel);
        adapterModel.startListening();
    }

    private void setNumberOrder(String date, int positon) {
        Doctor doctor = null;
        if (adapterModel != null && sizeListDoctors != 0 && selected){
            doctor = adapterModel.getItem(positon);
        }
        else {
            setViewNumberOrder(1);
            textViewNumberOrder.setText("---");
        }
        if(doctor != null){
            // lấy số thứ tự khám
            Query query = FirebaseUtil.getAppointmentCollectionReference()
                    .whereEqualTo("date",date)
                    .whereEqualTo("doctorId",doctor.getDoctorId());
            query.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    int numberOfRecords = task.getResult().size();
                    if (numberOfRecords >= 2){ // số lượng để test
                        setViewNumberOrder(2);
                    }
                    else {
                        setViewNumberOrder(1);
                        String numStr = "0" + String.valueOf(numberOfRecords + 1);
                        textViewNumberOrder.setText(numStr);
                    }
                }
            });
        }
    }

    private void setOnclick() {
        editTextDate.setOnClickListener(this);
        editTextSpecialist.setOnClickListener(this);
        editTextSymptom.setOnClickListener(this);
        btnDateDialog.setOnClickListener(this);
        btnSpecialist.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnInforDoctor.setOnClickListener(this);
        btnEnter.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void initReference() {
        fullNameUser = findViewById(R.id.fullName_user);
        birthUser = findViewById(R.id.birth_user);
        imageAccount = findViewById(R.id.image_user);
        editTextDate = findViewById(R.id.date);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        editTextDate.setText(day+"/"+(month+1)+"/"+year);
        editTextSpecialist = findViewById(R.id.specialist);
        textViewNumberOrder = findViewById(R.id.numberOrder);
        editTextSymptom = findViewById(R.id.symptom);
        btnDateDialog = findViewById(R.id.btnDateDialog);
        btnInforDoctor = findViewById(R.id.infor_detail_doctor);
        btnSpecialist = findViewById(R.id.btnSpecialist);
        btnEnter = findViewById(R.id.enterBook);
        textViewName = findViewById(R.id.nameDoctor);
        textNotify = findViewById(R.id.notify);
        notifyNumberOrder = findViewById(R.id.notify_numberOrder);
        progressBarLoadDoctors = findViewById(R.id.loadListDoctors);
        progressBarLoadNumberOrder = findViewById(R.id.loadNumberOrder);
        recyclerView = findViewById(R.id.doctorList);
        btnBack = findViewById(R.id.back_btn);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.date || v.getId() == R.id.btnDateDialog){
            showDatePicker(Gravity.CENTER);
        }
        else if (v.getId() == R.id.specialist || v.getId() == R.id.btnSpecialist){
            setStringSharedPreferences("date",editTextDate.getText().toString());
            setStringSharedPreferences("specialistSelected",editTextSpecialist.getText().toString());
            Intent intent = new Intent(Infor_Appoitment_Activity.this, Specialist.class);
            startActivity(intent);
        }
        else if (v.getId() == R.id.back_btn){
            Intent intent = new Intent(Infor_Appoitment_Activity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.infor_detail_doctor){
            if (!selected){
                Toast.makeText(this, "Chưa chọn bác sĩ", Toast.LENGTH_SHORT).show();
                return;
            }
            showInforDoctor(indexDoctorSelected, Gravity.CENTER);
        }
        else if (v.getId() == R.id.enterBook){
            if (!validation()) return;
            Intent intent = new Intent(Infor_Appoitment_Activity.this, Confirm_Appointment.class);
            setDataIntent(intent);
            startActivity(intent);
        }
    }

    private void setDataIntent(Intent intent) {
        String userId = FirebaseUtil.currentUserId();
        String dateBook = editTextDate.getText().toString().trim();
        String specialist = editTextSpecialist.getText().toString().trim();
        String doctorId = adapterModel.getItem(indexDoctorSelected).getDoctorId();
        String orderStr = textViewNumberOrder.getText().toString().trim();
        int order = Integer.parseInt(orderStr);
        String symptom = editTextSymptom.getText().toString().trim();
        int stateAppointment = 0;
        Appointment appointment = new Appointment(userId,dateBook,specialist,doctorId,
                order,symptom,stateAppointment);
        AndroidUtil.passAppointmentModelAsIntent(intent,appointment);
    }

    private boolean validation() {
        if (editTextSpecialist.getText().toString().trim().equals("")){
            CustomToast.showToast(getApplicationContext(),"Chọn chuyên khoa",Toast.LENGTH_SHORT);
            return false;
        }
        if (!selected || indexDoctorSelected == -1){
            CustomToast.showToast(getApplicationContext(),"Chọn bác sĩ",Toast.LENGTH_SHORT);
            return false;
        }
        if (editTextSymptom.getText().toString().trim().equals("")){
            CustomToast.showToast(getApplicationContext(),"Nhập triệu chứng",Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }

    private void showInforDoctor(int indexDoctorSelected, int center) {
        Doctor doctor = null;
        if (adapterModel != null){
            doctor = adapterModel.getItem(indexDoctorSelected);
        }
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_infordoctor);
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
        TextView textFullName, textGender, textSpecialist, textPrice,textDegree;
        Button btnConfirm;
        textFullName = dialog.findViewById(R.id.name_doctor);
        textDegree = dialog.findViewById(R.id.degree);
        textGender = dialog.findViewById(R.id.gender);
        textSpecialist = dialog.findViewById(R.id.specialist);
        textPrice = dialog.findViewById(R.id.price);
        if (doctor != null){
            textFullName.setText(doctor.getFullName());
            textDegree.setText("Học vị: "+doctor.getDegree());
            textGender.setText("Giới tính: "+doctor.getGender());
            textSpecialist.setText("Chuyên khoa: "+doctor.getSpecialist());
            textPrice.setText("Giá khám: "+String.valueOf(doctor.getPrice())+"đ");
        }
        btnConfirm = dialog.findViewById(R.id.confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void showDatePicker(int center) {
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
        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        editTextDate.setText(selectedDate);
        setStringSharedPreferences("date",selectedDate);
        if (editTextSpecialist.getText().toString().trim().equals("")){
            return;
        }
        setViewNumberOrder(0);
        progressBarLoadNumberOrder.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBarLoadNumberOrder.setVisibility(View.GONE);
                setNumberOrder(selectedDate,indexDoctorSelected);
            }
        }, 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapterModel!=null)
            adapterModel.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
        Intent intent = getIntent();
        if (intent != null) {
            int code = intent.getIntExtra("requestCode", 0);
            String specialistSelected = intent.getStringExtra("specialistSelected");
            if (code == 100) {
                String dateSate = sharedPreferences.getString("date", "aa");
                String specialistState = sharedPreferences.getString("specialistSelected", "");
                editTextDate.setText(dateSate);
                editTextSpecialist.setText(specialistState);
            }
            if (specialistSelected != null){
                editTextSpecialist.setText(specialistSelected);
            }
        }
        progressBarLoadNumberOrder.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Ẩn progressBar sau khi đã trì hoãn
                progressBarLoadDoctors.setVisibility(View.GONE);
                progressBarLoadNumberOrder.setVisibility(View.GONE);
                // Gọi phương thức setListDoctors()
                setListDoctors();
                textViewNumberOrder.setText("---");
            }
        }, 1000);
    }

    private void refresh() {
        textViewNumberOrder.setText("");
        selected = false;
        textViewName.setText("");
        setViewNumberOrder(1);
    }
    private void setViewNumberOrder(int a){
        if (a == 0){
            textViewNumberOrder.setVisibility(View.INVISIBLE);
            notifyNumberOrder.setVisibility(View.INVISIBLE);
        }
        else if (a == 1){
            textViewNumberOrder.setVisibility(View.VISIBLE);
            notifyNumberOrder.setVisibility(View.INVISIBLE);
        }
        else {
            textViewNumberOrder.setVisibility(View.INVISIBLE);
            notifyNumberOrder.setVisibility(View.VISIBLE);
        }
    }
    private void setStringSharedPreferences(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    private void setObjectSharedPreferences(Appointment appointment){
    }
}