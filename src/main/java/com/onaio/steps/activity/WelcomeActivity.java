package com.onaio.steps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.R;
import com.onaio.steps.handler.activity.SettingActivityHandler;

public class WelcomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
    }

    public void openDefaultSetting(View view){
        new SettingActivityHandler(this).prepareFor(FlowType.None).open();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        new SettingActivityHandler(this).handleResult(data,resultCode);
    }
}
