package com.onaio.steps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.onaio.steps.R;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.Dialog;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.onaio.steps.helper.Constants.*;

public class NewHouseholdActivity extends Activity {

    private String phoneId;
    private int householdSeed;
    private int DELTA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_household);
        populateDataFromIntent();
        populateGeneratedHouseholdId();
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
        Intent intent = this.getIntent();
        Household household = getHouseholdFromView();
        if(!household.isValid()){
            new Dialog().notify(this,Dialog.EmptyListener,R.string.invalid_household,R.string.error_title);
            return;
        }
        DatabaseHelper db = new DatabaseHelper(this.getApplicationContext());
        household.save(db);
        setResult(RESULT_OK, intent);
        finish();
    }

    private Household getHouseholdFromView() {
        TextView name = (TextView) findViewById(R.id.generated_household_id);
        TextView number = (TextView) findViewById(R.id.household_number);
        String phoneNumber = number.getText().toString();
        String currentDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        return new Household(name.getText().toString(), phoneNumber, HouseholdStatus.NOT_SELECTED, currentDate);
    }

}
