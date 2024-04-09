package com.example.medihealth.activities.prescription_schedule;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.adapters.prescription_schedule.PrescriptionItemAdapter;
import com.example.medihealth.adapters.prescription_schedule.ScheduleAdapter;
import com.example.medihealth.apiservices.PrescriptionService;
import com.example.medihealth.models.Prescription;
import com.example.medihealth.models.ResponseObject;
import com.example.medihealth.models.Schedule;
import com.example.medihealth.retrofitcustom.LocalDateAdapter;
import com.example.medihealth.retrofitcustom.LocalDateTimeAdapter;
import com.example.medihealth.retrofitcustom.LocalTimeAdapter;
import com.example.medihealth.retrofitcustom.RetrofitClient;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescriptionDetailManagement extends AppCompatActivity {

    private static Prescription prescription;
    PrescriptionItemAdapter prescriptionItemAdapter;
    ScheduleAdapter scheduleAdapter;
    ImageView btnBack, btnMore;
    TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_detail_management);
        loadData();

        // Custom toolbar
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(prescription.getTitle());
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnMore = findViewById(R.id.btn_more);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu();
            }
        });


        RecyclerView rcvListPrescriptionItem = findViewById(R.id.rcv_list_prescription_item);
        rcvListPrescriptionItem.setLayoutManager(new LinearLayoutManager(this));
        prescriptionItemAdapter = new PrescriptionItemAdapter(prescription.getPrescriptionItems());
        rcvListPrescriptionItem.setAdapter(prescriptionItemAdapter);

        RecyclerView rcvListPrescriptionSchedule = findViewById(R.id.rcv_list_prescription_schedule);
        rcvListPrescriptionSchedule.setLayoutManager(new LinearLayoutManager(this));
        scheduleAdapter = new ScheduleAdapter(prescription.getSchedules());
        rcvListPrescriptionSchedule.setAdapter(scheduleAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refresh() {
        List<Schedule> schedules = new ArrayList<>(
                prescription.getSchedules()
                        .stream().filter(Schedule::isActive)
                        .collect(Collectors.toList())
        );
        scheduleAdapter.notifyDataSetChanged();
        schedules.sort((o1, o2) -> {
            LocalTime t1 = o1.getTime();
            LocalTime t2 = o2.getTime();
            if (t1.getHour() != t2.getHour())
                return Integer.compare(t1.getHour(), t2.getHour());
            return Integer.compare(t1.getMinute(), t2.getMinute());
        });

        tvTitle.setText(prescription.getTitle());
        prescriptionItemAdapter.setPrescriptionItems(prescription.getPrescriptionItems());
        scheduleAdapter.setSchedules(schedules);
    }

    private void getData() {
        PrescriptionService service = RetrofitClient.createService(PrescriptionService.class);
        service.getById(prescription.getId()).enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                            .create();
                    prescription = gson.fromJson(
                            new Gson().toJson(response.body().getData()),
                            new TypeToken<Prescription>() {
                            }.getType()
                    );
                    refresh();
                } else {
                    Toast.makeText(
                            PrescriptionDetailManagement.this,
                            response.body().getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
                Log.i("GET_PRESCRIPTION_DETAIL", response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("GET_PRESCRIPTION_DETAIL", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        prescription = (Prescription) bundle.get("prescription");
    }

    private void showPopupMenu() {
        PopupMenu menu = new PopupMenu(btnMore.getContext(), btnMore);
        menu.inflate(R.menu.menu_prescription_detail);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.i_edit_title) {
                    showDialogEditTitle();
                    return true;
                }
                if (item.getItemId() == R.id.i_edit_prescription_item) {
                    editPrescriptionItems();
                    return true;
                }
                if (item.getItemId() == R.id.i_edit_schedule) {
                    editSchedules();
                    return true;
                }
                if (item.getItemId() == R.id.i_delete) {
                    showAlertDialogDelete();
                    return true;
                }
                return false;
            }
        });
        menu.show();
    }

    private void editPrescriptionItems() {
        Intent intent = new Intent(this, EditPrescriptionItemsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("prescription", prescription);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void editSchedules() {
        Intent intent = new Intent(this, EditSchedulesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("prescription", prescription);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void showDialogEditTitle() {
        CustomDialog dialog = new CustomDialog(this);
        dialog.setTitle("Sửa tên đơn thuốc");
        dialog.setContent(prescription.getTitle());
        dialog.setHint("Tên đơn thuốc");
        dialog.setNegativeButton("Hủy", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setPositiveButton("Lưu", new CustomOnClickListener<String>() {
            @Override
            public void onClick(String data) {
                if (data.isEmpty()) {
                    Toast.makeText(
                            PrescriptionDetailManagement.this,
                            "Vui lòng nhập tên đơn thuốc",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    prescription.setTitle(data);
                    updatePrescription();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void updatePrescription() {
        PrescriptionService prescriptionService = RetrofitClient.createService(PrescriptionService.class);
        prescriptionService.editPrescription(prescription).enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(
                            PrescriptionDetailManagement.this,
                            "Cập nhật thành công",
                            Toast.LENGTH_SHORT
                    ).show();
                    refresh();
                } else {
                    Toast.makeText(
                            PrescriptionDetailManagement.this,
                            response.body().getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
                Log.i("UPDATE_PRESCRIPTION", response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(
                        PrescriptionDetailManagement.this,
                        "Đã có lỗi xảy ra, vui lòng thử lại sau",
                        Toast.LENGTH_SHORT
                ).show();
                Log.e("UPDATE_PRESCRIPTION", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void showAlertDialogDelete() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle("Xác nhận");
        dialog.setMessage("Bạn có chắc chắn muốn xóa đơn thuốc này không?");
        dialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePrescription();
            }
        });
        dialog.show();
    }

    private void deletePrescription() {
        PrescriptionService prescriptionService = RetrofitClient.createService(PrescriptionService.class);
        prescriptionService.deletePrescription(prescription.getId()).enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(
                            PrescriptionDetailManagement.this,
                            "Xóa đơn thuốc thành công",
                            Toast.LENGTH_SHORT
                    ).show();
                    SyncService.sync(PrescriptionDetailManagement.this);
                    // Back to prescription management
                    finish();
                } else {
                    Toast.makeText(
                            PrescriptionDetailManagement.this,
                            response.body().getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
                Log.i("DELETE_PRESCRIPTION", response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(
                        PrescriptionDetailManagement.this,
                        "Đã có lỗi xảy ra, vui lòng thử lại sau",
                        Toast.LENGTH_SHORT
                ).show();
                Log.e("DELETE_PRESCRIPTION", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

}
