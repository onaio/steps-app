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

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.activities.MainActivityOrchestrator;
import com.onaio.steps.activities.SettingsActivity;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.RequestCode;
import com.onaio.steps.orchestrators.flows.FlowType;


public class SettingActivityHandler implements IMenuHandler, IActivityResultHandler {

    private FlowType flowType;
    private AppCompatActivity activity;

    public SettingActivityHandler(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.action_settings || menu_id == R.id.go_to_settings;
    }

    @Override
    public boolean open() {
        Intent intent = new Intent(activity, SettingsActivity.class);
        intent.putExtra(Constants.FLOW_TYPE,flowType.toString());
        activity.startActivityForResult(intent, RequestCode.SETTINGS.getCode());
        return true;
    }

    public SettingActivityHandler prepareFor(FlowType flowType) {
        this.flowType = flowType;
        return this;
    }

    @Override
    public void handleResult(Intent data, int resultCode) {
        if (resultCode != Activity.RESULT_OK)
            return;
        activity.startActivity(new Intent(activity, MainActivityOrchestrator.class));
        activity.finish();
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return RequestCode.SETTINGS.getCode() == requestCode;
    }
}
