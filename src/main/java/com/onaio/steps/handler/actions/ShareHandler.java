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

import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activities.SettingsImportExportActivity;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.listeners.QRBitmapSaveListener;
import com.onaio.steps.tasks.SaveQRCodeAsyncTask;
import com.onaio.steps.utils.QRCodeUtils;
import com.onaio.steps.utils.ViewUtils;

public class ShareHandler implements IMenuHandler, IMenuPreparer {

    private SettingsImportExportActivity settingsImportExportActivity;
    private boolean qrDisplayed;
    private static final int MENU_ID = R.id.menu_item_settings_share;
    private Menu menu;

    public ShareHandler(SettingsImportExportActivity settingsImportExportActivity, boolean qrDisplayed) {
        this.settingsImportExportActivity = settingsImportExportActivity;
        this.qrDisplayed = qrDisplayed;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        SaveQRCodeAsyncTask saveQRCodeAsyncTask = new SaveQRCodeAsyncTask(settingsImportExportActivity, settingsImportExportActivity.getQrCodeBitmap(), new QRBitmapSaveListener() {
            @Override
            public void onSuccessfulSave() {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + QRCodeUtils.QR_CODE_FILEPATH));

                settingsImportExportActivity.startActivity(Intent.createChooser(intent, settingsImportExportActivity.getString(R.string.share_qr_code_title)));
            }

            @Override
            public void onError(Exception e) {
                ViewUtils.showCustomToast(settingsImportExportActivity, settingsImportExportActivity.getString(R.string.qr_code_share_error));
            }
        });

        saveQRCodeAsyncTask.execute();
        return true;
    }

    public IMenuPreparer withMenu(Menu menu) {
        this.menu = menu;
        return this;
    }

    @Override
    public boolean shouldDeactivate() {
        return !qrDisplayed;
    }

    @Override
    public void deactivate() {
        MenuItem item = menu.findItem(MENU_ID);
        item.setVisible(false);
        item.setEnabled(false);
    }

    @Override
    public void activate() {
        MenuItem item = menu.findItem(MENU_ID);
        item.setVisible(true);
        item.setEnabled(true);
    }
}
