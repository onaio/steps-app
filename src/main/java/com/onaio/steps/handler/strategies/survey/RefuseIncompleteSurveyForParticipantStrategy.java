package com.onaio.steps.handler.strategies.survey;

import android.app.Activity;

import com.onaio.steps.R;
import com.onaio.steps.handler.strategies.survey.interfaces.IDoNotTakeSurveyStrategy;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

/**
 * Created by onamacuser on 15/12/2015.
 */
public class RefuseIncompleteSurveyForParticipantStrategy implements IDoNotTakeSurveyStrategy {
    private Participant participant;
    private Activity activity;

    public RefuseIncompleteSurveyForParticipantStrategy(Participant participant, Activity activity){

        this.participant = participant;
        this.activity = activity;
    }


    @Override
    public void open() {
        participant.setStatus(InterviewStatus.INCOMPLETE_REFUSED);
        participant.update(new DatabaseHelper(activity.getApplicationContext()));
    }

    @Override
    public boolean shouldInactivate(){
        boolean interviewIncomplete = participant.getStatus() == InterviewStatus.INCOMPLETE;
        return !interviewIncomplete;
    }

    @Override
    public int dialogMessage() {
        return R.string.participant_survey_refusal_message;
    }
}
