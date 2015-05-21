package com.onaio.steps.orchestrators.flows;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.onaio.steps.R;
import com.onaio.steps.activity.WelcomeActivity;

public class InitialFlow implements IFlow {
    private Activity activity;

    public InitialFlow(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean canHandle(FlowType flowType) {
        return flowType.equals(FlowType.None);
    }

    @Override
    public void prepareSettingScreen() {
        prepareView();
    }

    @Override
    public void saveSettings() {
    }

    @Override
    public Intent getIntent() {
        return new Intent(activity, WelcomeActivity.class);
    }

    private void prepareView() {
        View settingContents = activity.findViewById(R.id.setting_contents);
        Button householdFlowDisabled = (Button)activity.findViewById(R.id.household_flow_disabled);
        Button participantFlowDisabled = (Button)activity.findViewById(R.id.participant_flow_disabled);
        settingContents.setVisibility(View.GONE);
        householdFlowDisabled.setVisibility(View.GONE);
        participantFlowDisabled.setVisibility(View.GONE);
    }

}
