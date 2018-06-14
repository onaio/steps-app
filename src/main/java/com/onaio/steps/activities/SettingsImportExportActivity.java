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

package com.onaio.steps.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.onaio.steps.R;
import com.onaio.steps.listeners.QRBitmapGeneratorListener;
import com.onaio.steps.utils.CompressionUtils;
import com.onaio.steps.utils.QRCodeUtils;
import com.onaio.steps.utils.ViewUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;

public class SettingsImportExportActivity extends Activity {

    public static final String TAG = SettingsImportExportActivity.class.getName();
    public static final int PICK_IMAGE_REQUEST_CODE = 89;
    public static final int SCAN_REQUEST_CODE = IntentIntegrator.REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_import_export);

        // Should generate the QR Code and display it
        QRCodeUtils.generateSettingQRCode(this, new QRBitmapGeneratorListener() {
            @Override
            public void onBitmapGenerated(Bitmap bitmap) {
                ImageView qrCodeImg = (ImageView) findViewById(R.id.qrCodeImg);
                qrCodeImg.setImageBitmap(bitmap);
            }

            @Override
            public void onError(Exception e) {
                /*Toast.makeText(SettingsImportExportActivity.this, "An error occured generating the settings", Toast.LENGTH_LONG)
                        .show();*/
                ViewUtils.showCustomToast(SettingsImportExportActivity.this, getString(R.string.error_generating_qr_code));
            }
        });
    }

    public void scanCode(View view) {
        new IntentIntegrator(this)
                .setBeepEnabled(true)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                .setOrientationLocked(false)
                .initiateScan();
    }

    public void importCodeFromSDCard(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select QR Code"), PICK_IMAGE_REQUEST_CODE);
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SCAN_REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    /*Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();*/
                    ViewUtils.showCustomToast(SettingsImportExportActivity.this, getString(R.string.cancelled));

                } else {
                    try {
                        String decompressedSettings = CompressionUtils.decompress(result.getContents());
                        importSettings(decompressedSettings);
                        Log.i(TAG, "Import text: " + result.getContents());

                        setResult(RESULT_OK);
                        finish();
                    } catch (IOException | DataFormatException e) {
                        Log.e(TAG, Log.getStackTraceString(e));
                    }
                }
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                // Process the image
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver()
                        .openInputStream(imageUri);

                final Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                String response = QRCodeUtils.decodeFromBitmap(bitmap);
                Log.i(TAG, "Import text: " + response);

                importSettings(response);
            } catch (DataFormatException | IOException | FormatException | ChecksumException | NotFoundException e) {
                Log.e(TAG, Log.getStackTraceString(e));
                /*Toast.makeText(this, R.string.import_qr_code_error_msg, Toast.LENGTH_LONG)
                        .show();*/
                ViewUtils.showCustomToast(this, getString(R.string.import_qr_code_error_msg));
            }
        }
    }

    private void importSettings(String compressedSettings) {
        try {
            if (!QRCodeUtils.importSettingsFromJSON(SettingsImportExportActivity.this, compressedSettings)) {
                throw new DataFormatException("JSON Format is Incorrect");
            }
        } catch (DataFormatException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }
}
