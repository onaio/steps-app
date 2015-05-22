package com.onaio.steps.helper;

import android.app.Activity;

import com.onaio.steps.R;
import com.onaio.steps.model.Gender;

import java.util.ArrayList;
import java.util.List;

public class DataValidator {

    private Activity activity;
    private List<String> errorFields;
    public DataValidator(Activity activity) {

        this.activity = activity;
        errorFields =new ArrayList<String>();
    }

    public DataValidator validateAgeRange(String ageString, String errorKey) {
        if(ageString == null || ageString.equals(""))
            return this;
        int minAge = getValue(Constants.MIN_AGE);
        int maxAge = getValue(Constants.MAX_AGE);
        int age = Integer.parseInt(ageString);
        if(age<minAge || age>maxAge)
            errorFields.add(String.format(errorKey,minAge,maxAge));
        return this;
    }

    public DataValidator validate(Gender gender,String errorKey) {
        if(gender.equals(Gender.NotDefined))
            errorFields.add(errorKey);
        return this;
    }

    public DataValidator validate(String fieldValue, String errorKey){
        if(fieldValue==null || fieldValue.equals(""))
            errorFields.add(errorKey);
        return this;
    }

    public List<String> finish()
    {
        return errorFields;
    }

    private int getValue(String key) {
        return Integer.parseInt(KeyValueStoreFactory.instance(activity).getString(key)) ;
    }

}
