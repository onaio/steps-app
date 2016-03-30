package com.onaio.steps.exceptions;

import android.app.Activity;

import com.onaio.steps.R;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class InvalidDataException extends Exception {

    public InvalidDataException(Activity activity, String modelName, List<String> errorFields) {
       super((activity.getString(R.string.action_settings).equalsIgnoreCase(modelName))?
               activity.getString(R.string.invalid_settings)+" "+StringUtils.join(errorFields.toArray(), ','):(activity.getString(R.string.action_member).equalsIgnoreCase(modelName))?
               activity.getString(R.string.invalid_member)+" "+StringUtils.join(errorFields.toArray(),','):"");

        //super(String.format(activity.getString(R.string.invalid)+" %s, " +activity.getString(R.string.fill_correct_message)+" %s.",modelName, StringUtils.join(errorFields.toArray(),',')));
    }


    public InvalidDataException(List<String> errorMessages) {
        super(StringUtils.join(errorMessages.toArray(),','));
    }



}
