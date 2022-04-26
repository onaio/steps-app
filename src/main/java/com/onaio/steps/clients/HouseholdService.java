package com.onaio.steps.clients;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface HouseholdService {

    @Multipart
    @POST
    Call<ResponseBody> uploadData(
            @Url String url,
            @Part MultipartBody.Part file,
            @Part("username") RequestBody username,
            @Part("password") RequestBody password);
}
