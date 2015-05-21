package com.onaio.steps.activityHandler.Settings;

import com.onaio.steps.InitializtionStrategy.FlowType;

public interface ISettingPreparer {
    boolean canHandle(FlowType flowType);

    public void prepare();

    public void save();
}
