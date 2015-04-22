package com.onaio.steps.helper;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;

import com.onaio.steps.R;

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
            Notification.Builder builder = new Notification.Builder(activity);
            Notification exportError = builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(activity.getText(R.string.error_title))
                    .setContentText(activity.getText(R.string.export_failed))
                    .build();
            NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1,exportError);
        }
        return null;
    }
}
