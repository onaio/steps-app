package com.onaio.steps.orchestrators.flows;

import android.content.Intent;

import com.onaio.steps.exceptions.InvalidDataException;

public interface IFlow {
    boolean canHandle(FlowType flowType);

    public void prepareSettingScreen();
    public boolean validateOptions() throws InvalidDataException;

    public void saveSettings();

    public Intent getIntent();
}
