package com.onaio.steps.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.provider.DocumentFile;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.orchestrators.FlowOrchestrator;
import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;

import java.io.OutputStream;

import static android.app.PendingIntent.getActivity;

public class SettingsActivity extends Activity {

    private FlowType flowType;
    private FlowOrchestrator flowOrchestrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

}
