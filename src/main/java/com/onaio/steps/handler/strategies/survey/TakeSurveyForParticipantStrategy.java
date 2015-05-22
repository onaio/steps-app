package com.onaio.steps.handler.strategies.survey;


import android.app.Activity;

import com.onaio.steps.handler.strategies.form.ParticipantFormStrategy;
import com.onaio.steps.handler.strategies.survey.interfaces.ITakeSurveyStrategy;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.ODKForm.ODKForm;
import com.onaio.steps.model.ODKForm.ODKSavedForm;
import com.onaio.steps.model.Participant;
import com.onaio.steps.model.RequestCode;

import java.io.IOException;

public class TakeSurveyForParticipantStrategy  implements ITakeSurveyStrategy {
    private Participant participant;
    private Activity activity;

    public TakeSurveyForParticipantStrategy(Participant participant, Activity activity){

        this.participant = participant;
        this.activity = activity;
    }

    @Override
    public void open(String formId) throws IOException {
        String formName = String.format(formId + "-%s", participant.getId());
        ODKForm requiredForm = ODKForm.create(activity, formId, formName);
        requiredForm.open(new ParticipantFormStrategy(participant), activity, RequestCode.SURVEY.getCode());
    }

    @Override
    public boolean shouldInactivate(){
        InterviewStatus status = participant.getStatus();
        boolean doneStatus = status == InterviewStatus.DONE;
        boolean refusedStatus = status == InterviewStatus.REFUSED;
        return doneStatus || refusedStatus;
    }

    @Override
    public void handleResult(ODKSavedForm savedForm){
        if (Constants.ODK_FORM_COMPLETE_STATUS.equals(savedForm.getStatus()))
            participant.setStatus(InterviewStatus.DONE);
        else
            participant.setStatus(InterviewStatus.INCOMPLETE);
        long update = participant.update(new DatabaseHelper(activity));
    }

    @Override
    public String getFormName(String formNameFormat) {
        return String.format(formNameFormat, participant.getId());
    }

}
