package com.example.medihealth.apiservices;

import com.example.medihealth.models.Prescription;
import com.example.medihealth.models.ResponseObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PrescriptionService {

    @GET("prescription")
    Call<ResponseObject> getAllByDrugUser(@Query("d_uid") Long drugUserId);

    @GET("prescription/{id}")
    Call<ResponseObject> getById(@Path("id") Long id);

    @POST("prescription")
    Call<ResponseObject> addPrescription(@Body Prescription prescription);

    @PUT("prescription")
    Call<ResponseObject> editPrescription(@Body Prescription prescription);

    @DELETE("prescription/{id}")
    Call<ResponseObject> deletePrescription(@Path("id") Long id);

}
