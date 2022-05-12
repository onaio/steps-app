package com.onaio.steps.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.listeners.QRBitmapSaveListener;
import com.onaio.steps.utils.QRCodeUtils;

import org.json.JSONException;

import java.security.NoSuchAlgorithmException;

import timber.log.Timber;

public class SaveQRCodeAsyncTask extends AsyncTask<Void, Void, Exception> {

    private AppCompatActivity activity;
    private QRBitmapSaveListener qrBitmapSaveListener;
    private Bitmap bitmap;
    public static final String TAG = SaveQRCodeAsyncTask.class.getName();

    public SaveQRCodeAsyncTask(@NonNull AppCompatActivity activity, @NonNull Bitmap bitmap, QRBitmapSaveListener qrBitmapSaveListener) {
        this.activity = activity;
        this.qrBitmapSaveListener = qrBitmapSaveListener;
        this.bitmap = bitmap;
    }

    @Override
    protected Exception doInBackground(Void... voids) {
        try {
            QRCodeUtils.saveToDisk(activity, bitmap);
        } catch (JSONException | NoSuchAlgorithmException e) {
            Timber.e(e);
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
