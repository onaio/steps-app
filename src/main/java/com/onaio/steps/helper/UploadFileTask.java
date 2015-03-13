package com.onaio.steps.helper;

import android.app.Activity;
import android.os.AsyncTask;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;

import static com.onaio.steps.helper.Constants.ENDPOINT_URL;

public class UploadFileTask extends AsyncTask<File, Void, Void> {
    private final Activity activity;

    public UploadFileTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(File... files) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(KeyValueStoreFactory.instance(activity).getString(ENDPOINT_URL));
        try {
            MultipartEntity multipartEntity = new MultipartEntity();
            multipartEntity.addPart("file", new FileBody(files[0]));
            httpPost.setEntity(multipartEntity);
            httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
