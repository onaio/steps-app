package com.onaio.steps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;

import com.onaio.steps.orchestrators.FlowOrchestrator;
import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;

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
        flowOrchestrator.saveSettings(flowType);
        setResult(RESULT_OK, this.getIntent());
        finish();
    }

    public void cancel(View view){
        finish();
    }

//    private boolean isValid(String value) {
//        return value != null && !value.equals("");
//    }

    public void enableHouseholdFlow(View view){
        flowType = FlowType.Household;
        setHeader();
        prepareViewWithData();
    }

    public void enableParticipantFlow(View view){
        flowType = FlowType.Participant;
        setHeader();
        prepareViewWithData();
    }

}
