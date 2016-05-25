package com.onaio.steps.modelViewWrapper;

import android.app.Activity;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdVisit;
import com.onaio.steps.model.InterviewStatus;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HouseholdViewWrapper {
    private Activity activity;

    public HouseholdViewWrapper(Activity activity) {
        this.activity = activity;
    }

    public Household getHousehold(int nameViewId, int numberViewId, int commentsViewId, int eligibilityRadioGroup, int otherSpecifyEditText) throws InvalidDataException {
        TextView nameView = (TextView) activity.findViewById(nameViewId);
        TextView numberView = (TextView) activity.findViewById(numberViewId);
        String phoneNumber = numberView.getText().toString();
        String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
        EditText commentsView = (EditText) activity.findViewById(commentsViewId);
        String comments = commentsView.getText().toString();

        RadioGroup rg = (RadioGroup) activity.findViewById(eligibilityRadioGroup);
        String eligibilityRadioBtnValue = ((RadioButton) activity.findViewById(rg.getCheckedRadioButtonId())).getText().toString();
        String otherRadioButtonSpecifyValue = null;
        if (eligibilityRadioBtnValue.equalsIgnoreCase(activity.getString(R.string.other))) {
            otherRadioButtonSpecifyValue = ((EditText) activity.findViewById(otherSpecifyEditText)).getText().toString();
        }

        return new Household(nameView.getText().toString(), phoneNumber, InterviewStatus.NOT_SELECTED, currentDate, comments, eligibilityRadioBtnValue, otherRadioButtonSpecifyValue);
    }

    public Household updateHousehold(Household household, int numberViewId, int commentsViewId) {
        TextView numberView = (TextView) activity.findViewById(numberViewId);
        String phoneNumber = numberView.getText().toString();
        TextView commentsView = (TextView) activity.findViewById(commentsViewId);
        String comments = commentsView.getText().toString();
        household.setPhoneNumber(phoneNumber);
        household.setComments(comments);
        return household;
    }

    public HouseholdVisit getHouseholdVisit(int eligibilityRadioGroup, int otherSpecifyEditText) throws InvalidDataException {
        String currentDate = new SimpleDateFormat(Constants.DATE_TIME_FORMAT).format(new Date());
        HouseholdVisit householdVisit = new HouseholdVisit();

        RadioGroup rg = (RadioGroup) activity.findViewById(eligibilityRadioGroup);
        String eligibilityRadioBtnValue = ((RadioButton) activity.findViewById(rg.getCheckedRadioButtonId())).getText().toString();
        String otherRadioButtonSpecifyValue = null;
        if (eligibilityRadioBtnValue.equalsIgnoreCase(activity.getString(R.string.other))) {
            otherRadioButtonSpecifyValue = ((EditText) activity.findViewById(otherSpecifyEditText)).getText().toString();
            householdVisit.setComments(otherRadioButtonSpecifyValue);
        }

        householdVisit.setStatus(eligibilityRadioBtnValue);
        householdVisit.setCreatedAt(currentDate);

        return householdVisit;
    }
}
