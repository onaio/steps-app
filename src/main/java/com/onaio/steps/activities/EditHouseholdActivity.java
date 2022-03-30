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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.modelViewWrapper.HouseholdViewWrapper;
import com.onaio.steps.validators.PhoneNumberValidator;

import static com.onaio.steps.helper.Constants.HH_HOUSEHOLD;

import androidx.appcompat.app.AppCompatActivity;

public class EditHouseholdActivity extends AppCompatActivity {

    private Household household;
    private PhoneNumberValidator phoneNumberValidator;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customDialog = new CustomDialog();
        populateView();
        populateDataFromIntent();
    }

    private void populateView() {
        setContentView(R.layout.household_form);
        TextView header = (TextView) findViewById(R.id.form_header);
        header.setText(R.string.household_edit_header);
        phoneNumberValidator = new PhoneNumberValidator(findViewById(R.id.household_number));
    }

    private void populateDataFromIntent() {
        Intent intent = getIntent();
        household = (Household) intent.getSerializableExtra(HH_HOUSEHOLD);
        TextView nameView = (TextView) findViewById(R.id.generated_household_id);
        TextView  phoneNumberView= (TextView) findViewById(R.id.household_number);
        TextView commentsView = (TextView) findViewById(R.id.household_comments);
        nameView.setText(household.getName());
        phoneNumberView.setText(household.getPhoneNumber());
        commentsView.setText(household.getComments());
    }

    public void doneBtnClicked(View view) {
            if (phoneNumberValidator.validate()) {
                Intent intent = this.getIntent();
                household = new HouseholdViewWrapper(this).updateHousehold(household, R.id.household_number,R.id.household_comments);
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                household.update(db);
                intent.putExtra(HH_HOUSEHOLD,household);
                setResult(RESULT_OK, intent);
                finish();
            }
            else {
                customDialog.notify(this, CustomDialog.EmptyListener, R.string.error_title, R.string.invalid_phone_number);
            }
    }

    public void cancel(View view){
        finish();
    }

}
