package com.onaio.steps.validators;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

public class PhoneNumberValidator implements Validator {

    public static final int DEFAULT_LENGTH = 7;

    private final AppCompatEditText appCompatEditText;
    private int minLength;
    private int maxLength;

    public PhoneNumberValidator(AppCompatEditText appCompatEditText) {
        this(appCompatEditText, DEFAULT_LENGTH, DEFAULT_LENGTH);
    }

    public PhoneNumberValidator(AppCompatEditText appCompatEditText, int minLength, int maxLength) {
        this.appCompatEditText = appCompatEditText;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public boolean validate() {
        int length = appCompatEditText.length();
        return length >= minLength && length <= maxLength;
    }
}
