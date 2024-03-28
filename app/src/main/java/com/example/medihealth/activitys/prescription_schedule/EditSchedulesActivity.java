package com.example.medihealth.activitys.prescription_schedule;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.adapters.prescription_schedule.ScheduleAdapter;
import com.example.medihealth.apiservices.PrescriptionService;
import com.example.medihealth.models.Prescription;
import com.example.medihealth.models.ResponseObject;
import com.example.medihealth.models.Schedule;
import com.example.medihealth.retrofitcustom.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSchedulesActivity extends AppCompatActivity implements ItemTouchHelperListener {

    Prescription prescription;
    TextView tvToolbarTitle;
    ImageView btnBackToolbar;
    List<Schedule> schedules = new ArrayList<>();
    RecyclerView rcvListSchedule;
    ScheduleAdapter scheduleAdapter;
    FloatingActionButton fabAddNew;
    Button btnComplete;
    RelativeLayout rootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_schedule);

        loadData();

        rootView = findViewById(R.id.root_view);

        // Custom toolbar
        tvToolbarTitle = findViewById(R.id.tv_toolbar);
        tvToolbarTitle.setText("Chỉnh sửa thời gian thông báo");
        btnBackToolbar = findViewById(R.id.btn_back_toolbar);
        btnBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rcvListSchedule = findViewById(R.id.rcv_list_schedule);
        rcvListSchedule.setLayoutManager(new LinearLayoutManager(this));
        scheduleAdapter = new ScheduleAdapter(schedules);
        scheduleAdapter.setOnClickListener(new CustomOnClickListener<Schedule>() {
            @Override
            public void onClick(Schedule data) {
                showTimePickerDialogUpdate(data);
            }
        });
        rcvListSchedule.setAdapter(scheduleAdapter);
        rcvListSchedule.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    fabAddNew.hide();
                }
                else {
                    fabAddNew.show();
                }
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewItemTouchHelper(
                0,
                ItemTouchHelper.LEFT,
                this
        );
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rcvListSchedule);

        fabAddNew = findViewById(R.id.fab_add_new);
        fabAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        btnComplete = findViewById(R.id.btn_complete);
        btnComplete.setText("Lưu");
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prescription.setSchedules(schedules);
                updatePrescription();
            }
        });
    }

    private void showTimePickerDialogUpdate(Schedule schedule) {
        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(this);
        timePickerDialog.setIs24HourView(true);
        timePickerDialog.setTitle("Chỉnh sửa");
        timePickerDialog.setTime(schedule.getTime().getHour(), schedule.getTime().getMinute());
        timePickerDialog.setNegativeButton("Hủy", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.dismiss();
            }
        });

        timePickerDialog.setPositiveButton("Lưu", new CustomTimePickerDialog.onClickPositiveButtonListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(int hour, int minute) {
                LocalTime time = LocalTime.of(hour, minute);
                timePickerDialog.dismiss();
                schedule.setTime(time);
                scheduleAdapter.notifyDataSetChanged();
            }
        });

        timePickerDialog.show();
    }

    private void showTimePickerDialog() {
        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(this);
        timePickerDialog.setIs24HourView(true);
        timePickerDialog.setTitle("Thêm mới");
        timePickerDialog.setTime(LocalTime.NOON.getHour(), 0);
        timePickerDialog.setNegativeButton("Hủy", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.dismiss();
            }
        });

        timePickerDialog.setPositiveButton("Thêm", new CustomTimePickerDialog.onClickPositiveButtonListener() {
            @Override
            public void onClick(int hour, int minute) {
                LocalTime time = LocalTime.of(hour, minute);
                timePickerDialog.dismiss();
                addScheduleToList(time);
            }
        });

        timePickerDialog.show();
    }

    private void backToViewDetail() {
        setResult(RESULT_OK);
        finish();
    }

    private void updatePrescription() {
        PrescriptionService service = RetrofitClient.createService(PrescriptionService.class);
        service.editPrescription(prescription).enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(
                            EditSchedulesActivity.this,
                            "Cập nhật thành công",
                            Toast.LENGTH_SHORT
                    ).show();
                    Log.i("EDIT_SCHEDULES", response.body().getMessage());
                    backToViewDetail();
                } else {
                    Toast.makeText(
                            EditSchedulesActivity.this,
                            response.body().getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                    Log.i("EDIT_SCHEDULES", response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(
                        EditSchedulesActivity.this,
                        "Đã có lỗi xảy ra, vui lòng thử lại sau",
                        Toast.LENGTH_SHORT
                ).show();
                Log.e("EDIT_SCHEDULES", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addScheduleToList(LocalTime time) {

        List<LocalTime> times = schedules.stream()
                .map(Schedule::getTime)
                .collect(Collectors.toList());

        if (!times.contains(time)) {
            Schedule schedule = new Schedule();
            schedule.setTime(time);
            schedule.setActive(true);
            schedules.add(schedule);
            schedules.sort(new Comparator<Schedule>() {
                @Override
                public int compare(Schedule o1, Schedule o2) {
                    LocalTime t1 = o1.getTime();
                    LocalTime t2 = o2.getTime();
                    if (t1.getHour() != t2.getHour())
                        return Integer.compare(t1.getHour(), t2.getHour());
                    return Integer.compare(t1.getMinute(), t2.getMinute());
                }
            });
            scheduleAdapter.notifyDataSetChanged();
        }
    }

    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        prescription = (Prescription) bundle.getSerializable("prescription");
        schedules = prescription.getSchedules()
                .stream()
                .filter(Schedule::isActive)
                .collect(Collectors.toList());
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof ScheduleAdapter.ScheduleViewHolder) {
            int index = viewHolder.getAdapterPosition();
            Schedule schedule = schedules.get(index);
            removeItem(index);

            Snackbar snackbar = Snackbar.make(rootView, "Đã xóa", Snackbar.LENGTH_SHORT);
            snackbar.setAction("Hoàn tác", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    undoItem(schedule, index);
                }
            });
            snackbar.show();
        }
    }

    public void removeItem(int index) {
        schedules.remove(index);
        scheduleAdapter.notifyItemRemoved(index);
    }

    public void undoItem(Schedule schedule, int index) {
        schedules.add(index, schedule);
        scheduleAdapter.notifyItemInserted(index);
    }
}
