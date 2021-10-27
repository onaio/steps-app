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
import static com.onaio.steps.helper.Constants.HH_USER_ID;
import static com.onaio.steps.helper.Constants.IMPORT_URL;
import static com.onaio.steps.helper.Constants.PA_PHONE_ID;


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
    public void prepareSettingScreen(boolean forceRefreshValues) {
        prepareView();
        populateData(forceRefreshValues);
    }

    @Override
    public void saveSettings(boolean saveDeviceID) {
        saveData(R.id.campaignId_household, HH_SURVEY_ID);
        saveData(R.id.user_id_household, HH_USER_ID);

        if (saveDeviceID) {
            saveData(R.id.deviceId_household, HH_PHONE_ID);
            saveData(R.id.deviceId_household, PA_PHONE_ID);
        }

        saveData(R.id.form_id_household, HH_FORM_ID);
        saveData(R.id.min_age_household, HH_MIN_AGE);
        saveData(R.id.max_age_household, HH_MAX_AGE);
        saveData(R.id.endpointUrl_household, ENDPOINT_URL);
        saveData(R.id.importUrl_household, IMPORT_URL);
        saveData(R.id.household_seed_household, HH_HOUSEHOLD_SEED);
        saveSafely(activity, FLOW_TYPE, FlowType.Household.toString());
    }

    @Override
    public Intent getIntent() {
        return new Intent(activity, HouseholdListActivity.class);
    }

    @Override
    public void validateOptions(boolean checkDeviceID) throws InvalidDataException {
        String surveyIdValue = ((TextView) activity.findViewById(R.id.campaignId_household)).getText().toString().trim();
        String userIdValue = ((TextView) activity.findViewById(R.id.user_id_household)).getText().toString().trim();
        String deviceIdValue = ((TextView) activity.findViewById(R.id.deviceId_household)).getText().toString().trim();
        String formIdValue = ((TextView) activity.findViewById(R.id.form_id_household)).getText().toString().trim();
        String minAgeValue = ((TextView) activity.findViewById(R.id.min_age_household)).getText().toString().trim();
        String maxAgeValue = ((TextView) activity.findViewById(R.id.max_age_household)).getText().toString().trim();

        errorFields = validateHouseHoldSettings(surveyIdValue, userIdValue, deviceIdValue, formIdValue, minAgeValue, maxAgeValue, checkDeviceID);
        if (errorFields != null && !errorFields.isEmpty())
            throw new InvalidDataException(activity, getStringValue(R.string.action_settings), errorFields);
    }

    public List<String> validateHouseHoldSettings(String surveyId, String userId, String deviceId, String formId, String minAge, String maxAge, boolean checkDeviceID) {
        DataValidator dataValidator = new DataValidator(activity)
                .validate(surveyId, getStringValue(R.string.survey_id_label))
                .validate(userId, getStringValue(R.string.user_id_label));

        if (checkDeviceID) {
            dataValidator.validate(deviceId, getStringValue(R.string.device_id_label));
        }
        dataValidator.validate(formId, getStringValue(R.string.form_id)).
                validate(minAge, getStringValue(R.string.min_age)).
                validate(maxAge, getStringValue(R.string.max_age));


        return dataValidator.finish();
    }

    @Override
    public void populateData(boolean forceRefreshValues) {
        setData(R.id.campaignId_household, Constants.HH_SURVEY_ID, forceRefreshValues);
        setData(R.id.user_id_household, Constants.HH_USER_ID, forceRefreshValues);
        setData(R.id.deviceId_household, Constants.HH_PHONE_ID, forceRefreshValues);
        setData(R.id.form_id_household, Constants.HH_FORM_ID, forceRefreshValues);
        setData(R.id.min_age_household, Constants.HH_MIN_AGE, forceRefreshValues);
        setData(R.id.max_age_household, Constants.HH_MAX_AGE, forceRefreshValues);
        setData(R.id.household_seed_household, Constants.HH_HOUSEHOLD_SEED, forceRefreshValues);
        setData(R.id.endpointUrl_household, Constants.ENDPOINT_URL, forceRefreshValues);
        setData(R.id.importUrl_household, Constants.IMPORT_URL, forceRefreshValues);
    }

    private void setData(int viewId, String keyId, boolean forceRefreshValues) {
        String data = getValue(activity, keyId);
        TextView textView = (TextView) activity.findViewById(viewId);
        if (forceRefreshValues || (!forceRefreshValues && textView.getText().toString().trim().isEmpty())) {
            textView.setText(data);
        }
    }

    private void prepareView() {
        hide(R.id.participant_flow_disabled);
        show(R.id.participant_flow);
        hide(R.id.household_flow);
        hide(R.id.participantSettingsContent);
        show(R.id.household_flow_disabled);
        show(R.id.householdSettingsContent);
    }

    private void hide(int viewId) {
        View viewElement = activity.findViewById(viewId);
        viewElement.setVisibility(View.GONE);
    }

    private void show(int viewId) {
        View viewElement = activity.findViewById(viewId);
        viewElement.setVisibility(View.VISIBLE);
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

    public String getValue(Activity activity, String key) {
        return KeyValueStoreFactory.instance(activity).getString(key);
    }

    protected String getStringValue(int value) {
        return activity.getString(value);
    }

}
