package com.onaio.steps.modelViewWrapper;

import android.app.Activity;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.exception.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DataValidator;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ParticipantViewWrapper {

    private final DataValidator dataValidator;
    private List<String> errorFields;
    private Activity activity;

    public ParticipantViewWrapper(Activity activity) {
        this.activity = activity;
        errorFields = new ArrayList<String>();
        dataValidator = new DataValidator(activity);
    }

    public Participant getParticipant(int participant_id, int familySurnameViewId, int firstNameViewId, int genderViewId, int ageViewId) throws InvalidDataException {
        String participantId = ((TextView) activity.findViewById(participant_id)).getText().toString();
        String surname = ((TextView) activity.findViewById(familySurnameViewId)).getText().toString();
        String firstName = ((TextView) activity.findViewById(firstNameViewId)).getText().toString();
        Gender gender = dataValidator.genderSelection(((RadioGroup) activity.findViewById(genderViewId)).getCheckedRadioButtonId());
        String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
        String ageString = ((TextView) activity.findViewById(ageViewId)).getText().toString();
        validation(participantId, surname, firstName, gender, ageString);
        if (!errorFields.isEmpty())
            throw new InvalidDataException(activity, dataValidator.getStringValue(R.string.participant), errorFields);
        return new Participant(participantId, surname, firstName, gender, Integer.parseInt(ageString), InterviewStatus.NOT_SELECTED, currentDate);
    }

    public Participant update(Participant participant, String participantId, String surname, String firstName, Gender gender, String ageString) throws InvalidDataException {
        validation(participantId, surname, firstName, gender, ageString);
        if (!errorFields.isEmpty())
            throw new InvalidDataException(activity, dataValidator.getStringValue(R.string.participant), errorFields);
        return new Participant(participant.getId(), surname, firstName, gender, Integer.parseInt(ageString), participant.getStatus());

    }

    private void validation(String participantId, String surname, String firstName, Gender gender, String ageString) {
        dataValidator.validateId(participantId, R.string.participant_id);
        errorFields= dataValidator.validateFields(surname, firstName, gender, ageString);
    }


}
