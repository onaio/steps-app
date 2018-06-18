/*
 * Copyright (C) 2017 Shobhit
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.onaio.steps.tasks;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.zxing.WriterException;
import com.onaio.steps.listeners.QRBitmapGeneratorListener;
import com.onaio.steps.utils.QRCodeUtils;

import org.json.JSONException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 05/06/2018.
 */

public class GenerateQRCodeAsyncTask extends AsyncTask<Void, Void, Object> {

    private static final String TAG = GenerateQRCodeAsyncTask.class.getName();
    private QRBitmapGeneratorListener qrBitmapGeneratorListener;
    private Activity context;

    public GenerateQRCodeAsyncTask(@NonNull Activity context, QRBitmapGeneratorListener qrBitmapGeneratorListener) {
        this.qrBitmapGeneratorListener = qrBitmapGeneratorListener;
        this.context = context;
    }

    @Override
    protected Object doInBackground(Void... voids) {
        try {
            return QRCodeUtils.generateSettingQRCode(context);
        } catch (NoSuchAlgorithmException | WriterException | IOException | JSONException e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return e;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        if (qrBitmapGeneratorListener != null) {
            if (o instanceof Bitmap) {
                qrBitmapGeneratorListener.onBitmapGenerated((Bitmap) o);
            } else {
                qrBitmapGeneratorListener.onError((Exception) o);
            }
        }
    }
}
