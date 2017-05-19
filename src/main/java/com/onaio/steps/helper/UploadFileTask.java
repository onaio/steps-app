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

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.onaio.steps.R;
import com.onaio.steps.handler.actions.ExportHandler;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.IOException;

import static com.onaio.steps.helper.Constants.ENDPOINT_URL;

//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;

public class UploadFileTask extends AsyncTask<File, Void, Boolean> {
    private final Activity activity;
    private ExportHandler.OnExportListener onExportListener;

    public UploadFileTask(Activity activity, ExportHandler.OnExportListener onExportListener) {
        this.activity = activity;
        this.onExportListener = onExportListener;
    }

    public void setOnExportListener(ExportHandler.OnExportListener onExportListener) {
        this.onExportListener = onExportListener;
    }

    @Override
    protected Boolean doInBackground(File... files) {
        if (!TextUtils.isEmpty(KeyValueStoreFactory.instance(activity).getString(ENDPOINT_URL))) {
            /*HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(KeyValueStoreFactory.instance(activity).getString(ENDPOINT_URL));
            try {
                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
                multipartEntity.addPart("file", new FileBody(files[0]));
                httpPost.setEntity(multipartEntity);
                httpClient.execute(httpPost);
                new CustomNotification().notify(activity, R.string.export_complete, R.string.export_complete_message);
                return true;
            } catch (IOException e) {
                new Logger().log(e, "Export failed.");
                new CustomNotification().notify(activity, R.string.error_title, R.string.export_failed);
            }*/
            try {
                OkHttpClient client = new OkHttpClient();

                final MediaType MEDIA_TYPE = MediaType.parse("text/csv");

                RequestBody requestBody = new MultipartBuilder()
                        .type(MultipartBuilder.FORM)
                        .addFormDataPart("file", files[0].getName(), RequestBody.create(MEDIA_TYPE, files[0]))
                        .build();

                Request request = new Request.Builder()
                        .url(KeyValueStoreFactory.instance(activity).getString(ENDPOINT_URL))
                        .post(requestBody)
                        .build();
                client.newCall(request).execute();
                new CustomNotification().notify(activity, R.string.export_complete, R.string.export_complete_message);
                return true;
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
        if (onExportListener != null) onExportListener.onFileUploaded(success);
    }
}
