package com.onaio.steps.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.exceptions.InvalidDataException;
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
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateView();
        populateDataFromIntent();
        populateGeneratedHouseholdId();
        activity = this;
    }

    private void populateView() {
        setContentView(R.layout.household_form);
        TextView header = (TextView) findViewById(R.id.form_header);
        header.setText(R.string.household_new_header);
        final Button doneButton = (Button) findViewById(R.id.ic_done);
        doneButton.setText(R.string.add);
        doneButton.setVisibility(View.GONE);
        RadioGroup group = (RadioGroup) this.findViewById(R.id.rGrp_household_eligibility);
        final LinearLayout otherSpecifyLayout = (LinearLayout) this.findViewById(R.id.layout_other_specify);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                doneButton.setVisibility(View.VISIBLE);

                RadioButton radioButton = (RadioButton) radioGroup.findViewById(i);
                if (radioButton.getText().toString().equalsIgnoreCase(getString(R.string.other))) {
                    otherSpecifyLayout.setVisibility(View.VISIBLE);
                    EditText txtOthersSpecify = (EditText) activity.findViewById(R.id.others_specify);
                    txtOthersSpecify.requestFocus();
                } else {
                    otherSpecifyLayout.setVisibility(View.GONE);

                }
            }
        });
    }

    private void populateGeneratedHouseholdId() {
        TextView phoneIdView = (TextView) findViewById(R.id.generated_household_id);
        int householdsCount = Household.getAllCount(db);
        int generatedId = householdSeed + householdsCount;
        phoneIdView.setText(String.format("%s-%s", phoneId, String.valueOf(generatedId)));
    }

    private void populateDataFromIntent() {
        Intent intent = getIntent();
        phoneId = intent.getStringExtra(HH_PHONE_ID);
        String householdSeedString = intent.getStringExtra(HH_HOUSEHOLD_SEED);
        householdSeedString = householdSeedString == null || householdSeedString.equals("") ? "1" : householdSeedString;
        householdSeed = Integer.parseInt(householdSeedString);
    }

    public void save(View view) {
        try {
            Intent intent = this.getIntent();
            Household household = new HouseholdViewWrapper(this).getHousehold(R.id.generated_household_id, R.id.household_number, R.id.household_comments,R.id.rGrp_household_eligibility,R.id.others_specify);
            household.save(db);
            intent.putExtra(Constants.HH_HOUSEHOLD, household);
            setResult(RESULT_OK, intent);
            finish();
        } catch (InvalidDataException e) {
            new CustomDialog().notify(this, CustomDialog.EmptyListener, e.getMessage(), R.string.error_title);
        }
    }

    public void cancel(View view) {
        finish();
    }

}
