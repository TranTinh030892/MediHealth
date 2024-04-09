package com.example.medihealth.apiservices;

import com.example.medihealth.models.DrugUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DrugUserService {
    @GET("drug-user/all")
    Call<List<DrugUser>> getDrugUserofUser(@Query("uid") String user_id);
}
