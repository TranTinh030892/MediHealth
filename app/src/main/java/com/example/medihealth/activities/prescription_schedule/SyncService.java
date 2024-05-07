package com.example.medihealth.activities.prescription_schedule;

import android.content.Context;
import android.util.Log;

import com.example.medihealth.apiservices.ScheduleService;
import com.example.medihealth.models.ResponseObject;
import com.example.medihealth.models.Schedule;
import com.example.medihealth.retrofitcustom.LocalDateAdapter;
import com.example.medihealth.retrofitcustom.LocalDateTimeAdapter;
import com.example.medihealth.retrofitcustom.LocalTimeAdapter;
import com.example.medihealth.retrofitcustom.RetrofitClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncService {

    private static final String TAG = SyncService.class.getSimpleName();

    public interface MyCallback {
        void onResponse(List<Schedule> schedules);

        void onFailure(String message);
    }

    public static void sync(Context context) {
        getSchedules(new MyCallback() {
            @Override
            public void onResponse(List<Schedule> schedules) {
                schedules.forEach((schedule) -> {
                    Log.i("SCHEDULE", String.format("{id: %d, active: %b}", schedule.getId(), schedule.isActive()));
                    if (schedule.isActive()
                            && schedule.getPrescription().isActive()
                            && schedule.getPrescription().getDrugUser().isActive()) {
                        RemindScheduler.scheduleRemind(context, schedule);
                    } else {
                        RemindScheduler.cancelRemind(context, schedule.getId());
                    }
                });
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private static void getSchedules(MyCallback MyCallback) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Log.i(TAG, "Not logged in yet");
            return;
        }

        String uid = user.getUid();

        ScheduleService service = RetrofitClient.createService(ScheduleService.class);
        service.getAllByUser(uid).enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                            .create();
                    List<Schedule> data = gson.fromJson(
                            new Gson().toJson(response.body().getData()),
                            new TypeToken<List<Schedule>>() {
                            }.getType()
                    );
                    MyCallback.onResponse(data);
                }
                Log.i(TAG, response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                MyCallback.onFailure(t.getMessage());
                Log.e(TAG, Objects.requireNonNull(t.getMessage()));
                getSchedules(MyCallback);
            }
        });
    }

    public static void logout(Context context) {
        Log.v("LOGOUT", "Cancel reminders");
        getSchedules(new MyCallback() {
            @Override
            public void onResponse(List<Schedule> schedules) {
                schedules.forEach((schedule) -> {
                    RemindScheduler.cancelRemind(context, schedule.getId());
                });
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
