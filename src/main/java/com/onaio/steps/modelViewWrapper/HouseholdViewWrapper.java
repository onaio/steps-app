package com.onaio.steps.modelViewWrapper;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import com.onaio.steps.exception.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HouseholdViewWrapper {
    private Activity activity;

    public HouseholdViewWrapper(Activity activity) {
        this.activity = activity;
    }

    public Household getHousehold(int nameViewId, int numberViewId, int commentsViewId) throws InvalidDataException {
        TextView nameView = (TextView) activity.findViewById(nameViewId);
        TextView numberView = (TextView) activity.findViewById(numberViewId);
        String phoneNumber = numberView.getText().toString();
        String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
        EditText commentsView = (EditText) activity.findViewById(commentsViewId);
        String comments = commentsView.getText().toString();
        return new Household(nameView.getText().toString(), phoneNumber, HouseholdStatus.NOT_SELECTED, currentDate,comments);
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


}
