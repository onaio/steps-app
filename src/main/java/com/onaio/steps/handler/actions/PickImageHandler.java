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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.onaio.steps.R;
import com.onaio.steps.activities.SettingsImportExportActivity;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.model.RequestCode;
import com.onaio.steps.utils.QRCodeUtils;
import com.onaio.steps.utils.ViewUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;

import timber.log.Timber;


public class PickImageHandler implements IMenuHandler, IActivityResultHandler {

    private static final String TAG = PickImageHandler.class.getName();
    private static final int MENU_ID = R.id.importCodeImageBtn;
    private SettingsImportExportActivity activity;

    public PickImageHandler(SettingsImportExportActivity activity) {
        this.activity = activity;
    }

    @Override
    public void handleResult(Intent data, int resultCode) {
        if (resultCode == Activity.RESULT_OK) {
            try {
                // Process the image
                final Uri imageUri = data.getData();
                final InputStream imageStream = activity.getContentResolver()
                        .openInputStream(imageUri);

                final Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                String response = QRCodeUtils.decodeFromBitmap(bitmap);
                Timber.i("Import text: %s", response);

                activity.importSettings(response);
            } catch (DataFormatException | IOException | FormatException | ChecksumException | NotFoundException e) {
                Timber.e(e);
                ViewUtils.showCustomToast(activity, activity.getString(R.string.import_qr_code_error_msg));
            }
        }
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == RequestCode.PICK_IMAGE.getCode();
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.select_qr_code)), RequestCode.PICK_IMAGE.getCode());

        return true;
    }
}
