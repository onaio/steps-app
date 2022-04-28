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
import static com.onaio.steps.helper.Constants.HH_USER_ID;
import static com.onaio.steps.helper.Constants.HH_USER_PASSWORD;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.clients.HouseholdService;
import com.onaio.steps.decorators.FileDecorator;
import com.onaio.steps.handler.actions.ExportHandler;
import com.onaio.steps.model.UploadResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    public void prepareForUpload(Queue<FileDecorator> fileDecorators) {
        if (!TextUtils.isEmpty(KeyValueStoreFactory.instance(activity).getString(ENDPOINT_URL))) {

            String endPoint = KeyValueStoreFactory.instance(activity).getString(ENDPOINT_URL);

            KeyValueStore store = KeyValueStoreFactory.instance(activity);
            String userId = store.getString(HH_USER_ID);
            String userPassword = store.getString(HH_USER_PASSWORD);

            if (!isAllFieldValid(userId, userPassword)) {
                onExportListener.onError(activity.getString(R.string.invalid_fields_error));
            } else {

                RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"), userId);
                RequestBody userPasswordBody = RequestBody.create(MediaType.parse("text/plain"), userPassword);

                if (!fileDecorators.isEmpty()) {
                    upload(fileDecorators, endPoint, userIdBody, userPasswordBody, new ArrayList<>());
                }
            }
        } else {
            onExportListener.onError(activity.getString(R.string.export_failed));
        }
    }

    public void upload(Queue<FileDecorator> fileDecorators, String endPoint, RequestBody userIdBody, RequestBody userPasswordBody, List<UploadResult> uploadResults) {

        FileDecorator fileDecorator = fileDecorators.remove();
        String fileType = "text/csv";
        RequestBody requestFile = RequestBody.create(MediaType.parse(fileType), fileDecorator.getFile());
        MultipartBody.Part fileBody = MultipartBody.Part.createFormData("file", fileDecorator.getFile().getName(), requestFile);

        retrofit.create(HouseholdService.class).uploadData(endPoint, fileBody, userIdBody, userPasswordBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                uploadResults.add(new UploadResult(fileDecorator.getFormTitle(), response.isSuccessful() && response.code() == 201, findError(response.errorBody())));

                if (!isDone(fileDecorators, uploadResults)) {
                    upload(fileDecorators, endPoint, userIdBody, userPasswordBody, uploadResults);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                uploadResults.add(new UploadResult(fileDecorator.getFormTitle(), false));

                if (!isDone(fileDecorators, uploadResults)) {
                    upload(fileDecorators, endPoint, userIdBody, userPasswordBody, uploadResults);
                }
            }
        });
    }

    public boolean isDone(Queue<FileDecorator> fileDecorators, List<UploadResult> uploadResults) {
        boolean isDone = fileDecorators.isEmpty();
        if (isDone) {
            onExportListener.onFileUploaded(uploadResults);
        }
        return isDone;
    }

    public boolean isAllFieldValid(@Nullable String... fields) {
        boolean isValid = true;

        if (fields == null) return false;

        for (String field : fields) {
            isValid = !(field == null || field.isEmpty());
            if (!isValid) break;
        }
        return isValid;
    }

    @Nullable
    public String findError(@Nullable ResponseBody errorBody) {
        String error = null;
        if (errorBody != null) {
            try {
                String rawError = errorBody.string();
                JSONObject jsonObject = new JSONObject(rawError);
                error = jsonObject.getString("error");
            } catch (JSONException | IOException ex) {
                ex.printStackTrace();
            }
        }

        return error;
    }
}
