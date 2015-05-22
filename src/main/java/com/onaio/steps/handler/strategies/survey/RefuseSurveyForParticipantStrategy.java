package com.onaio.steps.handler.strategies.survey;


import android.app.Activity;

import com.onaio.steps.R;
import com.onaio.steps.handler.strategies.survey.interfaces.IDoNotTakeSurveyStrategy;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

public class RefuseSurveyForParticipantStrategy implements IDoNotTakeSurveyStrategy {
    private Participant participant;
    private Activity activity;

    public RefuseSurveyForParticipantStrategy(Participant participant, Activity activity){

        this.participant = participant;
        this.activity = activity;
    }


    @Override
    public void open() {
        participant.setStatus(InterviewStatus.REFUSED);
        participant.update(new DatabaseHelper(activity.getApplicationContext()));
    }

    @Override
    public boolean shouldInactivate(){
        boolean deferredStatus = participant.getStatus() == InterviewStatus.DEFERRED;
        boolean notSelectedStatus = participant.getStatus() == InterviewStatus.NOT_SELECTED;
        return  !(deferredStatus || notSelectedStatus);
    }

    @Override
    public int dialogMessage() {
        return R.string.participant_survey_refusal_message;
    }
}
