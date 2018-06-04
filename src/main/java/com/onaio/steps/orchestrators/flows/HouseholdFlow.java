/*
 * Copyright 2016. World Health Organization
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onaio.steps.orchestrators.flows;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.activities.HouseholdListActivity;
import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DataValidator;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;

import java.util.ArrayList;
import java.util.List;

import static com.onaio.steps.helper.Constants.ENDPOINT_URL;
import static com.onaio.steps.helper.Constants.FLOW_TYPE;
import static com.onaio.steps.helper.Constants.HH_FORM_ID;
import static com.onaio.steps.helper.Constants.HH_HOUSEHOLD_SEED;
import static com.onaio.steps.helper.Constants.HH_MAX_AGE;
import static com.onaio.steps.helper.Constants.HH_MIN_AGE;
import static com.onaio.steps.helper.Constants.HH_PHONE_ID;
import static com.onaio.steps.helper.Constants.HH_SURVEY_ID;
import static com.onaio.steps.helper.Constants.IMPORT_URL;


public class HouseholdFlow implements IFlow {
    private Activity activity;
    protected List<String> errorFields;

    public HouseholdFlow(Activity activity) {
        this.activity = activity;
        errorFields = new ArrayList<String>();

    }

    @Override
    public boolean canHandle(FlowType flowType) {
        return FlowType.Household.equals(flowType);
    }

    @Override
    public void prepareSettingScreen() {
        prepareView();
        populateData();
    }

    @Override
    public void saveSettings() {
        saveData(R.id.campaignId, HH_SURVEY_ID);
        saveData(R.id.deviceId, HH_PHONE_ID);
        saveData(R.id.form_id, HH_FORM_ID);
        saveData(R.id.min_age, HH_MIN_AGE);
        saveData(R.id.max_age, HH_MAX_AGE);
        saveData(R.id.endpointUrl, ENDPOINT_URL);
        saveData(R.id.importUrl, IMPORT_URL);
        saveData(R.id.household_seed, HH_HOUSEHOLD_SEED);
        saveSafely(activity, FLOW_TYPE, FlowType.Household.toString());
    }

    @Override
    public Intent getIntent() {
        return new Intent(activity, HouseholdListActivity.class);
    }

    @Override
    public void validateOptions() throws InvalidDataException {
        String surveyIdValue = ((TextView) activity.findViewById(R.id.campaignId)).getText().toString().trim();
        String deviceIdValue = ((TextView) activity.findViewById(R.id.deviceId)).getText().toString().trim();
        String formIdValue = ((TextView) activity.findViewById(R.id.form_id)).getText().toString().trim();
        String minAgeValue = ((TextView) activity.findViewById(R.id.min_age)).getText().toString().trim();
        String maxAgeValue = ((TextView) activity.findViewById(R.id.max_age)).getText().toString().trim();

        errorFields = new DataValidator(activity).
                validate(surveyIdValue, getStringValue(R.string.survey_id_label)).
                validate(deviceIdValue, getStringValue(R.string.device_id_label)).
                validate(formIdValue, getStringValue(R.string.form_id)).
                validate(minAgeValue, getStringValue(R.string.min_age)).
                validate(maxAgeValue, getStringValue(R.string.max_age)).
                finish();
        if (errorFields != null && !errorFields.isEmpty())
            throw new InvalidDataException(activity, getStringValue(R.string.action_settings), errorFields);

    }

    private void populateData() {
        setData(R.id.campaignId, Constants.HH_SURVEY_ID);
        setData(R.id.deviceId, Constants.HH_PHONE_ID);
        setData(R.id.form_id, Constants.HH_FORM_ID);
        setData(R.id.min_age, Constants.HH_MIN_AGE);
        setData(R.id.max_age, Constants.HH_MAX_AGE);
        setData(R.id.household_seed, Constants.HH_HOUSEHOLD_SEED);
        setData(R.id.endpointUrl, Constants.ENDPOINT_URL);
        setData(R.id.importUrl, Constants.IMPORT_URL);
    }

    private void setData(int viewId, String keyId) {
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


    private void saveData(int viewId, String keyId) {
        TextView textView = (TextView) activity.findViewById(viewId);
        String data = textView.getText().toString().trim();
        saveSafely(activity, keyId, data);
    }

    public void saveSafely(Activity activity, String key, String value) {
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

    protected String getStringValue(int value) {
        return activity.getString(value);
    }

}
