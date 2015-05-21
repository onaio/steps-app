package com.onaio.steps.activityHandler.Settings;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onaio.steps.InitializtionStrategy.FlowType;
import com.onaio.steps.R;
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


public class HouseholdSettingPreparer implements ISettingPreparer{
    private Activity activity;

    public HouseholdSettingPreparer(Activity activity) {

        this.activity = activity;
    }

    @Override
    public boolean canHandle(FlowType flowType) {
        return FlowType.Household.equals(flowType);
    }

    @Override
    public void prepare(){
        prepareView();
        populateData();
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
        Button householdFlow = (Button)activity.findViewById(R.id.household_flow);
        householdFlow.setVisibility(View.GONE);
        Button participantDisabledFlow = (Button)activity.findViewById(R.id.participant_flow_disabled);
        participantDisabledFlow.setVisibility(View.GONE);
    }

    @Override
    public void save() {
        saveData(R.id.deviceId,PHONE_ID);
        saveData(R.id.form_id,FORM_ID);
        saveData(R.id.min_age,MIN_AGE);
        saveData(R.id.max_age,MAX_AGE);
        saveData(R.id.endpointUrl,ENDPOINT_URL);
        saveData(R.id.household_seed,HOUSEHOLD_SEED);
        saveSafely(activity, FLOW_TYPE, FlowType.Household.toString());

//        activity.startActivity(new Intent(activity, MainActivityOrchestrator.class));
//        activity.finish();
    }

    private void saveData(int viewId, String keyId){
        TextView textView = (TextView) activity.findViewById(viewId);
        String data = textView.getText().toString();
        saveSafely(activity,keyId,data);
    }

//    @Override
//    public void start() {
//        Intent intent = new Intent(activity, SettingsActivity.class);
////        intent.putExtra(PHONE_ID, getValue(activity,PHONE_ID));
////        intent.putExtra(FORM_ID, getValue(activity, FORM_ID));
////        intent.putExtra(ENDPOINT_URL, getValue(activity, ENDPOINT_URL));
////        intent.putExtra(HOUSEHOLD_SEED, getValue(activity, HOUSEHOLD_SEED));
////        intent.putExtra(MIN_AGE, getValue(activity, MIN_AGE));
////        intent.putExtra(MAX_AGE, getValue(activity, MAX_AGE));
////        intent.putExtra(FLOW_TYPE,FlowType.Household.toString());
//        activity.startActivityForResult(intent, RequestCode.SETTINGS.getCode());
//    }

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
