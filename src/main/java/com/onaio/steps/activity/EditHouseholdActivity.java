package com.onaio.steps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.DatabaseHelper;
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
        TextView header = (TextView) findViewById(R.id.form_header);
        header.setText(R.string.household_edit_header);
    }

    private void populateDataFromIntent() {
        Intent intent = getIntent();
        household = (Household) intent.getSerializableExtra(HOUSEHOLD);
        TextView nameView = (TextView) findViewById(R.id.generated_household_id);
        TextView  phoneNumberView= (TextView) findViewById(R.id.household_number);
        TextView commentsView = (TextView) findViewById(R.id.household_comments);
        nameView.setText(household.getName());
        phoneNumberView.setText(household.getPhoneNumber());
        commentsView.setText(household.getComments());
    }

    public void save(View view) {
            Intent intent = this.getIntent();
            household = new HouseholdViewWrapper(this).updateHousehold(household, R.id.household_number,R.id.household_comments);
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            household.update(db);
            intent.putExtra(HOUSEHOLD,household);
            setResult(RESULT_OK, intent);
            finish();
    }

    public void cancel(View view){
        finish();
    }

}
