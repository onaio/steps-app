package com.onaio.steps.orchestrators.flows;

import android.content.Intent;

import com.onaio.steps.exceptions.InvalidDataException;

public interface IFlow {
    boolean canHandle(FlowType flowType);

    void prepareSettingScreen();
    void validateOptions() throws InvalidDataException;

    void saveSettings();

    Intent getIntent();
}
