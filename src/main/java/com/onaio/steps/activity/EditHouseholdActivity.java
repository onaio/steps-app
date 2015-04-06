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

import static com.onaio.steps.helper.Constants.HOUSEHOLD;

public class EditHouseholdActivity extends Activity {

    private Household household;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateView();
        populateDataFromIntent();
    }

    private void populateView() {
        setContentView(R.layout.household_form);
        TextView header = (TextView) findViewById(R.id.household_form_header);
        header.setText(R.string.household_edit_header);
    }

    private void populateDataFromIntent() {
        Intent intent = getIntent();
        household = (Household) intent.getSerializableExtra(HOUSEHOLD);
        TextView nameView = (TextView) findViewById(R.id.generated_household_id);
        TextView  phoneNumberView= (TextView) findViewById(R.id.household_number);
        nameView.setText(household.getName());
        phoneNumberView.setText(household.getPhoneNumber());
    }

    public void saveHousehold(View view) {
        try {
            Intent intent = this.getIntent();
            household = new HouseholdViewWrapper(this).updateHousehold(household, R.id.household_number);
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            household.update(db);
            intent.putExtra(HOUSEHOLD,household);
            setResult(RESULT_OK, intent);
            finish();

        } catch (InvalidDataException e) {
            new Dialog().notify(this,Dialog.EmptyListener,e.getMessage(),R.string.error_title);
        }
    }

}
