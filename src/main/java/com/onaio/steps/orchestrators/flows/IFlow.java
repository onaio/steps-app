package com.onaio.steps.orchestrators.flows;

import android.content.Intent;

public interface IFlow {
    boolean canHandle(FlowType flowType);

    public void prepareSettingScreen();

    public void saveSettings();

    public Intent getIntent();
}
