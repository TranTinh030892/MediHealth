package com.example.medihealth.apiservices;

import com.example.medihealth.models.ResponseObject;
import com.example.medihealth.models.Schedule;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ScheduleService {

    @GET("schedule/today")
    Call<List<Schedule>> getScheduleToday(@Query("user_id") String uid);

    @GET("schedule")
    Call<ResponseObject> getAllByUser(@Query("uid") String uid);

    @GET("schedule/{id}")
    Call<ResponseObject> getById(@Path("id") Long id);

}
