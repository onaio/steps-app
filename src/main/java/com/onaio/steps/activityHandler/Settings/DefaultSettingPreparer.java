package com.onaio.steps.activityHandler.Settings;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import com.onaio.steps.InitializtionStrategy.FlowType;
import com.onaio.steps.R;

public class DefaultSettingPreparer implements ISettingPreparer {
    private Activity activity;

    public DefaultSettingPreparer(Activity activity) {

        this.activity = activity;
    }

    @Override
    public boolean canHandle(FlowType flowType) {
        return flowType.equals(FlowType.None);
    }

    @Override
    public void prepare() {
        prepareView();
    }

    private void prepareView() {
        View settingContents = activity.findViewById(R.id.setting_contents);
        Button householdFlowDisabled = (Button)activity.findViewById(R.id.household_flow_disabled);
        Button participantFlowDisabled = (Button)activity.findViewById(R.id.participant_flow_disabled);
        settingContents.setVisibility(View.GONE);
        householdFlowDisabled.setVisibility(View.GONE);
        participantFlowDisabled.setVisibility(View.GONE);
    }

    @Override
    public void save() {
    }
}
