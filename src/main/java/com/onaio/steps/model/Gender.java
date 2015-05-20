package com.onaio.steps.model;


import android.app.Activity;

import com.onaio.steps.R;

public enum Gender {
    NotDefined(0,R.string.undefined_label),
    Male(1, R.string.male_label),
    Female(2,R.string.female_label);

    private final int intValue;
    private int label;

    Gender(int intValue, int label) {
        this.intValue = intValue;
        this.label = label;
    }

    public int getIntValue(){
        return intValue;
    }
    public String getInternationalizedString(Activity activity){

        return activity.getString(label);
    }


}
