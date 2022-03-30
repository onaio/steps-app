/*
 * Copyright 2016. World Health Organization
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onaio.steps.helper;

import static com.onaio.steps.helper.Constants.ENDPOINT_URL;
import static com.onaio.steps.helper.Constants.HH_SURVEY_ID;
import static com.onaio.steps.helper.Constants.HH_USER_ID;
import static com.onaio.steps.helper.Constants.HH_USER_PASSWORD;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.clients.HouseholdService;
import com.onaio.steps.handler.actions.ExportHandler;

import java.io.File;
import java.util.Queue;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UploadFileTask {
    private final AppCompatActivity activity;
    private final ExportHandler.OnExportListener onExportListener;
    private final Retrofit retrofit;

    public UploadFileTask(@NonNull AppCompatActivity activity, @NonNull ExportHandler.OnExportListener onExportListener) {
        this.activity = activity;
        this.onExportListener = onExportListener;
        this.retrofit = new Retrofit.Builder().baseUrl("https://steps.ona.io/").build();
    }

    public void prepareForUpload(Queue<File> files) {
        if (!TextUtils.isEmpty(KeyValueStoreFactory.instance(activity).getString(ENDPOINT_URL))) {

            String endPoint = KeyValueStoreFactory.instance(activity).getString(ENDPOINT_URL);

            KeyValueStore store = KeyValueStoreFactory.instance(activity);
            String surveyId = store.getString(HH_SURVEY_ID);
            String userId = store.getString(HH_USER_ID);
            String userPassword = store.getString(HH_USER_PASSWORD);

            if (surveyId.isEmpty() || userId.isEmpty() || userPassword.isEmpty()) {
                new CustomNotification().notify(activity, R.string.error_title, R.string.invalid_fields_error);
                onExportListener.onFileUploaded(false);
            } else {

                RequestBody surveyIdBody = RequestBody.create(MediaType.parse("text/plain"), surveyId);
                RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"), userId);
                RequestBody userPasswordBody = RequestBody.create(MediaType.parse("text/plain"), userPassword);

                if (!files.isEmpty()) {
                    upload(files, endPoint, surveyIdBody, userIdBody, userPasswordBody);
                }
            }
        } else {
            new CustomNotification().notify(activity, R.string.error_title, R.string.export_failed);
            onExportListener.onFileUploaded(false);
        }
    }

    public void upload(Queue<File> files, String endPoint, RequestBody surveyIdBody, RequestBody userIdBody, RequestBody userPasswordBody) {

        File file = files.remove();
        String fileType = "text/csv";
        RequestBody requestFile = RequestBody.create(MediaType.parse(fileType), file);
        MultipartBody.Part fileBody = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        retrofit.create(HouseholdService.class).uploadData(endPoint, fileBody, surveyIdBody, userIdBody, userPasswordBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.code() == 201) {
                    if (files.isEmpty()) {
                        new CustomNotification().notify(activity, R.string.export_complete, R.string.export_complete_message);
                        onExportListener.onFileUploaded(true);
                    } else {
                        upload(files, endPoint, surveyIdBody, userIdBody, userPasswordBody);
                    }
                } else {
                    if (files.isEmpty()) {
                        new CustomNotification().notify(activity, R.string.error_title, R.string.export_failed);
                        onExportListener.onFileUploaded(false);
                    }
                    else {
                        upload(files, endPoint, surveyIdBody, userIdBody, userPasswordBody);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                new CustomNotification().notify(activity, R.string.error_title, R.string.export_failed);
                onExportListener.onFileUploaded(false);
            }
        });
    }
}
