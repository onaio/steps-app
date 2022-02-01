package com.onaio.steps.clients;


import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
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
            @Field("survey_id") String surveyId,
            @Field("username") String username,
            @Field("password") String password);
}
