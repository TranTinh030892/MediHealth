package com.example.medihealth.activitys.prescription_schedule;

import android.content.Context;
import android.util.Log;

import com.example.medihealth.apiservices.ScheduleService;
import com.example.medihealth.models.ResponseObject;
import com.example.medihealth.models.Schedule;
import com.example.medihealth.retrofitcustom.LocalDateAdapter;
import com.example.medihealth.retrofitcustom.LocalTimeAdapter;
import com.example.medihealth.retrofitcustom.RetrofitClient;
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

public class SyncService {

    private static final String TAG = SyncService.class.getSimpleName();

    public interface ScheduleCallback {
        void onSchedulesReceived(List<Schedule> schedules);

        void onScheduleError(String message);
    }

    public static void sync(Context context) {
        getSchedules(new ScheduleCallback() {
            @Override
            public void onSchedulesReceived(List<Schedule> schedules) {
                schedules.forEach((schedule) -> {
                    Log.e("SCHEDULE", String.format("{id: %d, active: %b}", schedule.getId(), schedule.isActive()));
                    if (schedule.isActive()
                            && schedule.getPrescription().isActive()
                            && schedule.getPrescription().getDrugUser().isActive()) {
                        RemindScheduler.scheduleRemind(context, schedule);
                    } else {
                        RemindScheduler.cancelRemind(context, schedule);
                    }
                });
            }

            @Override
            public void onScheduleError(String message) {

            }
        });
    }

    private static void getSchedules(ScheduleCallback scheduleCallback) {
        ScheduleService service = RetrofitClient.createService(ScheduleService.class);
        service.getAllByUser("12345").enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                            .create();
                    List<Schedule> data = gson.fromJson(
                            new Gson().toJson(response.body().getData()),
                            new TypeToken<List<Schedule>>() {
                            }.getType()
                    );
                    scheduleCallback.onSchedulesReceived(data);
                }
                Log.i(TAG, response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                scheduleCallback.onScheduleError(t.getMessage());
                Log.e(TAG, Objects.requireNonNull(t.getMessage()));
                getSchedules(scheduleCallback);
            }
        });
    }

    public static void logout(Context context) {
        Log.v("LOGOUT", "Cancel reminders");
        getSchedules(new ScheduleCallback() {
            @Override
            public void onSchedulesReceived(List<Schedule> schedules) {
                schedules.forEach((schedule) -> {
                    RemindScheduler.cancelRemind(context, schedule);
                });
            }

            @Override
            public void onScheduleError(String message) {

            }
        });
    }
}
