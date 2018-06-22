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

package com.onaio.steps.handler.actions;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.onaio.steps.R;
import com.onaio.steps.activities.SettingsImportExportActivity;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.model.RequestCode;
import com.onaio.steps.utils.CompressionUtils;
import com.onaio.steps.utils.ViewUtils;

import java.io.IOException;
import java.util.zip.DataFormatException;


public class QRCodeScanHandler implements IMenuHandler, IActivityResultHandler {

    private static final String TAG = QRCodeScanHandler.class.getName();
    private static final int MENU_ID = R.id.scanCodeBtn;
    private SettingsImportExportActivity activity;

    public QRCodeScanHandler(SettingsImportExportActivity activity) {
        this.activity = activity;
    }

    @Override
    public void handleResult(Intent data, int resultCode) {
        IntentResult result = IntentIntegrator.parseActivityResult(RequestCode.QR_CODE_SCAN.getCode(), resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                ViewUtils.showCustomToast(activity, activity.getString(R.string.cancelled));

            } else {
                try {
                    String decompressedSettings = CompressionUtils.decompress(result.getContents());
                    activity.importSettings(decompressedSettings);

                    activity.setResult(Activity.RESULT_OK);
                    activity.finish();
                } catch (IOException | DataFormatException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
        }
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == RequestCode.QR_CODE_SCAN.getCode();
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        new IntentIntegrator(activity)
                .setBeepEnabled(true)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                .setOrientationLocked(false)
                .initiateScan();

        return true;
    }
}
