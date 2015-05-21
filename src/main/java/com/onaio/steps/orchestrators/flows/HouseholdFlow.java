package com.onaio.steps.orchestrators.flows;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.activity.HouseholdListActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;

import static com.onaio.steps.helper.Constants.ENDPOINT_URL;
import static com.onaio.steps.helper.Constants.FLOW_TYPE;
import static com.onaio.steps.helper.Constants.FORM_ID;
import static com.onaio.steps.helper.Constants.HOUSEHOLD_SEED;
import static com.onaio.steps.helper.Constants.MAX_AGE;
import static com.onaio.steps.helper.Constants.MIN_AGE;
import static com.onaio.steps.helper.Constants.PHONE_ID;


public class HouseholdFlow implements IFlow {
    private Activity activity;

    public HouseholdFlow(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean canHandle(FlowType flowType) {
        return FlowType.Household.equals(flowType);
    }

    @Override
    public void prepareSettingScreen(){
        prepareView();
        populateData();
    }

    @Override
    public void saveSettings() {
        saveData(R.id.deviceId,PHONE_ID);
        saveData(R.id.form_id,FORM_ID);
        saveData(R.id.min_age,MIN_AGE);
        saveData(R.id.max_age,MAX_AGE);
        saveData(R.id.endpointUrl,ENDPOINT_URL);
        saveData(R.id.household_seed,HOUSEHOLD_SEED);
        saveSafely(activity, FLOW_TYPE, FlowType.Household.toString());
    }

    @Override
    public Intent getIntent() {
        return new Intent(activity, HouseholdListActivity.class);
    }

    private void populateData() {
        setData(R.id.deviceId,Constants.PHONE_ID);
        setData(R.id.form_id,Constants.FORM_ID);
        setData(R.id.min_age,Constants.MIN_AGE);
        setData(R.id.max_age,Constants.MAX_AGE);
        setData(R.id.household_seed,Constants.HOUSEHOLD_SEED);
        setData(R.id.endpointUrl,Constants.ENDPOINT_URL);
    }

    private void setData(int viewId, String keyId){
        String data = getValue(activity, keyId);
        TextView textView = (TextView) activity.findViewById(viewId);
        textView.setText(data);
    }

    private void prepareView() {
        hide(R.id.participant_flow_disabled);
        hide(R.id.household_flow);
    }

    private void hide(int viewId) {
        View viewElement = activity.findViewById(viewId);
        viewElement.setVisibility(View.GONE);
    }


    private void saveData(int viewId, String keyId){
        TextView textView = (TextView) activity.findViewById(viewId);
        String data = textView.getText().toString();
        saveSafely(activity,keyId,data);
    }

    private void saveSafely(Activity activity, String key, String value) {
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(activity);
        if (!keyValueStore.putString(key, value))
            saveSettingsErrorHandler(key);
    }

    private void saveSettingsErrorHandler(String field) {
        //TODO: toast message for save phone id failure
    }

    private String getValue(Activity activity, String key) {
        return KeyValueStoreFactory.instance(activity).getString(key);
    }
}
