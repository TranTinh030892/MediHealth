package com.example.medihealth.activities.stat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.adapters.stat.ScheduleStatMonthApdapter;
import com.example.medihealth.apiservices.DrugUserService;
import com.example.medihealth.apiservices.PrescriptionStatService;
import com.example.medihealth.models.DrugUser;
import com.example.medihealth.models.PrescriptionStat;
import com.example.medihealth.retrofitcustom.RetrofitClient;
import com.example.medihealth.utils.stat.MonthInfo;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatMonthActivity extends AppCompatActivity {
    private EditText editTextSelectDU;
    private EditText editTextMonth;
    private ImageView imgBack;
    private RecyclerView statMonthRecyclerView;
    private ScheduleStatMonthApdapter scheduleStatMonthApdapter;
    private List<DrugUser> drugUserList = new ArrayList<>();
    private Long duid;
    private LocalDate dayOfMonth;
    private List<MonthInfo> monthInfoList;
    private List<PrescriptionStat> prescriptionStats;
    private int selectedItemPositionDU;
    private int selectedItemPositionMonth;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stat_month);

        imgBack = findViewById(R.id.image_back);
        editTextSelectDU = findViewById(R.id.editTextSelectDU);
        editTextSelectDU.setBackgroundResource(R.drawable.background_ic_chervon_down);
        editTextMonth = findViewById(R.id.editTextMonth);
        statMonthRecyclerView = findViewById(R.id.statMonthRecyclerView);
        statMonthRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Get Month Current
        MonthInfo currentMonth = getCurrentMonth();
        editTextMonth.setText(currentMonth.getMonthLabel());
        dayOfMonth = currentMonth.getDayofmonth();
        monthInfoList = generateMonthList();
        for (int i = 0; i < monthInfoList.size(); i++) {
            if (monthInfoList.get(i).getDayofmonth().equals(currentMonth.getDayofmonth())) {
                selectedItemPositionMonth = i;
                break;
            }
        }

        //Get ListDrugUser
        DrugUserService drugUserService = RetrofitClient.createService(DrugUserService.class);
        drugUserService.getDrugUserofUser(FirebaseAuth.getInstance().getCurrentUser().getUid()).enqueue(new Callback<List<DrugUser>>() {
            @Override
            public void onResponse(Call<List<DrugUser>> call, Response<List<DrugUser>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Log.d("API_Response", "Data received from API: " + response.body().toString());

                    drugUserList.addAll(response.body());

                    //Default DrugUser
                    duid = drugUserList.get(0).getId();
                    editTextSelectDU.setText(drugUserList.get(0).toString());
                    selectedItemPositionDU = 0;
                    callAPItoGetPrescriptionStatMonth();
                } else {
                    Log.e("API_Error", "Response body is empty or null");
                }
            }

            @Override
            public void onFailure(Call<List<DrugUser>> call, Throwable t) {
                Log.e("API_Error", "Failed to connect to API: " + t.getMessage(), t);
            }
        });

        //Event select DrugUser
        editTextSelectDU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = getLayoutInflater().inflate(R.layout.menu_custom, null);

                ListView listView = popupView.findViewById(R.id.listView);
                ArrayAdapter<DrugUser> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, drugUserList);
                listView.setAdapter(adapter);

                int itemHeight = (int) getResources().getDisplayMetrics().density * 68;
                int maxHeight = itemHeight * 4;
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = maxHeight;
                listView.setLayoutParams(params);

                final PopupWindow popupWindow = new PopupWindow(popupView, editTextSelectDU.getWidth(), maxHeight, true);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        DrugUser selectedDrugUser = (DrugUser) parent.getItemAtPosition(position);
                        duid = selectedDrugUser.getId();
                        editTextSelectDU.setText(selectedDrugUser.toString());
                        selectedItemPositionDU = position;
                        callAPItoGetPrescriptionStatMonth();
                        popupWindow.dismiss();
                    }
                });

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        editTextSelectDU.setBackgroundResource(R.drawable.background_ic_chervon_down);
                    }
                });

                listView.setSelection(selectedItemPositionDU);
                popupWindow.showAsDropDown(editTextSelectDU);
                editTextSelectDU.setBackgroundResource(R.drawable.background_ic_chervon_up);
            }
        });

        //Show List Month
        editTextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = getLayoutInflater().inflate(R.layout.menu_custom, null);

                ListView listView = popupView.findViewById(R.id.listView);
                ArrayAdapter<MonthInfo> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, monthInfoList);
                listView.setAdapter(adapter);

                int itemHeight = (int) getResources().getDisplayMetrics().density * 68;
                int maxHeight = itemHeight * 4;
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = maxHeight;
                listView.setLayoutParams(params);

                final PopupWindow popupWindow = new PopupWindow(popupView, editTextSelectDU.getWidth(), maxHeight, true);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        MonthInfo selectedMonthInfo = (MonthInfo) parent.getItemAtPosition(position);
                        dayOfMonth = selectedMonthInfo.getDayofmonth();
                        editTextMonth.setText(selectedMonthInfo.toString());
                        selectedItemPositionMonth = position;
                        callAPItoGetPrescriptionStatMonth();
                        popupWindow.dismiss();
                    }
                });

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        editTextMonth.setBackgroundResource(R.drawable.background_ic_chervon_down);
                    }
                });

                listView.setSelection(selectedItemPositionMonth);
                popupWindow.showAsDropDown(editTextMonth);
                editTextMonth.setBackgroundResource(R.drawable.background_ic_chervon_up);
            }
        });

        if (duid != null) {
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

    public void updateRecyclerViewAdapter() {
        scheduleStatMonthApdapter = new ScheduleStatMonthApdapter(prescriptionStats);
        statMonthRecyclerView.setAdapter(scheduleStatMonthApdapter);
    }

    private void callAPItoGetPrescriptionStatMonth() {
        prescriptionStats = new ArrayList<>();
        PrescriptionStatService prescriptionStatService = RetrofitClient.createService(PrescriptionStatService.class);
        prescriptionStatService.getPrescriptionStatMonth(duid, dayOfMonth).enqueue(new Callback<List<PrescriptionStat>>() {
            @Override
            public void onResponse(Call<List<PrescriptionStat>> call, Response<List<PrescriptionStat>> response) {
                if (response.isSuccessful() && !response.body().isEmpty() && response.body() != null) {
                    Log.d("API_Response", "Data received from API: " + response.body().toString());
                    prescriptionStats.addAll(response.body());
                } else {
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

    private MonthInfo getCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        String monthLabel = "Tháng " + (currentDate.getMonthValue() > 9 ? currentDate.getMonthValue() : "0" + currentDate.getMonthValue()) + "/" + currentDate.getYear();
        LocalDate dayOfMonth = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1);
        return new MonthInfo(monthLabel, dayOfMonth);
    }

    private List<MonthInfo> generateMonthList() {
        List<MonthInfo> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.YEAR, 2030);
        endCalendar.set(Calendar.MONTH, Calendar.DECEMBER);

        while (calendar.before(endCalendar)) {
            LocalDate dayOfMonth = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);
            String monthLabel = "Tháng " + (calendar.get(Calendar.MONTH) + 1 > 9 ? calendar.get(Calendar.MONTH) + 1 : "0" + (calendar.get(Calendar.MONTH) + 1)) + "/" + calendar.get(Calendar.YEAR);
            MonthInfo monthInfo = new MonthInfo(monthLabel, dayOfMonth);
            months.add(monthInfo);
            calendar.add(Calendar.MONTH, 1);
        }

        return months;
    }
}

