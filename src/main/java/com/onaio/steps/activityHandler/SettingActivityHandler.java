package com.onaio.steps.activityHandler;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.InitializtionStrategy.InitialFlows;
import com.onaio.steps.R;
import com.onaio.steps.activity.MainActivityOrchestrator;
import com.onaio.steps.activity.SettingsActivity;
import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.RequestCode;

import static android.app.Activity.RESULT_OK;
import static com.onaio.steps.helper.Constants.ENDPOINT_URL;
import static com.onaio.steps.helper.Constants.FLOW_TYPE;
import static com.onaio.steps.helper.Constants.FORM_ID;
import static com.onaio.steps.helper.Constants.HOUSEHOLD_SEED;
import static com.onaio.steps.helper.Constants.MAX_AGE;
import static com.onaio.steps.helper.Constants.MIN_AGE;
import static com.onaio.steps.helper.Constants.PHONE_ID;


public class SettingActivityHandler implements IMenuHandler, IActivityResultHandler {

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
        intent.putExtra(PHONE_ID, getValue(PHONE_ID));
        intent.putExtra(FORM_ID,getValue(FORM_ID));
        intent.putExtra(ENDPOINT_URL, getValue(ENDPOINT_URL));
        intent.putExtra(HOUSEHOLD_SEED, getValue(HOUSEHOLD_SEED));
        intent.putExtra(MIN_AGE, getValue(MIN_AGE));
        intent.putExtra(MAX_AGE, getValue(MAX_AGE));
        activity.startActivityForResult(intent, RequestCode.SETTINGS.getCode());
        return true;
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == RequestCode.SETTINGS.getCode();
    }

    @Override
    public void handleResult(Intent data, int resultCode) {
        if (resultCode == RESULT_OK)
            handleSuccess(data);
    }

    private void handleSuccess(Intent data) {
        String phoneId = data.getStringExtra(PHONE_ID);
        String formId = data.getStringExtra(FORM_ID);
        String endpointUrl = data.getStringExtra(ENDPOINT_URL);
        String householdSeed = data.getStringExtra(HOUSEHOLD_SEED);
        String minAge = data.getStringExtra(MIN_AGE);
        String maxAge = data.getStringExtra(MAX_AGE);

        saveSafely(PHONE_ID, phoneId);
        saveSafely(FORM_ID,formId);
        saveSafely(ENDPOINT_URL, endpointUrl);
        saveSafely(HOUSEHOLD_SEED, householdSeed);
        saveSafely(MIN_AGE, minAge);
        saveSafely(MAX_AGE, maxAge);
        saveSafely(FLOW_TYPE, InitialFlows.Household.toString());

        activity.startActivity(new Intent(activity,MainActivityOrchestrator.class));
        activity.finish();
    }

    private void saveSafely(String key, String value) {
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(activity);
        if (!keyValueStore.putString(key, value))
            saveSettingsErrorHandler(key);
    }

    private void saveSettingsErrorHandler(String field) {
        //TODO: toast message for save phone id failure
    }

    private String getValue(String key) {
        return KeyValueStoreFactory.instance(activity).getString(key) ;
    }

}
