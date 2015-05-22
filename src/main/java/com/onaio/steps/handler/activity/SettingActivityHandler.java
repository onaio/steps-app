package com.onaio.steps.handler.activity;

import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.R;
import com.onaio.steps.activity.MainActivityOrchestrator;
import com.onaio.steps.activity.SettingsActivity;
import com.onaio.steps.handler.Interface.IActivityResultHandler;
import com.onaio.steps.handler.Interface.IMenuHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.RequestCode;


public class SettingActivityHandler implements IMenuHandler, IActivityResultHandler {

    private FlowType flowType;
    private Activity activity;

    public SettingActivityHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.action_settings || menu_id == R.id.go_to_settings;
    }

    @Override
    public boolean open() {
        Intent intent = new Intent(activity, SettingsActivity.class);
        intent.putExtra(Constants.FLOW_TYPE,flowType.toString());
        activity.startActivityForResult(intent, RequestCode.SETTINGS.getCode());
        return true;
    }

    public SettingActivityHandler prepareFor(FlowType flowType){
        this.flowType = flowType;
        return this;
    }

    @Override
    public void handleResult(Intent data, int resultCode) {
        if (resultCode != Activity.RESULT_OK)
            return;
        activity.startActivity(new Intent(activity, MainActivityOrchestrator.class));
        activity.finish();
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return RequestCode.SETTINGS.getCode() == requestCode;
    }
}
