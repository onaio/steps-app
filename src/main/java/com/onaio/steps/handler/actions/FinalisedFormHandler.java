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

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.model.RequestCode;

public class FinalisedFormHandler implements IMenuHandler{
   private AppCompatActivity activity;
    private static final int MENU_ID= R.id.action_saved_form;


    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    public FinalisedFormHandler(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean open() {
        launchODKCollect();
        return true;
    }

    private void launchODKCollect() {
        try {
            Intent surveyIntent = new Intent();
            surveyIntent.setComponent(new ComponentName(Constants.ODK_COLLECT_PACKAGE, Constants.ODK_COLLECT_UPLOADER_CLASS));
            surveyIntent.setAction(Intent.ACTION_EDIT);
            activity.startActivityForResult(surveyIntent, RequestCode.DATA_SUBMISSION.getCode());
        } catch (ActivityNotFoundException e) {
            new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.error_title, R.string.odk_app_not_installed);
        }
    }
}
