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

import com.onaio.steps.R;
import com.onaio.steps.handler.actions.ExportHandler;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static com.onaio.steps.helper.Constants.ENDPOINT_URL;

public class UploadFileTask extends AsyncTask<File, Void, Void> {
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
    protected Void doInBackground(File... files) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(KeyValueStoreFactory.instance(activity).getString(ENDPOINT_URL));
        try {
            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
            multipartEntity.addPart("file", new FileBody(files[0]));
            httpPost.setEntity(multipartEntity);
            httpClient.execute(httpPost);
            new CustomNotification().notify(activity, R.string.export_complete, R.string.export_complete_message);
        } catch (IOException e) {
            new Logger().log(e,"Export failed.");
            new CustomNotification().notify(activity, R.string.error_title, R.string.export_failed);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(onExportListener != null) onExportListener.onFileUploaded();
    }
}
