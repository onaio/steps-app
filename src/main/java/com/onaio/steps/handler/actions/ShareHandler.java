package com.onaio.steps.handler.actions;

import android.content.Intent;
import android.net.Uri;

import com.onaio.steps.R;
import com.onaio.steps.activities.SettingsImportExportActivity;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.listeners.QRBitmapSaveListener;
import com.onaio.steps.tasks.SaveQRCodeAsyncTask;
import com.onaio.steps.utils.QRCodeUtils;
import com.onaio.steps.utils.ViewUtils;

public class ShareHandler implements IMenuHandler {

    private SettingsImportExportActivity settingsImportExportActivity;

    public ShareHandler(SettingsImportExportActivity settingsImportExportActivity) {
        this.settingsImportExportActivity = settingsImportExportActivity;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.menu_item_settings_share;
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
}
