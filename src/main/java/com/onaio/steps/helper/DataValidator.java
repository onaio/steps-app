package com.onaio.steps.helper;

import android.app.Activity;

import com.onaio.steps.R;
import com.onaio.steps.model.Gender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manisharana on 5/22/15.
 */
public class DataValidator {

    private Activity activity;
    private List<String> errorFields;
    public DataValidator(Activity activity) {

        this.activity = activity;
        errorFields =new ArrayList<String>();
    }
    public List validateFields(String surname, String firstName, Gender gender, String ageString)
    {
        validate(surname, getStringValue(R.string.member_family_surname_hint));
        validate(firstName, getStringValue(R.string.member_first_name_hint));
        validate(gender);
        validate(ageString, getStringValue(R.string.age_hint));
        validateAgeRange(ageString, getStringValue(R.string.age_not_in_range)+"%d-%d)");
        return errorFields;
    }

    public void validateAgeRange(String ageString, String errorKey) {
        if(ageString == null || ageString.equals(""))
            return;
        int minAge = getValue(Constants.MIN_AGE);
        int maxAge = getValue(Constants.MAX_AGE);
        int age = Integer.parseInt(ageString);
        if(age<minAge || age>maxAge)
            errorFields.add(String.format(errorKey,minAge,maxAge));
    }

    public void validate(Gender gender) {
        if(gender.equals(Gender.NotDefined))
            errorFields.add(getStringValue(R.string.member_gender_hint));
    }

    public void validate(String fieldValue, String errorKey){
        if(fieldValue==null || fieldValue.equals(""))
            errorFields.add(errorKey);
    }


    public Gender genderSelection(int genderSelectionId) {
        if(genderSelectionId == R.id.male_selection)
            return Gender.Male;
        if(genderSelectionId == R.id.female_selection)
            return Gender.Female;
        return Gender.NotDefined;
    }

    private int getValue(String key) {
        return Integer.parseInt(KeyValueStoreFactory.instance(activity).getString(key)) ;
    }

    public String getStringValue(int value){
        return activity.getApplicationContext().getString(value);
    }

    public void validateId(String id, int value) {
        validate(id,getStringValue(value));
    }
}
