package com.onaio.steps.modelViewWrapper;

import android.app.Activity;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.exception.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Participant;

import java.util.ArrayList;
import java.util.List;


public class ParticipantViewWrapper {

    private List<String> errorFields;
    private Activity activity;

    public ParticipantViewWrapper(Activity activity) {
        this.activity = activity;
        errorFields = new ArrayList<String>();
    }

    public Participant getParticipant(int participant_id, int familySurnameViewId, int firstNameViewId, int genderViewId, int ageViewId) throws InvalidDataException{
        String participantId = ((TextView) activity.findViewById(participant_id)).getText().toString();
        String surname = ((TextView) activity.findViewById(familySurnameViewId)).getText().toString();
        String firstName = ((TextView) activity.findViewById(firstNameViewId)).getText().toString();
        Gender gender = genderSelection(((RadioGroup) activity.findViewById(genderViewId)).getCheckedRadioButtonId());
        String ageString = ((TextView) activity.findViewById(ageViewId)).getText().toString();
        validateFields(participantId,surname, firstName, gender, ageString);
        if(!errorFields.isEmpty())
            throw new InvalidDataException(activity, getStringValue(R.string.participant),errorFields);
        return new Participant(participantId,surname, firstName, gender, Integer.parseInt(ageString));
    }

    public Participant update(Participant participant, String particpantId, String surname, String firstName, Gender gender, String ageString) throws InvalidDataException {
        validateFields(particpantId,surname, firstName, gender, ageString);
        if(!errorFields.isEmpty())
            throw new InvalidDataException(activity, getStringValue(R.string.participant),errorFields);
        return new Participant(participant.getId(),surname,firstName, gender,Integer.parseInt(ageString));
    }


    private void validateFields(String participantId, String surname, String firstName, Gender gender, String ageString) {
        validate(participantId, getStringValue(R.string.participant_id));
        validate(surname, getStringValue(R.string.member_family_surname_hint));
        validate(firstName, getStringValue(R.string.member_first_name_hint));
        validate(gender);
        validate(ageString, getStringValue(R.string.age_hint));
        validateAgeRange(ageString, getStringValue(R.string.age_not_in_range)+"%d-%d)");
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
            errorFields.add(getStringValue(R.string.member_gender_hint));
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

    private String getStringValue(int value){
        return activity.getApplicationContext().getString(value);
    }

}
