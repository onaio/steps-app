package com.onaio.steps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.Dialog;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        populatePhoneId();

    }

    private void setView() {
        setContentView(R.layout.settings);
        TextView header = (TextView) findViewById(R.id.form_header);
        header.setText(R.string.action_settings);
        Button doneButton = (Button) findViewById(R.id.ic_done);
        doneButton.setText("DONE");
    }

    private void populatePhoneId() {
        Intent intent = getIntent();
        String phoneId = intent.getStringExtra(Constants.PHONE_ID);
        String householdSeed = intent.getStringExtra(Constants.HOUSEHOLD_SEED);
        String endpointUrl = intent.getStringExtra(Constants.ENDPOINT_URL);
        TextView phoneIdView = (TextView) findViewById(R.id.deviceId);
        TextView endpointUrlView = (TextView) findViewById(R.id.endpointUrl);
        TextView householdSeedView = (TextView) findViewById(R.id.household_seed);

        phoneIdView.setText(phoneId);
        endpointUrlView.setText(endpointUrl);
        householdSeedView.setText(householdSeed);
    }

    public void save(View view) {
        Intent intent = this.getIntent();
        String phoneId = ((TextView) findViewById(R.id.deviceId)).getText().toString();
        String endpointUrl = ((TextView) findViewById(R.id.endpointUrl)).getText().toString();
        String householdSeed = ((TextView) findViewById(R.id.household_seed)).getText().toString();
        if (!(isValid(phoneId) && isValid(endpointUrl))){
            new Dialog().notify(this,Dialog.EmptyListener,R.string.enter_a_value,R.string.error_title);
            return;
        }
        intent.putExtra(Constants.PHONE_ID, phoneId);
        intent.putExtra(Constants.ENDPOINT_URL, endpointUrl);
        intent.putExtra(Constants.HOUSEHOLD_SEED, householdSeed);
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
