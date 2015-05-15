package com.onaio.steps.exception;

import android.app.Activity;

import com.onaio.steps.R;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class InvalidDataException extends Exception {

    public InvalidDataException(Activity activity, String modelName, List<String> errorFields) {
        super(String.format(activity.getApplicationContext().getString(R.string.invalid)+" %s, " +activity.getApplicationContext().getString(R.string.fill_correct_message)+" %s",modelName, StringUtils.join(errorFields.toArray(),',')));
    }

    public InvalidDataException(List<String> errorMessages) {
        super(StringUtils.join(errorMessages.toArray(),','));
    }



}
