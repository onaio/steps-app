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

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.handler.factories.SettingsImportExportActivityFactory;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.listeners.QRBitmapGeneratorListener;
import com.onaio.steps.utils.QRCodeUtils;
import com.onaio.steps.utils.ViewUtils;

import java.util.List;
import java.util.zip.DataFormatException;

public class SettingsImportExportActivity extends AppCompatActivity {

    public static final String TAG = SettingsImportExportActivity.class.getName();

    private Bitmap qrCodeBitmap = null;

    private List<IMenuHandler> iMenuHandlers;
    private List<IActivityResultHandler> iActivityResultHandlers;
    private List<IMenuHandler> iCustomMenuHandlers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_import_export);

        generateSettingsQRCode();

        iMenuHandlers = SettingsImportExportActivityFactory.getMenuHandlers(this);
        iCustomMenuHandlers = SettingsImportExportActivityFactory.getCustomMenuHandler(this);
        iActivityResultHandlers = SettingsImportExportActivityFactory.getResultHandlers(this);
    }

    protected void generateSettingsQRCode() {
        // Should generate the QR Code and display it
        QRCodeUtils.generateSettingQRCode(this, new QRBitmapGeneratorListener() {
            @Override
            public void onBitmapGenerated(Bitmap bitmap) {
                ImageView qrCodeImg = (ImageView) findViewById(R.id.qrCodeImg);
                qrCodeImg.setImageBitmap(bitmap);

                qrCodeBitmap = bitmap;
                SettingsImportExportActivity.this.invalidateOptionsMenu();
            }

            @Override
            public void onError(Exception e) {
                ViewUtils.showCustomToast(SettingsImportExportActivity.this, getString(R.string.error_generating_qr_code));
                SettingsImportExportActivity.this.invalidateOptionsMenu();
            }
        });
    }

    public void scanCode(View view) {
        for(IMenuHandler iCustomMenuHandler: iCustomMenuHandlers) {
            if (iCustomMenuHandler.shouldOpen(view.getId())) {
                iCustomMenuHandler.open();
            }
        }
    }

    public void importCodeFromSDCard(View view) {
        for(IMenuHandler iCustomMenuHandler: iCustomMenuHandlers) {
            if (iCustomMenuHandler.shouldOpen(view.getId())) {
                iCustomMenuHandler.open();
            }
        }
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (IActivityResultHandler iActivityResultHandler: iActivityResultHandlers) {
            if (iActivityResultHandler.canHandleResult(requestCode)) {
                iActivityResultHandler.handleResult(data, resultCode);
            }
        }
    }

    public Bitmap getQrCodeBitmap() {
        return qrCodeBitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.export_import_settings, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        for (IMenuHandler iMenuHandler: iMenuHandlers) {
            if (iMenuHandler.shouldOpen(item.getItemId())) {
                return iMenuHandler.open();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        List<IMenuPreparer> iMenuPreparers = SettingsImportExportActivityFactory.getCustomMenuPreparer(this, menu);
        for (IMenuPreparer iMenuPreparer: iMenuPreparers) {
            if (iMenuPreparer.shouldDeactivate()) {
                iMenuPreparer.deactivate();
            } else {
                iMenuPreparer.activate();
            }
        }

        return true;
    }

    public void importSettings(String compressedSettings) {
        try {
            if (!QRCodeUtils.importSettingsFromJSON(SettingsImportExportActivity.this, compressedSettings)) {
                throw new DataFormatException("JSON Format is Incorrect");
            }
        } catch (DataFormatException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }
}
