package com.onaio.steps.modelViewWrapper;

import android.app.Activity;
import android.widget.TextView;

import com.onaio.steps.exception.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HouseholdViewWrapper {
    private Activity activity;
    private List<String> errorMessages;

    public HouseholdViewWrapper(Activity activity) {
        this.activity = activity;
        errorMessages = new ArrayList<String>();
    }

    public Household getHousehold(int nameViewId, int numberViewId) throws InvalidDataException {
        TextView nameView = (TextView) activity.findViewById(nameViewId);
        TextView numberView = (TextView) activity.findViewById(numberViewId);
        String phoneNumber = numberView.getText().toString();
        String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
        validateNonEmptyPhoneNumber(phoneNumber);
        validatePhoneNumber(phoneNumber);
        if(!errorMessages.isEmpty())
            throw new InvalidDataException(errorMessages);
        return new Household(nameView.getText().toString(), phoneNumber, HouseholdStatus.NOT_SELECTED, currentDate);
    }

    private void validateNonEmptyPhoneNumber(String phoneNumber){
        if(phoneNumber==null || phoneNumber.equals(""))
            errorMessages.add(Constants.EMPTY_PHONE_NUMBER);
    }

    private void validatePhoneNumber(String phoneNumber){
        if((phoneNumber.length() != 9 && phoneNumber.length() != 10))
            errorMessages.add(Constants.INVALID_PHONE_NUMBER);
    }
}
