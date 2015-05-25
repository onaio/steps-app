package com.onaio.steps.handler.strategies.survey;


import android.app.Activity;
import android.widget.Button;

import com.onaio.steps.R;
import com.onaio.steps.handler.strategies.survey.interfaces.ITakeSurveyStrategy;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.ODKForm.ODKForm;
import com.onaio.steps.model.ODKForm.ODKSavedForm;
import com.onaio.steps.model.ODKForm.strategy.HouseholdMemberFormStrategy;
import com.onaio.steps.model.RequestCode;

import java.io.IOException;

public class TakeSurveyForHouseholdStrategy  implements ITakeSurveyStrategy {
    private Household household;
    private Activity activity;
    private static final int MENU_ID = R.id.action_take_survey;

    public TakeSurveyForHouseholdStrategy(Household household,Activity activity){
        this.household = household;
        this.activity = activity;
    }

    @Override
    public void open(String formId) throws IOException {
        String formName = String.format(formId + "-%s", household.getName());
        ODKForm requiredForm = ODKForm.create(activity, formId, formName);
        requiredForm.open(new HouseholdMemberFormStrategy(household), activity, RequestCode.SURVEY.getCode());
    }

    public boolean shouldInactivate(){
        InterviewStatus status = household.getStatus();
        boolean selected = status == InterviewStatus.NOT_DONE;
        boolean deferred = status == InterviewStatus.DEFERRED;
        boolean incomplete = status == InterviewStatus.INCOMPLETE;
        return !(selected || deferred || incomplete);
    }

    public void handleResult(ODKSavedForm savedForm){
        if (Constants.ODK_FORM_COMPLETE_STATUS.equals(savedForm.getStatus()))
            household.setStatus(InterviewStatus.DONE);
        else
            household.setStatus(InterviewStatus.INCOMPLETE);

        household.update(new DatabaseHelper(activity));
    }

    @Override
    public String getFormName(String formNameFormat) {
        return String.format(formNameFormat, household.getName());
    }

    @Override
    public void activate() {
        Button button = (Button) activity.findViewById(MENU_ID);
        if (InterviewStatus.INCOMPLETE.equals(household.getStatus()))
            button.setText(R.string.continue_interview);
        else
            button.setText(R.string.interview_now);
    }
}
