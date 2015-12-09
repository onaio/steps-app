package com.onaio.steps.modelViewWrapper;

import android.app.Activity;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DataValidator;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ParticipantViewWrapper extends MemberViewWrapper {

    private final DataValidator dataValidator;
    private List<String> errorFields;
    private final int PARTICIPANT_VIEW_ID = R.id.participant_id_value;

    public ParticipantViewWrapper(Activity activity) {
        super(activity);
        errorFields = new ArrayList<String>();
        dataValidator = new DataValidator(activity);
    }

    public Participant getFromView() throws InvalidDataException {
        errorFields.clear();
        String participantId = getParticipantId();
        String surname = getSurname();
        String firstName = getFirstName();
        Gender gender = getGender();
        String ageString = getAge();
        validate(participantId, surname, firstName, gender, ageString);
        if (!errorFields.isEmpty()) {
            throw new InvalidDataException(activity, getStringValue(R.string.participant), errorFields);
        }
        String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
        return new Participant(participantId, surname, firstName, gender, Integer.parseInt(ageString), InterviewStatus.NOT_DONE, currentDate);
    }

    public Participant updateFromView(Participant participant) throws InvalidDataException {
        errorFields.clear();
        String participantId = getParticipantId();
        String surname = getSurname();
        String firstName = getFirstName();
        Gender gender = getGender();
        String ageString = getAge();
        validate(participantId, surname, firstName, gender, ageString);
        if (!errorFields.isEmpty())
            throw new InvalidDataException(activity, getStringValue(R.string.participant), errorFields);
        return new Participant(participant.getId(),participantId, surname, firstName, gender, Integer.parseInt(ageString), participant.getStatus(),participant.getCreatedAt());
    }
    public void updateView(Participant participant){
        setParticipantId(participant.getParticipantID());
        setSurname(participant.getFamilySurname());
        setFirstName(participant.getFirstName());
        setGender(genderSelection(participant.getGender()));
        setAge(String.valueOf(participant.getAge()));
    }

    private void validate(String participantId, String surname, String firstName, Gender gender, String ageString) {
        errorFields = dataValidator.validate(participantId, getStringValue(R.string.participant_id)).
                validate(surname, getStringValue(R.string.member_family_surname_hint)).
                validate(firstName, getStringValue(R.string.member_first_name_hint)).
                validate(gender,getStringValue(R.string.member_gender_hint)).
                validate(ageString, getStringValue(R.string.age_hint)).
                validateAgeRange(ageString, getStringValue(R.string.age_not_in_range) + " %s-%s)"
                        , String.valueOf(Constants.PA_MIN_AGE), String.valueOf(Constants.PA_MAX_AGE)).
                finish();
    }

    private String getParticipantId(){
        return ((TextView) activity.findViewById(PARTICIPANT_VIEW_ID)).getText().toString();
    }

    protected void setParticipantId(String participantId){
        ((TextView) activity.findViewById(PARTICIPANT_VIEW_ID)).setText(participantId);
    }



}
