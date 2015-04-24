package com.onaio.steps.modelViewWrapper;

import android.app.Activity;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.onaio.steps.R;
import com.onaio.steps.exception.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;
import java.util.ArrayList;
import java.util.List;

import static com.onaio.steps.helper.Constants.*;

public class MemberViewWrapper {

    private List<String> errorFields;
    private Activity activity;

    public MemberViewWrapper(Activity activity){
        this.activity = activity;
        errorFields = new ArrayList<String>();
    }

    public Member getMember(int familySurnameViewId, int firstNameViewId, int genderViewId, int ageViewId, Household household) throws InvalidDataException {
        String surname = ((TextView) activity.findViewById(familySurnameViewId)).getText().toString();
        String firstName = ((TextView) activity.findViewById(firstNameViewId)).getText().toString();
        Gender gender = genderSelection(((RadioGroup) activity.findViewById(genderViewId)).getCheckedRadioButtonId());
        String ageString = ((TextView) activity.findViewById(ageViewId)).getText().toString();
        validateFields(surname, firstName, gender, ageString);
        if(!errorFields.isEmpty())
            throw new InvalidDataException(MEMBER_ERROR,errorFields);
        return new Member(surname, firstName, gender, Integer.parseInt(ageString), household, false);
    }

    public Member update(Member member,String surname, String firstName, Gender gender, String ageString) throws InvalidDataException {
        validateFields(surname, firstName, gender, ageString);
        if(!errorFields.isEmpty())
            throw new InvalidDataException(MEMBER_ERROR,errorFields);
        return new Member(member.getId(),surname,firstName, gender,Integer.parseInt(ageString),member.getHousehold(),member.getMemberHouseholdId(),member.getDeleted());
    }

    private void validateFields(String surname, String firstName, Gender gender, String ageString) {
        validate(surname, FAMILY_SURNAME);
        validate(firstName, FIRST_NAME);
        validate(gender);
        validate(ageString, AGE);
        validateAgeRange(ageString,AGE_NOT_IN_RANGE);
    }

    private void validateAgeRange(String ageString, String errorKey) {
        if(ageString == null || ageString.equals(""))
            return;
        int minAge = getValue(Constants.MIN_AGE);
        int maxAge = getValue(Constants.MAX_AGE);
        int age = Integer.parseInt(ageString);
        if(age<minAge || age>maxAge)
            errorFields.add(String.format(errorKey,minAge,maxAge));
    }

    private void validate(Gender gender) {
        if(gender.equals(Gender.NotDefined))
            errorFields.add(GENDER);
    }

    private void validate(String fieldValue, String errorKey){
        if(fieldValue==null || fieldValue.equals(""))
            errorFields.add(errorKey);
    }

    private Gender genderSelection(int genderSelectionId) {
        if(genderSelectionId == R.id.male_selection)
            return Gender.Male;
        if(genderSelectionId == R.id.female_selection)
            return Gender.Female;
        return Gender.NotDefined;
    }

    private int getValue(String key) {
        return Integer.parseInt(KeyValueStoreFactory.instance(activity).getString(key)) ;
    }
}
