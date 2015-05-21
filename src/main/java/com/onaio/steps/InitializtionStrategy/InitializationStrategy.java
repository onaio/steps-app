package com.onaio.steps.InitializtionStrategy;


import android.app.Activity;
import android.content.Intent;

public class InitializationStrategy {

    public Intent getIntent(FlowType flowType, Activity activity){
        return new Intent(activity,flowType.getAssociatedActivityClass());
    }
}
