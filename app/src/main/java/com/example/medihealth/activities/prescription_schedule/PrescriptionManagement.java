package com.example.medihealth.activities.prescription_schedule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.medihealth.R;
import com.example.medihealth.adapters.prescription_schedule.PrescriptionAdapter;
import com.example.medihealth.apiservices.PrescriptionService;
import com.example.medihealth.models.DrugUser;
import com.example.medihealth.models.Prescription;
import com.example.medihealth.models.ResponseObject;
import com.example.medihealth.retrofitcustom.LocalDateAdapter;
import com.example.medihealth.retrofitcustom.LocalDateTimeAdapter;
import com.example.medihealth.retrofitcustom.LocalTimeAdapter;
import com.example.medihealth.retrofitcustom.RetrofitClient;
import com.example.medihealth.utils.FirebaseUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescriptionManagement extends AppCompatActivity {

    TextView tvToolbar;
    ImageView btnBackToolbar, btnQRScan;
    FloatingActionButton fabCreatePrescription;
    RecyclerView rcvListPrescription;
    List<Prescription> prescriptions = new ArrayList<>();
    PrescriptionAdapter prescriptionAdapter;
    static DrugUser drugUser;
    RelativeLayout defaultView;
    LinearLayout loadingView;
    SwipeRefreshLayout srlRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_management);

        loadData();

        // Custom toolbar
        tvToolbar = findViewById(R.id.tv_title);
        tvToolbar.setText("Danh sách đơn thuốc");
        btnBackToolbar = findViewById(R.id.btn_back);
        btnBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnQRScan = findViewById(R.id.btn_scan);
        btnQRScan.setOnClickListener((view) -> {
            openScanner();
        });

        srlRefresh = findViewById(R.id.srl_refresh);
        srlRefresh.setOnRefreshListener(() -> {
            getData();
            srlRefresh.setRefreshing(false);
        });

        fabCreatePrescription = findViewById(R.id.fab_create_prescription);
        fabCreatePrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewPrescription(new Prescription());
            }
        });

        defaultView = findViewById(R.id.default_view);
        loadingView = findViewById(R.id.loading_view);
        loadingView.setVisibility(View.VISIBLE);
        defaultView.setVisibility(View.GONE);

        rcvListPrescription = findViewById(R.id.rcv_list_prescription);
        rcvListPrescription.setVisibility(View.GONE);
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

    private void createNewPrescription(Prescription prescription) {
        prescription.setId(null);
        prescription.setDrugUser(drugUser);
        if (prescription.getPrescriptionItems() != null) {
            prescription.getPrescriptionItems().forEach(
                    (item) -> item.setId(null)
            );
        }
        if (prescription.getSchedules() != null) {
            prescription.getSchedules().forEach(
                    (schedule) -> schedule.setId(null)
            );
        }

        Intent intent = new Intent(PrescriptionManagement.this, EnterNewPrescriptionItemsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("prescription", prescription);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Nhận kết quả từ IntentIntegrator
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                // Nếu không có kết quả
                Toast.makeText(this, "Không tìm thấy mã QR", Toast.LENGTH_SHORT).show();
            } else {
                String qrCodeContent = result.getContents();
                Log.i("GET_DATA_SHARED", "ID: " + qrCodeContent);
                try {
                    Long id = Long.parseLong(qrCodeContent.trim());
                    getPrescriptionShared(id);
                } catch (NumberFormatException e) {
                    Log.e("GET_DATA_SHARED", "invalid id provided");
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void openScanner() {
        // Khởi tạo IntentIntegrator để quét mã QR
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false); // Cho phép quét cả hai hướng
        integrator.initiateScan(); // Bắt đầu quét
    }

    private void showDefaultView() {
        rcvListPrescription.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        defaultView.setVisibility(View.VISIBLE);
    }

    private void showListView() {
        loadingView.setVisibility(View.GONE);
        defaultView.setVisibility(View.GONE);
        rcvListPrescription.setVisibility(View.VISIBLE);
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
                FirebaseUtil.sendNotifyDataChange();
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
                            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                            .create();
                    List<Prescription> results = gson.fromJson(
                            new Gson().toJson(responseObject.getData()),
                            new TypeToken<List<Prescription>>() {
                            }.getType()
                    );
                    if (results.isEmpty()) {
                        showDefaultView();
                        return;
                    }
                    prescriptions.addAll(results);
                    prescriptionAdapter.notifyDataSetChanged();
                    showListView();
                }
                Log.i("GET_PRESCRIPTIONS", response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("GET_PRESCRIPTIONS", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void getPrescriptionShared(Long id) {
        PrescriptionService service = RetrofitClient.createService(PrescriptionService.class);
        service.getById(id).enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                ResponseObject responseObject = response.body();
                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                            .create();
                    Prescription result = gson.fromJson(
                            new Gson().toJson(responseObject.getData()),
                            new TypeToken<Prescription>() {
                            }.getType()
                    );
                    createNewPrescription(result);
                } else {
                    Toast.makeText(PrescriptionManagement.this, responseObject.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.i("GET_DATA_SHARED", responseObject.getMessage());
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("GET_DATA_SHARED", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}
