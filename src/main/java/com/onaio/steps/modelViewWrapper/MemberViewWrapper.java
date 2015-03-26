package com.onaio.steps.modelViewWrapper;

import android.app.Activity;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.onaio.steps.R;
import com.onaio.steps.exception.InvalidDataException;
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
        String gender = genderSelection(((RadioGroup) activity.findViewById(genderViewId)).getCheckedRadioButtonId());
        String ageString = ((TextView) activity.findViewById(ageViewId)).getText().toString();
        validate(surname, FAMILY_SURNAME);
        validate(firstName, FIRST_NAME);
        validate(gender, GENDER);
        validate(ageString, AGE);
        if(!errorFields.isEmpty())
            throw new InvalidDataException(MEMBER_ERROR,errorFields);
        return new Member(surname, firstName, gender, Integer.parseInt(ageString), household);
    }

    private void validate(String fieldValue, String errorKey){
        if(fieldValue==null || fieldValue.equals(""))
            errorFields.add(errorKey);
    }

    private String genderSelection(int genderSelectionId) {
        switch (genderSelectionId){
            case R.id.male_selection :
                return MALE;
            case R.id.female_selection :
                return FEMALE;
            default: return "";
        }
    }
}
