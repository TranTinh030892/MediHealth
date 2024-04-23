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
import com.example.medihealth.adapters.stat.StatWeekApdapter;
import com.example.medihealth.models.DrugUser;
import com.example.medihealth.models.Prescription;
import com.example.medihealth.retrofitcustom.RetrofitClient;
import com.example.medihealth.apiservices.DrugUserService;
import com.example.medihealth.apiservices.PrescriptionStatService;
import com.example.medihealth.utils.stat.WeekInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatWeeksActivity extends AppCompatActivity {
    private EditText editTextSelectDU;
    private EditText editTextWeek;
    private ImageView imgBack;
    private RecyclerView statWeekRecyclerView;
    private StatWeekApdapter statWeekApdapter;
    private List<DrugUser> drugUserList = new ArrayList<>();
    private Long duid;
    private LocalDate startWeek;
    private LocalDate endWeek;
    private List<WeekInfo> weekInfoList;
    private List<Prescription> prescriptions;
    private int selectedItemPositionDU;
    private int selectedItemPositionWeek;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stat_weeks);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser account = mAuth.getCurrentUser();
        imgBack = findViewById(R.id.image_back);
        editTextSelectDU = findViewById(R.id.editTextSelectDU);
        editTextSelectDU.setBackgroundResource(R.drawable.background_ic_chervon_down);
        editTextWeek = findViewById(R.id.editTextWeek);
        statWeekRecyclerView = findViewById(R.id.statWeekRecyclerView);
        statWeekRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Get Current Week
        WeekInfo currentWeek = getCurrentWeek();
        editTextWeek.setText(currentWeek.getWeekLabel());
        startWeek = currentWeek.getStartDate();
        endWeek = currentWeek.getEndDate();
        weekInfoList = generateWeekList();
        for(int i = 0; i < weekInfoList.size(); i++){
            if(weekInfoList.get(i).getStartDate().equals(currentWeek.getStartDate())){
                selectedItemPositionWeek = i;
                break;
            }
        }

        //Get ListDrugUser
        DrugUserService drugUserService = RetrofitClient.createService(DrugUserService.class);
        drugUserService.getDrugUserofUser(account.getUid()).enqueue(new Callback<List<DrugUser>>() {
            @Override
            public void onResponse(Call<List<DrugUser>> call, Response<List<DrugUser>> response) {
                if(response.isSuccessful() && response.body() != null && !response.body().isEmpty()){
                    Log.d("API_Response", "Data received from API: " + response.body().toString());

                    drugUserList.addAll(response.body());

                    //Default DrugUser
                    duid = drugUserList.get(0).getId();
                    editTextSelectDU.setText(drugUserList.get(0).toString());
                    selectedItemPositionDU = 0;
                    callAPItoGetPrescriptionStatWeek();
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
        editTextSelectDU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = getLayoutInflater().inflate(R.layout.menu_custom, null);

                ListView listView = popupView.findViewById(R.id.listView);
                ArrayAdapter<DrugUser> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, drugUserList);
                listView.setAdapter(adapter);

                int itemHeight = (int) getResources().getDisplayMetrics().density * 68;
                int maxHeight = itemHeight * 4 ;
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
                        callAPItoGetPrescriptionStatWeek();
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

        //Show List Week
        editTextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = getLayoutInflater().inflate(R.layout.menu_custom, null);

                ListView listView = popupView.findViewById(R.id.listView);
                ArrayAdapter<WeekInfo> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, weekInfoList);
                listView.setAdapter(adapter);

                int itemHeight = (int) getResources().getDisplayMetrics().density * 68;
                int maxHeight = itemHeight * 4 ;
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = maxHeight;
                listView.setLayoutParams(params);

                final PopupWindow popupWindow = new PopupWindow(popupView, editTextSelectDU.getWidth(), maxHeight, true);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        WeekInfo selectedWeekInfo = (WeekInfo) parent.getItemAtPosition(position);
                        startWeek = selectedWeekInfo.getStartDate();
                        endWeek = selectedWeekInfo.getEndDate();
                        editTextWeek.setText(selectedWeekInfo.toString());
                        selectedItemPositionWeek = position;
                        callAPItoGetPrescriptionStatWeek();
                        popupWindow.dismiss();
                    }
                });

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        editTextWeek.setBackgroundResource(R.drawable.background_ic_chervon_down);
                    }
                });

                listView.setSelection(selectedItemPositionWeek);
                popupWindow.showAsDropDown(editTextWeek);
                editTextWeek.setBackgroundResource(R.drawable.background_ic_chervon_up);
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
    private List<WeekInfo> generateWeekList() {
        List<WeekInfo> weeks = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 3);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.YEAR, 2030);
        endCalendar.set(Calendar.MONTH, Calendar.DECEMBER);
        endCalendar.set(Calendar.DAY_OF_MONTH, 31);

        while (calendar.before(endCalendar)) {
            String startOfWeek = sdf.format(calendar.getTime());
            LocalDate startDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            calendar.add(Calendar.DAY_OF_YEAR, 6);
            String endOfWeek = sdf.format(calendar.getTime());
            LocalDate endDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            String weekLabel = "Từ " + startOfWeek + " đến " + endOfWeek;
            weeks.add(new WeekInfo(weekLabel, startDate, endDate));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        return weeks;
    }
}





