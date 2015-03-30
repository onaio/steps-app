package com.onaio.steps.exception;

import com.onaio.steps.helper.Constants;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class InvalidDataException extends Exception {
    public InvalidDataException(String modelName, List<String> errorFields) {
        super(String.format(Constants.ERROR_STRING,modelName, StringUtils.join(errorFields.toArray(),',')));
    }

    public InvalidDataException(List<String> errorMessages) {
        super(StringUtils.join(errorMessages.toArray(),','));
    }
}
