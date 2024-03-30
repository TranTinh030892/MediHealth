package com.example.medihealth.apiservices;

import com.example.medihealth.models.ResponseObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ScheduleService {

    @GET("schedule/today")
    Call<ResponseObject> getScheduleToday(@Query("user_id") String uid);

}
