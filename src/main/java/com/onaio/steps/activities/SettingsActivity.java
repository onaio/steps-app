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

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.exceptions.AppNotInstalledException;
import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.handler.actions.ImportHandler;
import com.onaio.steps.handler.activities.ImportExportActivityHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.ODKForm.ODKBlankForm;
import com.onaio.steps.orchestrators.FlowOrchestrator;
import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private FlowType flowType;
    private FlowOrchestrator flowOrchestrator;
    private DatabaseHelper db;
    private boolean justOpened = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        Intent intent = getIntent();
        flowType = FlowType.valueOf(intent.getStringExtra(Constants.FLOW_TYPE));
        flowOrchestrator = new FlowOrchestrator(this);
        setContentView(R.layout.settings);
        findViewById(R.id.ic_save).setVisibility(View.VISIBLE);
        setHeader();
        prepareViewWithData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareAvailableFormList();
    }

    private void setHeader() {
        TextView header = (TextView) findViewById(R.id.form_header);
        header.setText(R.string.action_settings);
    }

    private void prepareViewWithData(boolean forceRefreshValues) {
        flowOrchestrator.prepareSettingScreen(flowType, forceRefreshValues);
        if (justOpened) {
            flowOrchestrator.prepareOtherScreenData(flowType, forceRefreshValues);
            justOpened = false;
        }
    }

    private void prepareViewWithData() {
        prepareViewWithData(false);
    }

    public void saveBtnClicked(View view) {
        save(false);
    }

    public void doneBtnClicked(View view) {
        save(true);
    }

    private void save(boolean finishAfterSaving) {
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (flowType == FlowType.Household) {
                    enableParticipantFlow(null);
                } else {
                    enableHouseholdFlow(null);
                }
            }
        };

        try {
            if (flowType != FlowType.None) {
                flowOrchestrator.saveSettings(flowType == FlowType.Household ? FlowType.Participant : FlowType.Household, false);
            }
        } catch (InvalidDataException e) {
            new CustomDialog().notify(this, onClickListener, e.getMessage(), R.string.error_title);
            return;
        }

        try {
            flowOrchestrator.saveSettings(flowType, true);
            setResult(RESULT_OK, this.getIntent());

            if (finishAfterSaving) {
                finish();
            } else {
                ViewUtils.showCustomToast(this, getString(R.string.settings_saved_successfully));
            }

            flowOrchestrator.prepareOtherScreenData(flowType, true);
        } catch (InvalidDataException e) {
            new CustomDialog().notify(this, CustomDialog.EmptyListener, e.getMessage(), R.string.error_title);
        }
    }

    public void cancel(View view) {
        finish();
    }

    public void enableHouseholdFlow(View view, boolean forceRefreshValues) {
        if (flowType == FlowType.None) {
            findViewById(R.id.setting_contents)
                    .setVisibility(View.VISIBLE);
        }
        flowType = FlowType.Household;
        setHeader();
        prepareViewWithData(forceRefreshValues);
    }

    public void enableHouseholdFlow(View view) {
        enableHouseholdFlow(view, false);
    }

    public void enableParticipantFlow(View view) {
        if (flowType == FlowType.None) {
            findViewById(R.id.setting_contents)
                    .setVisibility(View.VISIBLE);
        }
        flowType = FlowType.Participant;
        setHeader();
        prepareViewWithData();
    }

    public void importData(View view) {
        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ImportHandler importHandler = new ImportHandler(SettingsActivity.this);
                importHandler.open(R.id.deviceId_household);
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

    public void exportSettings(View view) {
        ImportExportActivityHandler importExportActivityHandler = new ImportExportActivityHandler(this);
        importExportActivityHandler.open();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImportExportActivityHandler importExportActivityHandler = new ImportExportActivityHandler(this);
        if (importExportActivityHandler.canHandleResult(requestCode)) {
            importExportActivityHandler.handleResult(data, resultCode);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public boolean isJustOpened() {
        return justOpened;
    }

    public void setJustOpened(boolean justOpened) {
        this.justOpened = justOpened;
    }

    public void prepareAvailableFormList() {

        try {
            List<ODKBlankForm> forms = ODKBlankForm.get(this, null);

            int[] actvIdList = new int[] {R.id.form_id_household, R.id.form_id_participant};
            for (int j : actvIdList) {
                AutoCompleteTextView actvFormId = findViewById(j);
                actvFormId.setThreshold(1);
                actvFormId.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, forms));
                actvFormId.setOnItemClickListener((adapterView, view, i, l) -> {
                    ODKBlankForm selection = (ODKBlankForm) adapterView.getItemAtPosition(i);
                    actvFormId.setText(selection.getJrFormId());
                    actvFormId.setSelection(actvFormId.length());
                });
            }
        } catch (AppNotInstalledException e) {
            e.printStackTrace();
        }
    }
}
