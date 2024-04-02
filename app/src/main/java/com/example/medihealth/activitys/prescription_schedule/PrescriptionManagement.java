package com.example.medihealth.activitys.prescription_schedule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.adapters.prescription_schedule.PrescriptionAdapter;
import com.example.medihealth.apiservices.PrescriptionService;
import com.example.medihealth.models.DrugUser;
import com.example.medihealth.models.Prescription;
import com.example.medihealth.models.ResponseObject;
import com.example.medihealth.retrofitcustom.LocalDateAdapter;
import com.example.medihealth.retrofitcustom.LocalTimeAdapter;
import com.example.medihealth.retrofitcustom.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescriptionManagement extends AppCompatActivity {

    TextView tvToolbar;
    ImageView btnBackToolbar;
    FloatingActionButton fabCreatePrescription;
    RecyclerView rcvListPrescription;
    List<Prescription> prescriptions = new ArrayList<>();
    PrescriptionAdapter prescriptionAdapter;
    static DrugUser drugUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_management);

        loadData();

        // Custom toolbar
        tvToolbar = findViewById(R.id.tv_toolbar);
        tvToolbar.setText("Danh sách đơn thuốc");
        btnBackToolbar = findViewById(R.id.btn_back_toolbar);
        btnBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fabCreatePrescription = findViewById(R.id.fab_create_prescription);
        fabCreatePrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrescriptionManagement.this, EnterNewPrescriptionItemsActivity.class);
                Prescription prescription = new Prescription();
                prescription.setDrugUser(drugUser);
                Bundle bundle = new Bundle();
                bundle.putSerializable("prescription", prescription);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        rcvListPrescription = findViewById(R.id.rcv_list_prescription);
        rcvListPrescription.setLayoutManager(new LinearLayoutManager(this));
        rcvListPrescription.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    fabCreatePrescription.hide();
                } else {
                    fabCreatePrescription.show();
                }
            }
        });
        prescriptionAdapter = new PrescriptionAdapter(prescriptions);
        prescriptionAdapter.setOnClickListener(new CustomOnClickListener<Prescription>() {
            @Override
            public void onClick(Prescription data) {
                Intent intent = new Intent(
                        PrescriptionManagement.this,
                        PrescriptionDetailManagement.class
                );
                Bundle bundle = new Bundle();
                bundle.putSerializable("prescription", data);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        prescriptionAdapter.setOnClickSwitchListener(new CustomOnClickListener<Prescription>() {
            @Override
            public void onClick(Prescription data) {
                savePrescription(data);
            }
        });

        rcvListPrescription.setAdapter(prescriptionAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void savePrescription(Prescription prescription) {
        PrescriptionService prescriptionService = RetrofitClient.createService(PrescriptionService.class);
        prescriptionService.editPrescription(prescription).enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(
                            PrescriptionManagement.this,
                            prescription.isActive() ? "Đã bật thông báo cho đơn thuốc" : "Đã tắt thông báo cho đơn thuốc",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Toast.makeText(
                            PrescriptionManagement.this,
                            response.body().getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                    prescription.setActive(!prescription.isActive());
                    prescriptionAdapter.notifyDataSetChanged();
                }
                SyncService.sync(PrescriptionManagement.this);
                Log.i("ACTIVE_PRESCRIPTION", response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("ACTIVE_PRESCRIPTION", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        drugUser = (DrugUser) bundle.get("drugUser");
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getData() {
        prescriptions.clear();
        prescriptionAdapter.notifyDataSetChanged();
        PrescriptionService prescriptionService = RetrofitClient.createService(PrescriptionService.class);
        prescriptionService.getAllByDrugUser(drugUser.getId()).enqueue(new Callback<ResponseObject>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                ResponseObject responseObject = response.body();
                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                            .create();
                    prescriptions.addAll(gson.fromJson(
                            new Gson().toJson(responseObject.getData()),
                            new TypeToken<List<Prescription>>() {
                            }.getType()
                    ));
                    prescriptionAdapter.notifyDataSetChanged();
                }
                Log.i("GET_PRESCRIPTIONS", response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("GET_PRESCRIPTIONS", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}
