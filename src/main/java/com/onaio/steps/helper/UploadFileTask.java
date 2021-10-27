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

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.onaio.steps.R;
import com.onaio.steps.handler.actions.ExportHandler;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class UploadFileTask extends AsyncTask<File, Void, Boolean> {
    private final Activity activity;
    private ExportHandler.OnExportListener onExportListener;
    private String error = null;

    public UploadFileTask(Activity activity, ExportHandler.OnExportListener onExportListener) {
        this.activity = activity;
        this.onExportListener = onExportListener;
        this.error = activity.getString(R.string.export_failed);
    }

    public void setOnExportListener(ExportHandler.OnExportListener onExportListener) {
        this.onExportListener = onExportListener;
    }

    @Override
    protected Boolean doInBackground(File... files) {
        if (!TextUtils.isEmpty(KeyValueStoreFactory.instance(activity).getString(ENDPOINT_URL))) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(KeyValueStoreFactory.instance(activity).getString(ENDPOINT_URL));
            try {

                KeyValueStore store = KeyValueStoreFactory.instance(activity);
                String surveyId = store.getString(HH_SURVEY_ID);
                String userId = store.getString(HH_USER_ID);

                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
                multipartEntity.addPart("file", new FileBody(files[0]));
                multipartEntity.addPart("survey_id", new StringBody(surveyId));
                multipartEntity.addPart("username", new StringBody(userId));
                httpPost.setEntity(multipartEntity);
                HttpResponse response = httpClient.execute(httpPost);
                if (response.getStatusLine().getStatusCode() == 201) {
                    new CustomNotification().notify(activity, R.string.export_complete, R.string.export_complete_message);
                    return true;
                }
                else {
                    String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                    try {
                        error = new JSONObject(responseBody).optString("error", error);
                    } catch (JSONException ex) {
                        new Logger().log(ex, "Json parse failed");
                    }
                }
            } catch (IOException e) {
                new Logger().log(e, "Export failed.");
                new CustomNotification().notify(activity, R.string.error_title, R.string.export_failed);
            }
        } else {
            new CustomNotification().notify(activity, R.string.error_title, R.string.export_failed);
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        if(onExportListener != null) {
            if (success) {
                onExportListener.onFileUploaded();
            }
            else {
                onExportListener.onFileFailedToUpload(error);
            }
        }
    }
}
