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

import static com.onaio.steps.helper.Constants.HH_HOUSEHOLD_SEED;
import static com.onaio.steps.helper.Constants.HH_PHONE_ID;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.handler.exceptions.ExceptionHandler;
import com.onaio.steps.handler.exceptions.IResolvableException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.ODKForm.ODKBlankForm;
import com.onaio.steps.modelViewWrapper.HouseholdViewWrapper;

import java.util.Locale;

public class NewHouseholdActivity extends AppCompatActivity implements IResolvableException, ExceptionHandler.ExceptionAlertCallback {

    private final DatabaseHelper db = new DatabaseHelper(this);
    private String phoneId;
    private int householdSeed;
    private ProgressDialog progressDialog;
    private ExceptionHandler exceptionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.saving_household));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        exceptionHandler = new ExceptionHandler(this, this);
        exceptionHandler.setCallback(this);
        populateView();
        populateDataFromIntent();
        populateGeneratedHouseholdId();
    }

    private void populateView() {
        setContentView(R.layout.household_form);
        TextView header = findViewById(R.id.form_header);
        header.setText(R.string.household_new_header);
        Button doneButton = findViewById(R.id.ic_done);
        doneButton.setText(R.string.add);
    }

    private void populateGeneratedHouseholdId() {
        TextView phoneIdView = findViewById(R.id.generated_household_id);
        int householdsCount = Household.getAllCount(db);
        int generatedId = householdSeed + householdsCount;
        phoneIdView.setText(String.format(Locale.getDefault(), "%s-%d",phoneId, generatedId));
    }

    private void populateDataFromIntent() {
        Intent intent = getIntent();
        phoneId = intent.getStringExtra(HH_PHONE_ID);
        String householdSeedString = intent.getStringExtra(HH_HOUSEHOLD_SEED);
        householdSeedString = householdSeedString == null || householdSeedString.equals("") ? "1" : householdSeedString;
        householdSeed = Integer.parseInt(householdSeedString);
    }

    public void doneBtnClicked(View view) {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

        saveHousehold();
    }

    public void cancel(View view){
        finish();
    }

    public void saveHousehold() {
        try {
            ODKBlankForm odkBlankForm = (ODKBlankForm) ODKBlankForm.find(this, getFormId());
            Intent intent = this.getIntent();
            Household household = new HouseholdViewWrapper(this).getHousehold(R.id.generated_household_id, R.id.household_number,R.id.household_comments);
            household.setOdkJrFormId(odkBlankForm.getJrFormId());
            household.setOdkJrFormTitle(odkBlankForm.getDisplayName());
            household.save(db);
            intent.putExtra(Constants.HH_HOUSEHOLD,household);
            setResult(RESULT_OK, intent);
            progressDialog.dismiss();
            finish();
        } catch (Exception e) {
            exceptionHandler.handle(e);
        }
    }

    public String getFormId() {
        return KeyValueStoreFactory.instance(this).getString(Constants.HH_FORM_ID);
    }

    @Override
    public void tryToResolve() {
        saveHousehold();
    }

    @Override
    public void onError(Exception e, int message) {
        progressDialog.dismiss();
    }
}
