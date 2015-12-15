package com.onaio.steps.handler.strategies.survey;

import android.app.Activity;

import com.onaio.steps.R;
import com.onaio.steps.handler.strategies.survey.interfaces.IDoNotTakeSurveyStrategy;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

/**
 * Created by onamacuser on 15/12/2015.
 */
public class RefuseIncompleteSurveyForHouseholdStrategy implements IDoNotTakeSurveyStrategy {
    private Household household;
    private Activity activity;

    public RefuseIncompleteSurveyForHouseholdStrategy(Household household, Activity activity){

        this.household = household;
        this.activity = activity;
    }


    @Override
    public void open() {
        household.setStatus(InterviewStatus.INCOMPLETE_REFUSED);
        household.update(new DatabaseHelper(activity.getApplicationContext()));
    }

    @Override
    public boolean shouldInactivate(){
        boolean interviewIncomplete = household.getStatus() == InterviewStatus.INCOMPLETE;
        return !interviewIncomplete;
    }


    @Override
    public int dialogMessage() {
        return R.string.survey_refusal_message;
    }
}
