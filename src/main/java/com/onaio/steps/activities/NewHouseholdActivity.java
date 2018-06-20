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

package com.onaio.steps.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.modelViewWrapper.HouseholdViewWrapper;

import static com.onaio.steps.helper.Constants.HH_HOUSEHOLD_SEED;
import static com.onaio.steps.helper.Constants.HH_PHONE_ID;

public class NewHouseholdActivity extends Activity {

    private final DatabaseHelper db = new DatabaseHelper(this);
    private String phoneId;
    private int householdSeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateView();
        populateDataFromIntent();
        populateGeneratedHouseholdId();
    }

    private void populateView() {
        setContentView(R.layout.household_form);
        TextView header = (TextView) findViewById(R.id.form_header);
        header.setText(R.string.household_new_header);
        Button doneButton = (Button) findViewById(R.id.ic_done);
        doneButton.setText(R.string.add);
    }

    private void populateGeneratedHouseholdId() {
        TextView phoneIdView = (TextView) findViewById(R.id.generated_household_id);
        int householdsCount = Household.getAllCount(db);
        int generatedId = householdSeed + householdsCount;
        phoneIdView.setText(String.format("%s-%s",phoneId, String.valueOf(generatedId)));
    }

    private void populateDataFromIntent() {
        Intent intent = getIntent();
        phoneId = intent.getStringExtra(HH_PHONE_ID);
        String householdSeedString = intent.getStringExtra(HH_HOUSEHOLD_SEED);
        householdSeedString = householdSeedString == null || householdSeedString.equals("") ? "1" : householdSeedString;
        householdSeed = Integer.parseInt(householdSeedString);
    }

    public void doneBtnClicked(View view) {
        try {
            Intent intent = this.getIntent();
            Household household = new HouseholdViewWrapper(this).getHousehold(R.id.generated_household_id, R.id.household_number,R.id.household_comments);
            household.save(db);
            intent.putExtra(Constants.HH_HOUSEHOLD,household);
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception e) {
            new CustomDialog().notify(this, CustomDialog.EmptyListener,e.getMessage(),R.string.error_title);
        }
    }

    public void cancel(View view){
        finish();
    }

}
