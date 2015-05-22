package com.onaio.steps.handler.strategies;


import android.app.Activity;

import com.onaio.steps.R;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

public class RefuseSurveyForHouseholdStrategy implements IDoNotTakeSurveyStrategy {
    private Household household;
    private Activity activity;

    public RefuseSurveyForHouseholdStrategy(Household household, Activity activity){

        this.household = household;
        this.activity = activity;
    }


    @Override
    public void open() {
        household.setStatus(InterviewStatus.REFUSED);
        household.update(new DatabaseHelper(activity.getApplicationContext()));
    }

    @Override
    public boolean shouldInactivate(){
        boolean memberSelected = household.getStatus() == InterviewStatus.NOT_DONE;
        boolean memberDeferred = household.getStatus() == InterviewStatus.DEFERRED;
        return !(memberSelected || memberDeferred);
    }


    @Override
    public int dialogMessage() {
        return R.string.survey_refusal_message;
    }
}
