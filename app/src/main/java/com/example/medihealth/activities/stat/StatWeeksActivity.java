package com.example.medihealth.activities.stat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.adapters.stat.StatWeekApdapter;
import com.example.medihealth.models.DrugUser;
import com.example.medihealth.models.Prescription;
import com.example.medihealth.retrofitcustom.RetrofitClient;
import com.example.medihealth.apiservices.DrugUserService;
import com.example.medihealth.apiservices.PrescriptionStatService;
import com.example.medihealth.utils.stat.WeekInfo;
import com.example.medihealth.utils.stat.WeekPickerDialog;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatWeeksActivity extends AppCompatActivity {
    private Spinner spinnerDrugUSer;
    private EditText editTextWeek;
    private ImageView imgBack;
    private RecyclerView statWeekRecyclerView;
    private StatWeekApdapter statWeekApdapter;
    private List<DrugUser> drugUserList = new ArrayList<>();
    private Long duid;
    private LocalDate startWeek;
    private LocalDate endWeek;
    private List<Prescription> prescriptions;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stat_weeks);

        imgBack = findViewById(R.id.image_back);
        spinnerDrugUSer = findViewById(R.id.spinnerDrugUser);
        editTextWeek = findViewById(R.id.editTextWeek);
        statWeekRecyclerView = findViewById(R.id.statWeekRecyclerView);
        statWeekRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Get Current Week
        WeekInfo currentWeek = getCurrentWeek();
        editTextWeek.setText(currentWeek.getWeekLabel());
        startWeek = currentWeek.getStartDate();
        endWeek = currentWeek.getEndDate();

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
                    ArrayAdapter<DrugUser> adapter = new ArrayAdapter<>(StatWeeksActivity.this, android.R.layout.simple_spinner_item, drugUserList);
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
                callAPItoGetPrescriptionStatWeek();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //Show List Week
        editTextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWeekPickerDialog();
            }
        });

        if(duid != null){
            //Default call API to Get PrescriptionStat and show
            callAPItoGetPrescriptionStatWeek();
        }

        //Event Back
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StatWeeksActivity.this, StatHomeActivity.class));
            }
        });
    }

    private void showWeekPickerDialog() {
        WeekPickerDialog weekPickerDialog = new WeekPickerDialog(this, new WeekPickerDialog.OnWeekSetListener() {
            @Override
            public void onWeekSet(WeekInfo selectedWeek) {
                editTextWeek.setText(selectedWeek.getWeekLabel());
                startWeek = selectedWeek.getStartDate();
                endWeek = selectedWeek.getEndDate();
                //Call API to PrescriptionStat and show
                callAPItoGetPrescriptionStatWeek();
            }
        });

        weekPickerDialog.show();
    }
    private void updateRecyclerViewAdapter() {
        statWeekApdapter = new StatWeekApdapter(prescriptions, startWeek);
        statWeekRecyclerView.setAdapter(statWeekApdapter);
    }
    private void callAPItoGetPrescriptionStatWeek(){
        prescriptions = new ArrayList<>();
        PrescriptionStatService prescriptionStatService = RetrofitClient.createService(PrescriptionStatService.class);
        prescriptionStatService.getPrescriptionStatWeek(duid, startWeek, endWeek).enqueue(new Callback<List<Prescription>>() {
            @Override
            public void onResponse(Call<List<Prescription>> call, Response<List<Prescription>> response) {
                if(response.isSuccessful() && !response.body().isEmpty() && response.body() != null){
                    Log.d("API_Response", "Data received from API: " + response.body().toString());
                    prescriptions.addAll(response.body());
                }else {
                    Log.e("API_Error", "Response body is null");
                }
                //Update StatWeekRecyclerView
                updateRecyclerViewAdapter();
            }

            @Override
            public void onFailure(Call<List<Prescription>> call, Throwable t) {
                Log.e("API_Error", "Failed to connect to API: " + t.getMessage(), t);
            }
        });
    }
    private WeekInfo getCurrentWeek() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.with(DayOfWeek.MONDAY);
        LocalDate endDate = currentDate.with(DayOfWeek.SUNDAY);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());

        String weekLabel = "Từ " + startDate.format(formatter) + " đến " + endDate.format(formatter);
        return new WeekInfo(weekLabel, startDate, endDate);
    }
}





