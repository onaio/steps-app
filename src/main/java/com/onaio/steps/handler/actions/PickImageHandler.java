package com.onaio.steps.handler.actions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

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


public class PickImageHandler implements IMenuHandler, IActivityResultHandler {

    private static final String TAG = PickImageHandler.class.getName();
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
                Log.i(TAG, "Import text: " + response);

                activity.importSettings(response);
            } catch (DataFormatException | IOException | FormatException | ChecksumException | NotFoundException e) {
                Log.e(TAG, Log.getStackTraceString(e));
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
        return menu_id == R.id.importCodeImageBtn;
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
