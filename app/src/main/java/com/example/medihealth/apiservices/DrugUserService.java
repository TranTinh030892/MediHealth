package com.example.medihealth.apiservices;

import com.example.medihealth.models.DrugUser;
import com.example.medihealth.models.ResponseObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DrugUserService {
    @GET("drug-user")
    Call<ResponseObject> getAllByUser(@Query("uid") String userId);

    @POST("drug-user")
    Call<ResponseObject> addDrugUser(@Body DrugUser drugUser);

    @PUT("drug-user")
    Call<ResponseObject> editDrugUser(@Body DrugUser drugUser);

    @DELETE("drug-user/{id}")
    Call<ResponseObject> deleteDrugUser(@Path("id") Long id);
}
