package com.example.medihealth.apiservices;

import com.example.medihealth.models.Prescription;
import com.example.medihealth.models.PrescriptionStat;

import java.time.LocalDate;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PrescriptionStatService {
    @GET("stat/day")
    Call<List<Prescription>> getPrescriptionStatDay(@Query("duid") Long duid, @Query("date") LocalDate date);

    @GET("stat/week")
    Call<List<Prescription>> getPrescriptionStatWeek(@Query("duid") Long duid, @Query("start") LocalDate start, @Query("end") LocalDate end);

    @GET("stat/month")
    Call<List<PrescriptionStat>> getPrescriptionStatMonth(@Query("duid") Long duid, @Query("dayofmonth") LocalDate dayofmonth);
}
