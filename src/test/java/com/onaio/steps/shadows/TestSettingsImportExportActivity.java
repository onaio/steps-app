package com.onaio.steps.shadows;

import android.os.AsyncTask;

import com.onaio.steps.activities.SettingsImportExportActivity;
import com.onaio.steps.tasks.GenerateQRCodeAsyncTask;
import com.onaio.steps.utils.QRCodeUtils;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowAsyncTask;

public class TestSettingsImportExportActivity extends SettingsImportExportActivity {


    @Override
    protected void generateSettingsQRCode() {

    }
}
