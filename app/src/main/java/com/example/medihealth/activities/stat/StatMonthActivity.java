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
import com.example.medihealth.adapters.stat.ScheduleStatMonthApdapter;
import com.example.medihealth.models.DrugUser;
import com.example.medihealth.models.PrescriptionStat;
import com.example.medihealth.retrofitcustom.RetrofitClient;
import com.example.medihealth.apiservices.DrugUserService;
import com.example.medihealth.apiservices.PrescriptionStatService;
import com.example.medihealth.utils.stat.MonthInfo;
import com.example.medihealth.utils.stat.MonthPickerDialog;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatMonthActivity extends AppCompatActivity {
    private Spinner spinnerDrugUSer;
    private EditText editTextMonth;
    private ImageView imgBack;
    private RecyclerView statMonthRecyclerView;
    private ScheduleStatMonthApdapter scheduleStatMonthApdapter;
    private List<DrugUser> drugUserList = new ArrayList<>();
    private Long duid;
    private LocalDate dayOfMonth;
    private List<PrescriptionStat> prescriptionStats;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stat_month);

        imgBack = findViewById(R.id.image_back);
        spinnerDrugUSer = findViewById(R.id.spinnerDrugUser);
        editTextMonth = findViewById(R.id.editTextMonth);
        statMonthRecyclerView = findViewById(R.id.statMonthRecyclerView);
        statMonthRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Get Month Current
        MonthInfo currentMonth = getCurrentMonth();
        editTextMonth.setText(currentMonth.getMonthLabel());
        dayOfMonth = currentMonth.getDayofmonth();

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
                    ArrayAdapter<DrugUser> adapter = new ArrayAdapter<>(StatMonthActivity.this, android.R.layout.simple_spinner_item, drugUserList);
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
                callAPItoGetPrescriptionStatMonth();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //Event show list month
        editTextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMonthPickerDialog();
            }
        });

        if(duid != null){
            //Default call API to Get PrescriptionStat and show
            callAPItoGetPrescriptionStatMonth();
        }

        //Event back
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StatMonthActivity.this, StatHomeActivity.class));
            }
        });
    }

    public void updateRecyclerViewAdapter(){
        scheduleStatMonthApdapter = new ScheduleStatMonthApdapter(prescriptionStats);
        statMonthRecyclerView.setAdapter(scheduleStatMonthApdapter);
    }

    private void callAPItoGetPrescriptionStatMonth(){
        prescriptionStats = new ArrayList<>();
        PrescriptionStatService prescriptionStatService = RetrofitClient.createService(PrescriptionStatService.class);
        prescriptionStatService.getPrescriptionStatMonth(duid, dayOfMonth).enqueue(new Callback<List<PrescriptionStat>>() {
            @Override
            public void onResponse(Call<List<PrescriptionStat>> call, Response<List<PrescriptionStat>> response) {
                if(response.isSuccessful() && !response.body().isEmpty() && response.body() != null){
                    Log.d("API_Response", "Data received from API: " + response.body().toString());
                    prescriptionStats.addAll(response.body());
                }else {
                    Log.e("API_Error", "Response body is null");
                }
                //Update StatWeekRecyclerView
                updateRecyclerViewAdapter();
            }

            @Override
            public void onFailure(Call<List<PrescriptionStat>> call, Throwable t) {
                Log.e("API_Error", "Failed to connect to API: " + t.getMessage(), t);
            }
        });
    }

    private void showMonthPickerDialog() {
        MonthPickerDialog monthPickerDialog = new MonthPickerDialog(this, new MonthPickerDialog.OnMonthSetListener() {
            @Override
            public void onMonthSet(MonthInfo selectedMonth) {
                editTextMonth.setText(selectedMonth.getMonthLabel());
                dayOfMonth = selectedMonth.getDayofmonth();
                //Call API to PrescriptionStat and show
                callAPItoGetPrescriptionStatMonth();
            }
        });

        monthPickerDialog.show();
    }
    private MonthInfo getCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        String monthLabel = "Tháng " + (currentDate.getMonthValue() > 9 ? currentDate.getMonthValue() : "0" + currentDate.getMonthValue()) + "/" + currentDate.getYear();
        LocalDate dayOfMonth = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1);
        return new MonthInfo(monthLabel, dayOfMonth);
    }
}

