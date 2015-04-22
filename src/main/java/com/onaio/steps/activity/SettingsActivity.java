package com.onaio.steps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;
import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.Dialog;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        populateData();
    }

    private void setView() {
        setContentView(R.layout.settings);
        TextView header = (TextView) findViewById(R.id.form_header);
        header.setText(R.string.action_settings);
    }

    private void populateData() {
        Intent intent = getIntent();
        String phoneId = intent.getStringExtra(Constants.PHONE_ID);
        String householdSeed = intent.getStringExtra(Constants.HOUSEHOLD_SEED);
        String endpointUrl = intent.getStringExtra(Constants.ENDPOINT_URL);
        String minAge = intent.getStringExtra(Constants.MIN_AGE);
        String maxAge = intent.getStringExtra(Constants.MAX_AGE);
        TextView phoneIdView = (TextView) findViewById(R.id.deviceId);
        TextView endpointUrlView = (TextView) findViewById(R.id.endpointUrl);
        TextView householdSeedView = (TextView) findViewById(R.id.household_seed);
        TextView minAgeView = (TextView) findViewById(R.id.min_age);
        TextView maxAgeView = (TextView) findViewById(R.id.max_age);

        phoneIdView.setText(phoneId);
        endpointUrlView.setText(endpointUrl);
        householdSeedView.setText(householdSeed);
        minAgeView.setText(minAge);
        maxAgeView.setText(maxAge);
    }

    public void save(View view) {
        Intent intent = this.getIntent();
        String phoneId = ((TextView) findViewById(R.id.deviceId)).getText().toString();
        String endpointUrl = ((TextView) findViewById(R.id.endpointUrl)).getText().toString();
        String householdSeed = ((TextView) findViewById(R.id.household_seed)).getText().toString();
        String minAge = ((TextView) findViewById(R.id.min_age)).getText().toString();
        String maxAge = ((TextView) findViewById(R.id.max_age)).getText().toString();
        if (!(isValid(phoneId) && isValid(endpointUrl) && isValid(minAge) && isValid(maxAge))){
            new Dialog().notify(this,Dialog.EmptyListener, R.string.error_title, R.string.enter_a_value);
            return;
        }
        intent.putExtra(Constants.PHONE_ID, phoneId);
        intent.putExtra(Constants.ENDPOINT_URL, endpointUrl);
        intent.putExtra(Constants.HOUSEHOLD_SEED, householdSeed);
        intent.putExtra(Constants.MIN_AGE, minAge);
        intent.putExtra(Constants.MAX_AGE, maxAge);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void cancel(View view){
        finish();
    }

    private boolean isValid(String value) {
        return value != null && !value.equals("");
    }
}
