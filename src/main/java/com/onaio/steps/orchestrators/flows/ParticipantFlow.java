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
import com.onaio.steps.activities.ParticipantListActivity;
import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DataValidator;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;

import java.util.ArrayList;
import java.util.List;

import static com.onaio.steps.helper.Constants.FLOW_TYPE;
import static com.onaio.steps.helper.Constants.PA_FORM_ID;
import static com.onaio.steps.helper.Constants.PA_MAX_AGE;
import static com.onaio.steps.helper.Constants.PA_MIN_AGE;
import static com.onaio.steps.helper.Constants.PA_PHONE_ID;

public class ParticipantFlow implements IFlow {

    private Activity activity;
    protected List<String> errorFields;


    public ParticipantFlow(Activity activity) {
        errorFields = new ArrayList<String>();
        this.activity = activity;
    }

    @Override
    public boolean canHandle(FlowType flowType) {
        return FlowType.Participant.equals(flowType);
    }

    @Override
    public void validateOptions() throws InvalidDataException {
        String deviceIdValue = ((TextView) activity.findViewById(R.id.deviceId)).getText().toString().trim();
        String formIdValue = ((TextView) activity.findViewById(R.id.form_id)).getText().toString().trim();
        String minAgeValue = ((TextView) activity.findViewById(R.id.min_age)).getText().toString().trim();
        String maxAgeValue = ((TextView) activity.findViewById(R.id.max_age)).getText().toString().trim();

        errorFields = new DataValidator(activity).
                validate(deviceIdValue, getStringValue(R.string.device_id_label)).
                validate(formIdValue, getStringValue(R.string.form_id)).
                validate(minAgeValue, getStringValue(R.string.min_age)).
                validate(maxAgeValue, getStringValue(R.string.max_age)).
                finish();
        if (errorFields != null && !errorFields.isEmpty())
            throw new InvalidDataException(activity, getStringValue(R.string.action_settings), errorFields);

    }

    @Override
    public void  prepareSettingScreen() {
        prepareView();
        populateData();
    }

    @Override
    public void saveSettings() {
        saveData(R.id.deviceId, PA_PHONE_ID);
        saveData(R.id.form_id, PA_FORM_ID);
        saveData(R.id.min_age, PA_MIN_AGE);
        saveData(R.id.max_age, PA_MAX_AGE);
        saveSafely(activity, FLOW_TYPE, FlowType.Participant.toString());
    }

    @Override
    public Intent getIntent() {
        return new Intent(activity, ParticipantListActivity.class);
    }

    private void prepareView() {
        hide(R.id.campaignId_label);
        hide(R.id.campaignId);
        hide(R.id.household_flow_disabled);
        hide(R.id.participant_flow);
        hide(R.id.household_seed);
        hide(R.id.household_seed_label);
        hide(R.id.endpointUrl);
        hide(R.id.endpointUrl_label);
        hide(R.id.importUrl);
        hide(R.id.importUrl_label);
        hide(R.id.importButton);
    }

    private void hide(int viewId) {
        View viewElement = activity.findViewById(viewId);
        viewElement.setVisibility(View.GONE);
    }

    private void populateData() {
        setData(R.id.deviceId, Constants.PA_PHONE_ID);
        setData(R.id.form_id, Constants.PA_FORM_ID);
        setData(R.id.min_age, Constants.PA_MIN_AGE);
        setData(R.id.max_age, Constants.PA_MAX_AGE);
    }

    private void setData(int viewId, String keyId) {
        String data = getValue(activity, keyId);
        TextView textView = (TextView) activity.findViewById(viewId);
        textView.setText(data);
    }

    private void saveData(int viewId, String keyId) {
        TextView textView = (TextView) activity.findViewById(viewId);
        String data = textView.getText().toString().trim();
        saveSafely(activity, keyId, data);
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

    protected String getStringValue(int value) {
        return activity.getString(value);
    }
}
