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

package com.onaio.steps.handler.activities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.SettingsActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.RequestCode;
import com.onaio.steps.orchestrators.flows.FlowType;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowToast;

public class ImportExportActivityHandlerTest extends StepsTestRunner {

    private ImportExportActivityHandler importExportActivityHandler;
    private SettingsActivity settingsActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra(Constants.FLOW_TYPE, FlowType.Household.toString());

        settingsActivity = Robolectric.buildActivity(SettingsActivity.class, intent)
                .create()
                .get();
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
        String toastText = ((TextView) latestToast.getView()
                .findViewById(R.id.text)).getText().toString();
        assertEquals(settingsActivity.getString(R.string.import_qr_code_success_msg), toastText);
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