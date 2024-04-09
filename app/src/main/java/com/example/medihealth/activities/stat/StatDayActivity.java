package com.example.medihealth.activities.stat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.adapters.stat.StatDayApdapter;
import com.example.medihealth.models.DrugUser;
import com.example.medihealth.models.Prescription;
import com.example.medihealth.retrofitcustom.RetrofitClient;
import com.example.medihealth.apiservices.DrugUserService;
import com.example.medihealth.apiservices.PrescriptionStatService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatDayActivity extends AppCompatActivity {
    private Spinner spinnerDrugUSer;
    private EditText editTextDate;
    private ImageView imgBack;
    private RecyclerView statDayRecyclerView;
    private StatDayApdapter statDayApdapter;
    private Calendar calendar;
    private List<DrugUser> drugUserList = new ArrayList<>();
    private Long duid;
    private LocalDate date;
    private List<Prescription> prescriptions;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stat_day);

        imgBack = findViewById(R.id.image_back);
        spinnerDrugUSer = findViewById(R.id.spinnerDrugUser);
        editTextDate = findViewById(R.id.editTextDate);
        calendar = Calendar.getInstance();

        statDayRecyclerView = findViewById(R.id.statDayRecyclerView);
        statDayRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Get day current
        String selectedDate = getDayCurrent();
        editTextDate.setText(selectedDate);
        date = LocalDate.now();

        //Get ListDrugUser
        DrugUserService drugUserService = RetrofitClient.createService(DrugUserService.class);
        drugUserService.getDrugUserofUser("12345").enqueue(new Callback<List<DrugUser>>() {
            @Override
            public void onResponse(Call<List<DrugUser>> call, Response<List<DrugUser>> response) {
                if(response.isSuccessful() && response.body() != null && !response.body().isEmpty()){
                    Log.d("API_Response", "Data received from API: " + response.body().toString());

                    drugUserList.addAll(response.body());

                    //Default DrugUser
                    duid = drugUserList.get(0).getId();

                    //Show DrugUser to select
                    ArrayAdapter<DrugUser> adapter = new ArrayAdapter<>(StatDayActivity.this, android.R.layout.simple_spinner_item, drugUserList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDrugUSer.setAdapter(adapter);
                }else {
                    Log.e("API_Error", "Response body is empty or null");
                }
            }

            @Override
            public void onFailure(Call<List<DrugUser>> call, Throwable t) {
                Log.e("API_Error", "Failed to connect to API: " + t.getMessage(), t);
            }
        });

        //Event select DrugUser
        spinnerDrugUSer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DrugUser selectedDrugUser = (DrugUser) parent.getItemAtPosition(position);
                duid = selectedDrugUser.getId();
                //Call API to PrescriptionStat and show
                callAPItoGetPrescriptionStatDay();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //Event show calendar
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        if(duid != null){
            //Default call API to Get PrescriptionStat and show
            callAPItoGetPrescriptionStatDay();
        }

        //Event Back
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StatDayActivity.this, StatHomeActivity.class));
            }
        });
    }

    private void updateRecyclerViewAdapter() {
        statDayApdapter = new StatDayApdapter(prescriptions);
        statDayRecyclerView.setAdapter(statDayApdapter);
    }

    private void callAPItoGetPrescriptionStatDay(){
        prescriptions = new ArrayList<>();
        PrescriptionStatService prescriptionStatService = RetrofitClient.createService(PrescriptionStatService.class);
        prescriptionStatService.getPrescriptionStatDay(duid, date).enqueue(new Callback<List<Prescription>>() {
            @Override
            public void onResponse(Call<List<Prescription>> call, Response<List<Prescription>> response) {
                if(response.isSuccessful() && !response.body().isEmpty() && response.body() != null){
                    Log.d("API_Response", "Data received from API: " + response.body().toString());
                    prescriptions.addAll(response.body());
                }else {
                    Log.e("API_Error", "Response body is null");
                }
                //Update StatDayRecyclerView
                updateRecyclerViewAdapter();
            }

            @Override
            public void onFailure(Call<List<Prescription>> call, Throwable t) {
                Log.e("API_Error", "Failed to connect to API: " + t.getMessage(), t);
            }
        });
    }


    private String getDayCurrent(){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return (dayOfMonth > 9 ? dayOfMonth : ("0"+dayOfMonth)) + "/" + (month > 8 ? (month + 1):("0"+(month+1))) + "/" + year;
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = (dayOfMonth > 9 ? dayOfMonth : ("0"+dayOfMonth)) + "/" + (month > 8 ? (month + 1):("0"+(month+1)))  + "/" + year;
                        editTextDate.setText(selectedDate);
                        date = LocalDate.of(year, month + 1, dayOfMonth);
                        //Call API to PrescriptionStat and show
                        callAPItoGetPrescriptionStatDay();
                    }
                },
                year, month, dayOfMonth
        );
        datePickerDialog.show();
    }
}
