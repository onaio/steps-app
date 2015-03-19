package com.onaio.steps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewHouseholdActivity extends Activity {

    private String phoneId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_household);
        populatePhoneIdInGeneratedId();
        bindGeneratedIdToTextInput();
    }

    private void bindGeneratedIdToTextInput() {
        TextView householdName = (TextView) findViewById(R.id.household_name);
        householdName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                TextView phoneIdView = (TextView) findViewById(R.id.generated_household_id);
                phoneIdView.setText(phoneId +"-"+editable.toString().trim());
            }
        });
    }

    private void populatePhoneIdInGeneratedId() {
        Intent intent = getIntent();
        phoneId = intent.getStringExtra(Constants.PHONE_ID);
        TextView phoneIdView = (TextView) findViewById(R.id.generated_household_id);
        phoneIdView.setText(phoneId +"-");
    }

    public void saveHousehold(View view) {
        Intent intent = this.getIntent();
        Household household = getHouseholdFromView(intent);
        DatabaseHelper db = new DatabaseHelper(this.getApplicationContext());
        household.save(db);
        setResult(RESULT_OK, intent);
        finish();
    }

    private Household getHouseholdFromView(Intent intent) {
        TextView name = (TextView) findViewById(R.id.household_name);
        TextView number = (TextView) findViewById(R.id.household_number);
        String phoneNumber = number.getText().toString();
        String currentDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        return new Household(intent.getStringExtra(Constants.PHONE_ID)+"-"+name.getText().toString(), phoneNumber, HouseholdStatus.OPEN, currentDate);
    }

}
