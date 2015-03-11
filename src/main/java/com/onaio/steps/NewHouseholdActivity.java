package com.onaio.steps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;

import static com.onaio.steps.StepsActivity.PHONE_ID;

public class NewHouseholdActivity extends Activity {

    public static final String HOUSEHOLD_NAME = "household_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_household);
        Intent intent = getIntent();
        String phoneId = intent.getStringExtra(PHONE_ID);
        TextView phoneIdView = (TextView) findViewById(R.id.generated_household_id);
        phoneIdView.setText(phoneId+"-");
    }

    public void saveHousehold(View view) {
        Intent intent = this.getIntent();
        TextView name = (TextView) findViewById(R.id.household_name);
        TextView number = (TextView) findViewById(R.id.household_number);
        long phoneNumber = Long.parseLong(number.getText().toString());
        Household household = new Household(intent.getStringExtra(PHONE_ID)+"-"+name.getText().toString(), phoneNumber);
        DatabaseHelper db = new DatabaseHelper(this.getApplicationContext());
        db.createHousehold(household);
        intent.putExtra(HOUSEHOLD_NAME,household.getName());
        setResult(RESULT_OK, intent);
        finish();
    }

}
