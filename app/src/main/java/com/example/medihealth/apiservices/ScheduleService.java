package com.example.medihealth.apiservices;

import com.example.medihealth.models.Schedule;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ScheduleService {
    @GET("schedule/today")
    Call<List<Schedule>> getScheduleToday(@Query("user_id") String user_id);
}
