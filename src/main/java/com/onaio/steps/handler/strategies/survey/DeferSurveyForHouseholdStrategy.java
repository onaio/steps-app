package com.onaio.steps.handler.strategies.survey;


import android.app.Activity;

import com.onaio.steps.R;
import com.onaio.steps.handler.strategies.survey.interfaces.IDoNotTakeSurveyStrategy;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

public class DeferSurveyForHouseholdStrategy implements IDoNotTakeSurveyStrategy {
    private Household household;
    private Activity activity;

    public DeferSurveyForHouseholdStrategy(Household household, Activity activity){

        this.household = household;
        this.activity = activity;
    }

    @Override
    public void open(){
        household.setStatus(InterviewStatus.DEFERRED);
        household.update(new DatabaseHelper(activity.getApplicationContext()));
    }

    @Override
    public boolean shouldInactivate(){
        boolean memberSelected = household.getStatus() == InterviewStatus.NOT_DONE;
        return !memberSelected;
    }

    @Override
    public int dialogMessage() {
        return R.string.survey_deferred_message;
    }
}
