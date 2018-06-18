package com.onaio.steps.tasks;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.onaio.steps.listeners.QRBitmapSaveListener;
import com.onaio.steps.utils.QRCodeUtils;

import org.json.JSONException;

import java.security.NoSuchAlgorithmException;

public class SaveQRCodeAsyncTask extends AsyncTask<Void, Void, Exception> {

    private Activity activity;
    private QRBitmapSaveListener qrBitmapSaveListener;
    private Bitmap bitmap;
    public static final String TAG = SaveQRCodeAsyncTask.class.getName();

    public SaveQRCodeAsyncTask(@NonNull Activity activity, @NonNull Bitmap bitmap, QRBitmapSaveListener qrBitmapSaveListener) {
        this.activity = activity;
        this.qrBitmapSaveListener = qrBitmapSaveListener;
        this.bitmap = bitmap;
    }

    @Override
    protected Exception doInBackground(Void... voids) {
        try {
            QRCodeUtils.saveToDisk(activity, bitmap);
        } catch (JSONException | NoSuchAlgorithmException e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return e;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Exception exception) {
        if (qrBitmapSaveListener != null) {
            if (exception != null) {
                qrBitmapSaveListener.onError(exception);
            } else {
                qrBitmapSaveListener.onSuccessfulSave();
            }
        }
    }
}
