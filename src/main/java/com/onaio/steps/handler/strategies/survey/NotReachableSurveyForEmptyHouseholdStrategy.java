package com.onaio.steps.handler.strategies.survey;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

public class NotReachableSurveyForEmptyHouseholdStrategy extends NotReachableSurveyForHouseholdStrategy{

    public NotReachableSurveyForEmptyHouseholdStrategy(Household household, AppCompatActivity activity) {
        super(household, activity);
    }

    @Override
    public boolean shouldInactivate() {
        return household.getStatus() != InterviewStatus.EMPTY_HOUSEHOLD;
    }
}
