package com.onaio.steps.modelViewWrapper;

import android.app.Activity;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.exception.InvalidDataException;
import com.onaio.steps.helper.DataValidator;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberViewWrapper {

    private final DataValidator dataValidator;
    private List<String> errorFields;
    private Activity activity;

    public MemberViewWrapper(Activity activity){
        this.activity = activity;
        errorFields = new ArrayList<String>();
        dataValidator = new DataValidator(activity);

    }

    public Member getMember(int familySurnameViewId, int firstNameViewId, int genderViewId, int ageViewId, Household household) throws InvalidDataException {
        String surname = ((TextView) activity.findViewById(familySurnameViewId)).getText().toString();
        String firstName = ((TextView) activity.findViewById(firstNameViewId)).getText().toString();
        Gender gender = dataValidator.genderSelection(((RadioGroup) activity.findViewById(genderViewId)).getCheckedRadioButtonId());
        String ageString = ((TextView) activity.findViewById(ageViewId)).getText().toString();
        validation(surname, firstName, gender, ageString);
        if(!errorFields.isEmpty())
            throw new InvalidDataException(activity,dataValidator.getStringValue(R.string.action_member),errorFields);
        return new Member(surname, firstName, gender, Integer.parseInt(ageString), household, false);
    }

    private void validation(String surname, String firstName, Gender gender, String ageString) {
        dataValidator.validateFields(surname,firstName,gender,ageString);
    }

    public Member update(Member member,String surname, String firstName, Gender gender, String ageString) throws InvalidDataException {
        validation(surname, firstName, gender, ageString);
        if(!errorFields.isEmpty())
            throw new InvalidDataException(activity,dataValidator.getStringValue(R.string.action_member),errorFields);
        return new Member(member.getId(),surname,firstName, gender,Integer.parseInt(ageString),member.getHousehold(),member.getMemberHouseholdId(),member.getDeleted());
    }


}
