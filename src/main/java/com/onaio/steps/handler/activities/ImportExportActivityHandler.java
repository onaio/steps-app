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

import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activities.SettingsActivity;
import com.onaio.steps.activities.SettingsImportExportActivity;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.model.RequestCode;
import com.onaio.steps.utils.ViewUtils;

public class ImportExportActivityHandler implements IMenuHandler, IActivityResultHandler {

    private static final int MENU_ID = R.id.exportSettings;
    private SettingsActivity activity;

    public ImportExportActivityHandler(SettingsActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        activity.startActivityForResult(new Intent(activity, SettingsImportExportActivity.class), RequestCode.IMPORT_EXPORT_SETTINGS.getCode());
        return true;
    }

    @Override
    public void handleResult(Intent data, int resultCode) {
        if (resultCode == Activity.RESULT_OK) {
            ViewUtils.showCustomToast(activity, activity.getString(R.string.import_qr_code_success_msg));
        }

        // This assures that the other tab data is also refreshed
        activity.setJustOpened(true);
        activity.enableHouseholdFlow(null, true);
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == RequestCode.IMPORT_EXPORT_SETTINGS.getCode();
    }
}
