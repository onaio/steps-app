package com.onaio.steps.activities;

import android.app.Activity;
import android.os.Bundle;

import com.onaio.steps.orchestrators.FlowOrchestrator;
import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStoreFactory;

public class MainActivityOrchestrator extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlowType flowType = FlowType.valueOf(getValue(Constants.FLOW_TYPE));
        new FlowOrchestrator(this).start(flowType);
        finish();
    }

    private String getValue(String key) {
        return KeyValueStoreFactory.instance(this).getString(key) ;
    }
}
