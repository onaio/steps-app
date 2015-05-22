package com.onaio.steps.handler.strategies.survey;


import android.app.Activity;

import com.onaio.steps.R;
import com.onaio.steps.handler.strategies.survey.interfaces.IDoNotTakeSurveyStrategy;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

public class DeferSurveyForParticipantStrategy implements IDoNotTakeSurveyStrategy {
    private Participant participant;
    private Activity activity;

    public DeferSurveyForParticipantStrategy(Participant participant, Activity activity){

        this.participant = participant;
        this.activity = activity;
    }

    @Override
    public void open(){
        participant.setStatus(InterviewStatus.DEFERRED);
        long update = participant.update(new DatabaseHelper(activity));
    }

    @Override
    public boolean shouldInactivate()
    {
        boolean participantNotSelected = participant.getStatus() == InterviewStatus.NOT_SELECTED;
        return !participantNotSelected;
    }

    @Override
    public int dialogMessage() {
        return R.string.participant_survey_deferred_message;
    }
}
