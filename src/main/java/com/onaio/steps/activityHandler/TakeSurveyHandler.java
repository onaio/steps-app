package com.onaio.steps.activityHandler;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuPreparer;
import com.onaio.steps.exception.AppNotInstalledException;
import com.onaio.steps.exception.FormNotPresentException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.helper.Logger;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.ODKForm.IForm;
import com.onaio.steps.model.ODKForm.ODKForm;
import com.onaio.steps.model.ODKForm.ODKSavedForm;
import com.onaio.steps.model.Participant;
import com.onaio.steps.model.RequestCode;

import java.io.IOException;
import java.util.List;

public class TakeSurveyHandler implements IMenuHandler, IMenuPreparer, IActivityResultHandler {
    private Activity activity;
    private Household household;
    private Participant participant;
    private static final int MENU_ID = R.id.action_take_survey;

    public TakeSurveyHandler(Activity activity, Participant participant) {
        this.activity = activity;
        this.participant = participant;
    }


    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    public TakeSurveyHandler(Activity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean open() {
        try {
            String formId = getValue(Constants.FORM_ID);
            if (participant == null) {
                String formName = String.format(formId + "-%s", household.getName());
                ODKForm requiredForm = ODKForm.create(activity, formId, formName);
                requiredForm.open(household, activity, RequestCode.SURVEY.getCode());
            } else {
                String formName = String.format(formId + "-%s", participant.getId());
                ODKForm requiredForm = ODKForm.create(activity, formId, formName);
                requiredForm.open(participant, activity, RequestCode.SURVEY.getCode());
            }
        } catch (FormNotPresentException e) {
            new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.error_title, R.string.form_not_present);
        } catch (AppNotInstalledException e) {
            new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.error_title, R.string.odk_app_not_installed);
        } catch (IOException e) {
            new Logger().log(e, "Failed to save csv file while opening the ODK Form");
            new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.error_title, R.string.something_went_wrong_try_again);
        }

        return true;
    }

    @Override
    public boolean shouldInactivate() {
        InterviewStatus status;
        if (participant == null) {
            status = household.getStatus();
            boolean selected = status == InterviewStatus.NOT_DONE;
            boolean deferred = status == InterviewStatus.DEFERRED;
            boolean incomplete = status == InterviewStatus.INCOMPLETE;
            return !(selected || deferred || incomplete);
        } else {
            status = participant.getStatus();
            boolean doneStatus = status == InterviewStatus.DONE;
            boolean refusedStatus = status == InterviewStatus.REFUSED;
            return doneStatus || refusedStatus;
        }
    }

    @Override
    public void inactivate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.INVISIBLE);
    }

    @Override
    public void activate() {
        Button button = (Button) activity.findViewById(MENU_ID);
        button.setVisibility(View.VISIBLE);
        if ( household!= null && InterviewStatus.INCOMPLETE.equals(household.getStatus()))
            button.setText(R.string.continue_interview);
        else
            button.setText(R.string.interview_now);

        if( participant!=null && InterviewStatus.INCOMPLETE.equals(participant.getStatus()))
        button.setText(R.string.continue_interview);
        else
            button.setText(R.string.enter_data_now);

    }

    @Override
    public void handleResult(Intent data, int resultCode) {
        if (resultCode != Activity.RESULT_OK)
            return;
        List<IForm> savedForms = getSavedForms();
        if (savedForms == null || savedForms.isEmpty())
            return;
        ODKSavedForm savedForm = (ODKSavedForm) savedForms.get(0);
        if (household != null) {

            if (Constants.ODK_FORM_COMPLETE_STATUS.equals(savedForm.getStatus()))
                household.setStatus(InterviewStatus.DONE);
            else
                household.setStatus(InterviewStatus.INCOMPLETE);

            household.update(new DatabaseHelper(activity));

        } else {
            if (Constants.ODK_FORM_COMPLETE_STATUS.equals(savedForm.getStatus()))
                participant.setStatus(InterviewStatus.DONE);
            else
                participant.setStatus(InterviewStatus.INCOMPLETE);

            participant.update(new DatabaseHelper(activity));
        }

    }

    protected List<IForm> getSavedForms() {
        try {
            String formNameFormat = getValue(Constants.FORM_ID) + "-%s";
            if (household != null) {
                return ODKSavedForm.findAll(activity, String.format(formNameFormat, household.getName()));
            } else
                return ODKSavedForm.findAll(activity, String.format(formNameFormat, participant.getId()));
        } catch (AppNotInstalledException e) {
            new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.error_title, R.string.odk_app_not_installed);
            return null;
        }
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == RequestCode.SURVEY.getCode();
    }

    private String getValue(String key) {
        return KeyValueStoreFactory.instance(activity).getString(key);
    }
}
