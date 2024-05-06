package com.example.medihealth.apiservices;

import com.example.medihealth.utils.show_schedule_today.ResponseMessage;

import java.time.LocalTime;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ConfirmNotificationService {
    @POST("schedule/confirm")
    Call<ResponseMessage> saveConfirmNotification_Confirm(@Query("schedule_id") Long schedule_id, @Query("time") LocalTime time);

    @POST("schedule/skip")
    Call<ResponseMessage> saveConfirmNotification_Cancel(@Query("schedule_id") Long schedule_id, @Query("time") LocalTime time, @Query("des") String des);
}
