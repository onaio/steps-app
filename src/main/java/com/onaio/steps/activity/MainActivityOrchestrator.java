package com.onaio.steps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.onaio.steps.InitializtionStrategy.FlowType;
import com.onaio.steps.InitializtionStrategy.InitializationStrategy;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStoreFactory;

public class MainActivityOrchestrator extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlowType flowType = FlowType.valueOf(getValue(Constants.FLOW_TYPE));
        Intent intent = new InitializationStrategy().getIntent(flowType, this);
        this.startActivity(intent);
        finish();
    }

    private String getValue(String key) {
        return KeyValueStoreFactory.instance(this).getString(key) ;
    }
}
