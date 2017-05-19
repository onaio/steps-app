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

package com.onaio.steps.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.handler.actions.ImportHandler;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.orchestrators.FlowOrchestrator;
import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;

public class SettingsActivity extends Activity {

    private FlowType flowType;
    private FlowOrchestrator flowOrchestrator;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        Intent intent = getIntent();
        flowType = FlowType.valueOf(intent.getStringExtra(Constants.FLOW_TYPE));
        flowOrchestrator = new FlowOrchestrator(this);
        setHeader();
        prepareViewWithData();
    }

    private void setHeader() {
        setContentView(R.layout.settings);
        TextView header = (TextView) findViewById(R.id.form_header);
        header.setText(R.string.action_settings);
    }

    private void prepareViewWithData() {
        flowOrchestrator.prepareSettingScreen(flowType);
    }

    public void save(View view) {
        try {
                flowOrchestrator.saveSettings(flowType);
                setResult(RESULT_OK, this.getIntent());
                finish();
        } catch (InvalidDataException e) {
            new CustomDialog().notify(this, CustomDialog.EmptyListener, e.getMessage(), R.string.error_title);
        }
    }

    public void cancel(View view) {
        finish();
    }


    public void enableHouseholdFlow(View view) {
        flowType = FlowType.Household;
        setHeader();
        prepareViewWithData();
    }

    public void enableParticipantFlow(View view) {
        flowType = FlowType.Participant;
        setHeader();
        prepareViewWithData();
    }

    public void importData(View view) {
        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ImportHandler importHandler = new ImportHandler(SettingsActivity.this);
                importHandler.open(R.id.deviceId);
            }
        };
        new CustomDialog().confirm(this, confirmListener, CustomDialog.EmptyListener, R.string.warning_merging_data, R.string.warning_title);
    }

    public void eraseData(View view) {
        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.truncate();
                Toast.makeText(SettingsActivity.this, R.string.data_wipe_successful, Toast.LENGTH_LONG).show();
            }
        };
        new CustomDialog().confirm(this, confirmListener, CustomDialog.EmptyListener, R.string.warning_erasing_data, R.string.warning_title);
    }

}
