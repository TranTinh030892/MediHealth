package com.example.medihealth.activites.appointment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medihealth.R;
import com.example.medihealth.activites.MainActivity;
import com.example.medihealth.trang.profile.activity.AddRelative;
import com.example.medihealth.adapters.appointment.DoctorAdapter;
import com.example.medihealth.adapters.appointment.RelativeAdapter;
import com.example.medihealth.adapters.appointment.TimeAdapter;
import com.example.medihealth.models.Appointment;
import com.example.medihealth.models.AppointmentConfirm;
import com.example.medihealth.models.AppointmentDTO;
import com.example.medihealth.models.CustomToast;
import com.example.medihealth.models.Doctor;
import com.example.medihealth.models.Relative;
import com.example.medihealth.models.UserModel;
import com.example.medihealth.utils.AndroidUtil;
import com.example.medihealth.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Infor_Appoitment_Activity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextSymptom;
    ImageView iconOne,iconTwo,iconThree,iconFour,iconFive;
    View lineOne,lineTwo,lineThree,lineFour;
    private ImageButton btnDateDialog, btnSpecialist, btnBack;
    RelativeLayout  btnInforDoctor, btnEnter,blockLoadTimeList, blockNotify,formCurrentUser,tick,
    btnAdd, blockIconOne, blockIconTwo, blockIconThree,blockIconFour,blockIconFive;
    ProgressBar progressBarLoadDoctors;
    private TextView editTextDate, editTextSpecialist, textViewName,btnDetailCurrentUser,nameCurrentUser;
    RecyclerView recyclerView, recyclerViewTime, recyclerViewRelative;
    DoctorAdapter adapterModel;
    TimeAdapter timeAdapter;
    RelativeAdapter relativeAdapter;
    SharedPreferences sharedPreferences;
    private int indexDoctorSelected = 0;
    private int indexPersonSelected = -1;
    private String timeSelected = "";
    UserModel userModel;
    int color;
    int colorDefault ;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                userModel = task.getResult().toObject(UserModel.class);
            }
            else {
                Log.e("ERROR","Lỗi kết nối mạng");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_appointment);
        color = ContextCompat.getColor(this, R.color.white);
        colorDefault = ContextCompat.getColor(this, R.color.icon_notselect);
        sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
        initReference();
        getDataDoctors();
        getDataRelative();
        setViewFormCurrentUser();
        setOnclick();
    }

    private void setUpListTime(int indexDoctorSelected, String date) {
        Doctor doctor = null;
        if (adapterModel != null && adapterModel.getItemCount() > indexDoctorSelected) {
            doctor = adapterModel.getItem(indexDoctorSelected);
        }
        if (doctor != null) {
            String doctorId = doctor.getDoctorId();
            FirebaseUtil.getAppointmentCollectionReference()
                    .whereEqualTo("doctor.doctorId", doctorId)
                    .whereEqualTo("appointmentDate", date)
                    .whereIn("stateAppointment", Arrays.asList(0, 1))
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<String> timeList = new ArrayList<>();
                            List<String> results = new ArrayList<>();
                            timeList.add("07:30");timeList.add("08:00");timeList.add("08:30");
                            timeList.add("09:00");timeList.add("09:30");timeList.add("10:00");
                            timeList.add("10:30");timeList.add("11:00");timeList.add("13:30");
                            timeList.add("14:00");timeList.add("14:30");timeList.add("15:00");
                            timeList.add("15:30");timeList.add("16:00");timeList.add("16:30");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Appointment appointment = document.toObject(Appointment.class);
                                timeList.remove(appointment.getTime());
                            }
                            if (date.equals(getCurrentDate())){
                                for (String time : timeList){
                                    if (time.compareTo(AndroidUtil.currentTime()) > 0){
                                        results.add(time);
                                    }
                                }
                                updateUI(results);
                            }
                            else updateUI(timeList);
                        } else {
                            Log.e("TAG", "Error getting documents: ", task.getException());
                        }
                    });
        }
    }


    private void updateUI(List<String> timeList) {
        if (timeList.isEmpty()) {
            blockLoadTimeList.setVisibility(View.INVISIBLE);
            recyclerViewTime.setVisibility(View.INVISIBLE);
            blockNotify.setVisibility(View.VISIBLE);
        } else {
            blockNotify.setVisibility(View.GONE);
        }
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 5);
        timeAdapter = new TimeAdapter(getApplicationContext(), timeList, new TimeAdapter.ITimeViewHolder() {
            @Override
            public void onClickItem(int position) {
                setColorBlockThree();
                timeSelected = timeList.get(position);
            }
        });
        recyclerViewTime.setLayoutManager(layoutManager);
        recyclerViewTime.setAdapter(timeAdapter);
    }

    private void getDataDoctors() {
        progressBarLoadDoctors.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBarLoadDoctors.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                setListDoctors();
            }
        }, 1000);
    }

    private void setListDoctors() {
        String specialistInput = editTextSpecialist.getText().toString().trim();
        Query query = null;
        if(specialistInput.equals("Chưa xác định")){
            query = FirebaseUtil.allDoctorCollectionReference();
        }
        else {
            query = FirebaseUtil.allDoctorCollectionReference().where(Filter.or(
                    Filter.equalTo("specialist","Chưa xác định"),
                    Filter.equalTo("specialist",specialistInput)
            ));
        }

        FirestoreRecyclerOptions<Doctor> options = new FirestoreRecyclerOptions.Builder<Doctor>()
                .setQuery(query,Doctor.class).build();
        adapterModel = new DoctorAdapter(options, getApplicationContext(), new DoctorAdapter.IDoctorViewHolder() {
            @Override
            public void onClickItem(int positon) {
                setColorBlockTwo();
                if (positon == indexDoctorSelected)return;
                indexDoctorSelected = positon;
                setTextNameDoctor(indexDoctorSelected);
                refreshOnclickDoctorOrDate();
                showTimeList();
            }
            @Override
            public void onDataLoaded(int size) {
                setTextNameDoctor(indexDoctorSelected);
                blockNotify.setVisibility(View.GONE);
                showTimeList();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(adapterModel);
        adapterModel.startListening();
    }

    private void setListDoctorsWithStateOnReSume() {
        String specialistInput = editTextSpecialist.getText().toString().trim();
        Query query = null;
        if(specialistInput.equals("Chưa xác định")){
            query = FirebaseUtil.allDoctorCollectionReference();
        }
        else {
            query = FirebaseUtil.allDoctorCollectionReference().where(Filter.or(
                    Filter.equalTo("specialist","Chưa xác định"),
                    Filter.equalTo("specialist",specialistInput)
            ));
        }

        FirestoreRecyclerOptions<Doctor> options = new FirestoreRecyclerOptions.Builder<Doctor>()
                .setQuery(query,Doctor.class).build();
        adapterModel = new DoctorAdapter(options, getApplicationContext(), new DoctorAdapter.IDoctorViewHolder() {
            @Override
            public void onClickItem(int positon) {
                if (positon == indexDoctorSelected)return;
                indexDoctorSelected = positon;
                setTextNameDoctor(indexDoctorSelected);
                refreshOnclickDoctorOrDate();
                showTimeList();
            }
            @Override
            public void onDataLoaded(int size) {
                setTextNameDoctor(indexDoctorSelected);
                blockNotify.setVisibility(View.GONE);
                showTimeListWithStateOnReSume();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(adapterModel);
        adapterModel.startListening();
    }

    private void showTimeListWithStateOnReSume() {
        if (timeAdapter != null) {
            timeAdapter.clearData();
            adapterModel.notifyDataSetChanged();
        }
        setUpListTime(indexDoctorSelected, editTextDate.getText().toString());
    }

    private void showTimeList(){
        if (timeAdapter != null) {
            timeAdapter.clearData();
            adapterModel.notifyDataSetChanged();
        }
        blockLoadTimeList.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                blockLoadTimeList.setVisibility(View.GONE);
                recyclerViewTime.setVisibility(View.VISIBLE);
                setUpListTime(indexDoctorSelected, editTextDate.getText().toString());
            }
        }, 700);
    }
    private void setViewFormCurrentUser() {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                UserModel currentUser = task.getResult().toObject(UserModel.class);
                if (currentUser != null){
                    nameCurrentUser.setText(getNameFromFullName(currentUser.getFullName()));
                }
            }
            else {
                Log.e("ERROR","Lỗi kết nối");
            }
        });
        setBackgroundItem(formCurrentUser);
    }

    private void getDataRelative() {
        String currentUserId = FirebaseUtil.currentUserId();
        Query query = FirebaseUtil.getRelativeCollectionReference()
                .whereEqualTo("userId",currentUserId);

        FirestoreRecyclerOptions<Relative> options = new FirestoreRecyclerOptions.Builder<Relative>()
                .setQuery(query,Relative.class).build();
        relativeAdapter = new RelativeAdapter(options, getApplicationContext(), new RelativeAdapter.IRelativeViewHolder() {
            @Override
            public void onClickItem(int positon) {
                setColorBlockFour();
                setBackgroundNotSelectedItem(formCurrentUser);
                tick.setVisibility(View.GONE);
                indexPersonSelected = positon;
            }
            @Override
            public void onDataLoaded(int size) {

            }
        });
        recyclerViewRelative.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        recyclerViewRelative.setAdapter(relativeAdapter);
        relativeAdapter.startListening();
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
        formCurrentUser.setOnClickListener(this);
        btnDetailCurrentUser.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void initReference() {
        editTextDate = findViewById(R.id.date);
        editTextDate.setText(getCurrentDate());
        editTextSpecialist = findViewById(R.id.specialist);
        editTextSymptom = findViewById(R.id.symptom);
        btnDateDialog = findViewById(R.id.btnDateDialog);
        btnInforDoctor = findViewById(R.id.infor_detail_doctor);
        btnSpecialist = findViewById(R.id.btnSpecialist);
        btnEnter = findViewById(R.id.enterBook);
        textViewName = findViewById(R.id.nameDoctor);
        blockNotify = findViewById(R.id.notify);
        progressBarLoadDoctors = findViewById(R.id.loadListDoctors);
        blockLoadTimeList = findViewById(R.id.blockLoadTimeList);
        recyclerView = findViewById(R.id.doctorList);
        recyclerViewTime = findViewById(R.id.listTime);
        btnBack = findViewById(R.id.back_btn);
        recyclerViewRelative = findViewById(R.id.listPerson);
        formCurrentUser = findViewById(R.id.form_currentUser);
        nameCurrentUser = findViewById(R.id.name);
        tick = findViewById(R.id.tick);
        btnDetailCurrentUser = findViewById(R.id.btn_Detail_currentUser);
        btnAdd = findViewById(R.id.form_add);
        blockIconOne = findViewById(R.id.block_icon_one);
        blockIconTwo = findViewById(R.id.block_icon_two);
        blockIconThree= findViewById(R.id.block_icon_three);
        blockIconFour = findViewById(R.id.block_icon_four);
        blockIconFive = findViewById(R.id.block_icon_five);
        lineOne = findViewById(R.id.line_one);
        lineTwo = findViewById(R.id.line_two);
        lineThree = findViewById(R.id.line_three);
        lineFour = findViewById(R.id.line_four);
        iconOne = findViewById(R.id.icon_one);
        iconTwo = findViewById(R.id.icon_two);
        iconThree = findViewById(R.id.icon_three);
        iconFour = findViewById(R.id.icon_four);
        iconFive= findViewById(R.id.icon_five);
    }

    private String getCurrentDate() {
        final Calendar calendar = Calendar.getInstance();
        int year, month, day;
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String monthStr = String.valueOf(month),dayStr = String.valueOf(day);

        if (month+1 < 10) monthStr = "0" + (month + 1);
        if (day < 10) dayStr = "0" + day;
        String result = dayStr+"/"+monthStr+"/"+year;
        return result;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.date || v.getId() == R.id.btnDateDialog){
            showDatePicker(Gravity.CENTER);
        }
        else if (v.getId() == R.id.specialist || v.getId() == R.id.btnSpecialist){
            showDialogSpecialist(Gravity.CENTER);
        }
        else if (v.getId() == R.id.back_btn){
            Intent intent = new Intent(Infor_Appoitment_Activity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.infor_detail_doctor){
            if (indexDoctorSelected == -1){
                CustomToast.showToast(getApplicationContext(),"Chưa chọn bác sĩ",Toast.LENGTH_SHORT);
                return;
            }
            getDetailDoctorAndPassIntent(indexDoctorSelected);
        }
        else if (v.getId() == R.id.enterBook){
            if (!validation()) return;
            passAppointmentAsIntent();
        }
        else if (v.getId() == R.id.form_currentUser){
            setBackgroundItem(formCurrentUser);
            tick.setVisibility(View.VISIBLE);
            relativeAdapter.clearBackgroundItems();
            indexPersonSelected = -1;
        }
        else if (v.getId() == R.id.form_add){
            Intent intent = new Intent(this, AddRelative.class);
            startActivity(intent);
        }
    }

    private void showDialogSpecialist(int center) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_specialist);
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

        RelativeLayout btnSpecialistAll,btnSpecialist2,btnSpecialist3, btnSpecialist4, btnSpecialist5,
                btnSpecialist6,btnSpecialist7,btnSpecialist8 ;
        TextView textSpecialistAll,textSpecialist2,textSpecialist3,textSpecialist4,textSpecialist5,
                textSpecialist6,textSpecialist7,textSpecialist8;
        btnSpecialistAll = dialog.findViewById(R.id.specialist_all);btnSpecialist2 = dialog.findViewById(R.id.specialist2);
        btnSpecialist3 = dialog.findViewById(R.id.specialist3);btnSpecialist4 = dialog.findViewById(R.id.specialist4);
        btnSpecialist5 = dialog.findViewById(R.id.specialist5);btnSpecialist6 = dialog.findViewById(R.id.specialist6);
        btnSpecialist7 = dialog.findViewById(R.id.specialist7);btnSpecialist8 = dialog.findViewById(R.id.specialist8);
        textSpecialistAll = dialog.findViewById(R.id.text_specialistALl);textSpecialist2 = dialog.findViewById(R.id.text_specialist2);
        textSpecialist3 = dialog.findViewById(R.id.text_specialist3);textSpecialist4 = dialog.findViewById(R.id.text_specialist4);
        textSpecialist5 = dialog.findViewById(R.id.text_specialist5);textSpecialist6 = dialog.findViewById(R.id.text_specialist6);
        textSpecialist7 = dialog.findViewById(R.id.text_specialist7);textSpecialist8 = dialog.findViewById(R.id.text_specialist8);
        btnSpecialistAll.setOnClickListener(this);btnSpecialist2.setOnClickListener(this);btnSpecialist3.setOnClickListener(this);
        btnSpecialist4.setOnClickListener(this);btnSpecialist5.setOnClickListener(this);btnSpecialist6.setOnClickListener(this);
        btnSpecialist7.setOnClickListener(this);btnSpecialist8.setOnClickListener(this);
        btnSpecialistAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorBlockOne();
                refreshColorBlockTwo();
                editTextSpecialist.setText(textSpecialistAll.getText().toString());
                dialog.dismiss();
                refreshOnclickSpecialist();
                getDataDoctors();
            }
        });
        btnSpecialist2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorBlockOne();
                refreshColorBlockTwo();
                editTextSpecialist.setText(textSpecialist2.getText().toString());
                dialog.dismiss();
                refreshOnclickSpecialist();
                getDataDoctors();
            }
        });
        btnSpecialist3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorBlockOne();
                refreshColorBlockTwo();
                editTextSpecialist.setText(textSpecialist3.getText().toString());
                dialog.dismiss();
                refreshOnclickSpecialist();
                getDataDoctors();
            }
        });
        btnSpecialist4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorBlockOne();
                refreshColorBlockTwo();
                editTextSpecialist.setText(textSpecialist4.getText().toString());
                dialog.dismiss();
                refreshOnclickSpecialist();
                getDataDoctors();
            }
        });
        btnSpecialist5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorBlockOne();
                refreshColorBlockTwo();
                editTextSpecialist.setText(textSpecialist5.getText().toString());
                dialog.dismiss();
                refreshOnclickSpecialist();
                getDataDoctors();
            }
        });
        btnSpecialist6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorBlockOne();
                refreshColorBlockTwo();
                editTextSpecialist.setText(textSpecialist6.getText().toString());
                dialog.dismiss();
                refreshOnclickSpecialist();
                getDataDoctors();
            }
        });
        btnSpecialist7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorBlockOne();
                refreshColorBlockTwo();
                editTextSpecialist.setText(textSpecialist7.getText().toString());
                dialog.dismiss();
                refreshOnclickSpecialist();
                getDataDoctors();
            }
        });
        btnSpecialist8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorBlockOne();
                refreshColorBlockTwo();
                editTextSpecialist.setText(textSpecialist8.getText().toString());
                dialog.dismiss();
                refreshOnclickSpecialist();
                getDataDoctors();
            }
        });
        dialog.show();
    }

    private void passAppointmentAsIntent() {
        Doctor doctor = null;
        if (adapterModel != null){
            doctor = adapterModel.getItem(indexDoctorSelected);
        }
        else CustomToast.showToast(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT);
        if (doctor != null){
            AppointmentDTO appointmentDTO = null;
            AppointmentConfirm appointmentConfirm = null;
            Relative relative = null;
            if (indexPersonSelected == -1){
                appointmentDTO = new AppointmentDTO(userModel.getUserId(),null,doctor.getDoctorId(),
                        editTextSpecialist.getText().toString(),timeSelected,getCurrentDate(),
                        editTextDate.getText().toString(),editTextSymptom.getText().toString());
                appointmentConfirm = new AppointmentConfirm(userModel.getFullName(),"Tôi",userModel.getPhoneNumber(),userModel.getGender(),
                        userModel.getBirth(),editTextSpecialist.getText().toString(),doctor.getFullName(),doctor.getPrice(),
                        timeSelected,editTextDate.getText().toString(),editTextSymptom.getText().toString());
            }
            else {
                if (relativeAdapter != null){
                    relative = relativeAdapter.getItem(indexPersonSelected);
                }
                if (relative != null){
                    appointmentDTO = new AppointmentDTO(null,relative.getPhoneNumber(),doctor.getDoctorId(),
                            editTextSpecialist.getText().toString(),timeSelected,getCurrentDate(),
                            editTextDate.getText().toString(),editTextSymptom.getText().toString());
                    appointmentConfirm = new AppointmentConfirm(relative.getFullName(),relative.getRelationship(),relative.getPhoneNumber(),relative.getGender(),
                            relative.getBirth(),editTextSpecialist.getText().toString(),doctor.getFullName(),doctor.getPrice(),
                            timeSelected,editTextDate.getText().toString(),editTextSymptom.getText().toString());
                } else {
                    appointmentDTO = null;
                }
            }
            if (appointmentDTO != null){
                Intent intent = new Intent(getApplicationContext(), ConfirmAppointment.class);
                AndroidUtil.passAppoitmentDTOModelAsIntent(intent,appointmentDTO);
                AndroidUtil.passAppoitmentConfirmModelAsIntent(intent,appointmentConfirm);
                startActivity(intent);
            }
        }
        else CustomToast.showToast(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT);
    }

    private boolean validation() {
        if (timeSelected.equals("")){
            CustomToast.showToast(getApplicationContext(),"Chọn giờ khám",Toast.LENGTH_SHORT);
            return false;
        }
        if (editTextSymptom.getText().toString().trim().equals("")){
            CustomToast.showToast(getApplicationContext(),"Nhập triệu chứng",Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }
    private void setTextNameDoctor(int indexDoctorSelected){
        if (adapterModel == null || adapterModel.getItemCount() == 0) {
            return;
        }
        String doctorName = adapterModel.getItem(indexDoctorSelected).getFullName();
        textViewName.setText(doctorName);
    }
    private void getDetailDoctorAndPassIntent(int indexDoctorSelected) {
        Doctor doctor = null;
        if (adapterModel != null){
            doctor = adapterModel.getItem(indexDoctorSelected);
        }
        if (doctor != null){
            Intent intent = new Intent(this, DoctorDetail.class);
            AndroidUtil.passDoctorModelAsIntent(intent,doctor);
            startActivity(intent);
        }
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
        String dayOfMonthStr = String.valueOf(dayOfMonth),
                monthOfYearStr = String.valueOf(monthOfYear+1);
        if (dayOfMonth < 10) dayOfMonthStr = "0" + dayOfMonth;
        if (monthOfYear+1 < 10) monthOfYearStr = "0" + (monthOfYear+1);
        String selectedDate = dayOfMonthStr + "/" + monthOfYearStr + "/" + year;
        if (reverseDateString(selectedDate).compareTo(reverseDateString(getCurrentDate())) < 0){
            CustomToast.showToast(getApplicationContext(),"Ngày không hợp lệ",Toast.LENGTH_SHORT);
            return;
        }
        if (selectedDate.equals(editTextDate.getText().toString()))return;
        editTextDate.setText(selectedDate);
        setIndexSelectedSharedPreferences("indexTimeSelected",-1);
        refreshOnclickDoctorOrDate();
        showTimeList();
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
        if (adapterModel != null){
            setListDoctorsWithStateOnReSume();
        }
    }
    private void setIndexSelectedSharedPreferences(String key, int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private String reverseDateString(String dateString) {
        String[] parts = dateString.split("/");
        return parts[2] + "/" + parts[1] + "/" + parts[0];
    }
    private void refreshOnclickDoctorOrDate(){
        blockNotify.setVisibility(View.GONE);
        refreshColorBlockThree();
        timeSelected = "";
        setIndexSelectedSharedPreferences("indexTimeSelected",-1);
    }

    private void refreshOnclickSpecialist(){
        recyclerView.setVisibility(View.GONE);
        blockNotify.setVisibility(View.INVISIBLE);
        recyclerViewTime.setVisibility(View.INVISIBLE);
        blockLoadTimeList.setVisibility(View.VISIBLE);
        indexDoctorSelected = 0;
        refreshColorBlockThree();
        timeSelected = "";
        textViewName.setText("");
        setIndexSelectedSharedPreferences("indexDoctorSelected",0);
        setIndexSelectedSharedPreferences("indexTimeSelected",-1);
    }

    private void setBackgroundNotSelectedItem(RelativeLayout formRelative) {
        Drawable selectedBackground = ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_form_person_notselected);
        formRelative.setBackground(selectedBackground);
    }

    private void setBackgroundItem(RelativeLayout formRelative) {
        Drawable selectedBackground = ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_form_person_selected);
        formRelative.setBackground(selectedBackground);
    }
    private String getNameFromFullName(String fullName){
        String[]str = fullName.split(" ");
        return str[str.length-1];
    }
    private void setColorBlockOne(){
        iconOne.setBackgroundTintList(ColorStateList.valueOf(color));
        blockIconOne.setBackgroundResource(R.drawable.bg_icon_selected);
    }
    private void refreshColorBlockThree(){
        lineTwo.setBackgroundTintList(ColorStateList.valueOf(colorDefault));
        iconThree.setBackgroundTintList(ColorStateList.valueOf(colorDefault));
        blockIconThree.setBackgroundResource(R.drawable.bg_icon_notselect);
    }
    private void setColorBlockTwo(){
        setColorBlockOne();
        lineOne.setBackgroundTintList(ColorStateList.valueOf(color));
        iconTwo.setBackgroundTintList(ColorStateList.valueOf(color));
        blockIconTwo.setBackgroundResource(R.drawable.bg_icon_selected);
    }
    private void refreshColorBlockTwo(){
        lineOne.setBackgroundTintList(ColorStateList.valueOf(colorDefault));
        iconTwo.setBackgroundTintList(ColorStateList.valueOf(colorDefault));
        blockIconTwo.setBackgroundResource(R.drawable.bg_icon_notselect);
    }
    private void setColorBlockThree(){
        setColorBlockTwo();
        lineTwo.setBackgroundTintList(ColorStateList.valueOf(color));
        iconThree.setBackgroundTintList(ColorStateList.valueOf(color));
        blockIconThree.setBackgroundResource(R.drawable.bg_icon_selected);
    }
    private void setColorBlockFour(){
        setColorBlockOne();
        setColorBlockTwo();
        lineThree.setBackgroundTintList(ColorStateList.valueOf(color));
        iconFour.setBackgroundTintList(ColorStateList.valueOf(color));
        blockIconFour.setBackgroundResource(R.drawable.bg_icon_selected);
    }
}