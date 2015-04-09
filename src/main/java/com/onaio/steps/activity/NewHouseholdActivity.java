package com.onaio.steps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.onaio.steps.R;
import com.onaio.steps.exception.InvalidDataException;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.Dialog;
import com.onaio.steps.model.Household;
import com.onaio.steps.modelViewWrapper.HouseholdViewWrapper;

import static com.onaio.steps.helper.Constants.*;

public class NewHouseholdActivity extends Activity {

    private String phoneId;
    private int householdSeed;
    private int DELTA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateView();
        populateDataFromIntent();
        populateGeneratedHouseholdId();
    }

    private void populateView() {
        setContentView(R.layout.household_form);
        TextView header = (TextView) findViewById(R.id.household_form_header);
        header.setText(R.string.household_new_header);
    }

    private void populateGeneratedHouseholdId() {
        TextView phoneIdView = (TextView) findViewById(R.id.generated_household_id);
        int householdsCount = Household.getAllCount(new DatabaseHelper(this));
        int generatedId = householdSeed + householdsCount + DELTA;
        phoneIdView.setText(String.format("%s-%d",phoneId, generatedId));
    }

    private void populateDataFromIntent() {
        Intent intent = getIntent();
        phoneId = intent.getStringExtra(PHONE_ID);
        String householdSeedString = intent.getStringExtra(HOUSEHOLD_SEED);
        householdSeedString = householdSeedString == null || householdSeedString.equals("") ? "0" : householdSeedString;
        householdSeed = Integer.parseInt(householdSeedString);
    }

    public void saveHousehold(View view) {
        try {
            Intent intent = this.getIntent();
            Household household = new HouseholdViewWrapper(this).getHousehold(R.id.generated_household_id, R.id.household_number);
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            household.save(db);
            setResult(RESULT_OK, intent);
            finish();
        } catch (InvalidDataException e) {
            new Dialog().notify(this,Dialog.EmptyListener,e.getMessage(),R.string.error_title);
        }
    }

}
