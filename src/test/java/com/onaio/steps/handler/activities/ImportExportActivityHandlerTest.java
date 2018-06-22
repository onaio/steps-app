package com.onaio.steps.handler.activities;

import android.app.Activity;
import android.widget.Toast;

import com.onaio.steps.R;
import com.onaio.steps.activities.SettingsActivity;
import com.onaio.steps.model.RequestCode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.*;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ImportExportActivityHandlerTest {

    private ImportExportActivityHandler importExportActivityHandler;
    private SettingsActivity settingsActivity;

    @Before
    public void setUp() {
        settingsActivity = Robolectric.setupActivity(SettingsActivity.class);
        importExportActivityHandler = new ImportExportActivityHandler(settingsActivity);
    }

    @Test
    public void shouldOpen() {
        assertTrue(importExportActivityHandler.shouldOpen(R.id.exportSettings));
    }

    @Test
    public void shouldNotOpenWhenOtherActionIsChosen() {
        assertFalse(importExportActivityHandler.shouldOpen(R.id.importButton));
        assertFalse(importExportActivityHandler.shouldOpen(R.id.action_export));
    }

    @Test
    public void handleResultShouldShowSuccessToastWhenResultOk() {
        importExportActivityHandler.handleResult(null, Activity.RESULT_OK);

        Toast latestToast = ShadowToast.getLatestToast();
        assertNotNull(latestToast);
        assertEquals(settingsActivity.getString(R.string.import_qr_code_success_msg), ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void handleResultShouldNotShowToastWhenResultCancelled() {
        importExportActivityHandler.handleResult(null, Activity.RESULT_CANCELED);

        Toast latestToast = ShadowToast.getLatestToast();
        assertNull(latestToast);
    }

    @Test
    public void canHandleResultForExportImportSettingsPageRequestCode() {
        assertTrue(importExportActivityHandler.canHandleResult(RequestCode.IMPORT_EXPORT_SETTINGS.getCode()));
    }

    @Test
    public void cannotHandleResultForOtherRequestCodes() {
        assertFalse(importExportActivityHandler.canHandleResult(RequestCode.EDIT_MEMBER.getCode()));
    }
}