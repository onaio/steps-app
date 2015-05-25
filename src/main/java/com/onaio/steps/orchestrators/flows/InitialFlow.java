package com.onaio.steps.orchestrators.flows;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activities.WelcomeActivity;
import com.onaio.steps.exceptions.InvalidDataException;

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
    public boolean validateOptions() throws InvalidDataException {
        return false;
    }

    @Override
    public void saveSettings() {
    }

    @Override
    public Intent getIntent() {
        return new Intent(activity, WelcomeActivity.class);
    }

    private void prepareView() {
        hide(R.id.setting_contents);
        hide(R.id.household_flow_disabled);
        hide(R.id.participant_flow_disabled);
    }

    private void hide(int viewId) {
        View viewElement = activity.findViewById(viewId);
        viewElement.setVisibility(View.GONE);
    }

}
