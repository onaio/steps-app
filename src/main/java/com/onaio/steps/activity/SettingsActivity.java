package com.onaio.steps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        populatePhoneId();

    }

    private void populatePhoneId() {
        Intent intent = getIntent();
        String phoneId = intent.getStringExtra(Constants.PHONE_ID);
        String householdSeed = intent.getStringExtra(Constants.HOUSEHOLD_SEED);
        String endpointUrl = intent.getStringExtra(Constants.ENDPOINT_URL);
        TextView phoneIdView = (TextView) findViewById(R.id.phoneId);
        TextView endpointUrlView = (TextView) findViewById(R.id.endpointUrl);
        TextView householdSeedView = (TextView) findViewById(R.id.household_seed);
        phoneIdView.setText(phoneId);
        endpointUrlView.setText(endpointUrl);
        householdSeedView.setText(householdSeed);
    }

    public void saveSettings(View view) {
        Intent intent = this.getIntent();
        TextView phoneIdView = (TextView) findViewById(R.id.phoneId);
        TextView endpointUrlView = (TextView) findViewById(R.id.endpointUrl);
        TextView householdSeed = (TextView) findViewById(R.id.household_seed);
        intent.putExtra(Constants.PHONE_ID, phoneIdView.getText().toString());
        intent.putExtra(Constants.ENDPOINT_URL, endpointUrlView.getText().toString());
        intent.putExtra(Constants.HOUSEHOLD_SEED, householdSeed.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
