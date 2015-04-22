package com.onaio.steps.activityHandler;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuPreparer;
import com.onaio.steps.exception.AppNotInstalledException;
import com.onaio.steps.exception.FormNotPresentException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.Dialog;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.ODKForm.IForm;
import com.onaio.steps.model.ODKForm.ODKForm;
import com.onaio.steps.model.ODKForm.ODKSavedForm;

import java.io.IOException;
import java.util.List;

public class TakeSurveyHandler implements IMenuHandler, IMenuPreparer, IActivityResultHandler {
    private ListActivity activity;
    private Household household;
    private static final int MENU_ID= R.id.action_take_survey;


    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    public TakeSurveyHandler(ListActivity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean open() {
        try {
            String formName = String.format(Constants.ODK_FORM_NAME_FORMAT, household.getName());
            ODKForm requiredForm = ODKForm.create(activity, Constants.ODK_FORM_ID, formName);
            requiredForm.open(household, activity, Constants.SURVEY_IDENTIFIER);
        } catch (FormNotPresentException e) {
            new Dialog().notify(activity,Dialog.EmptyListener, R.string.error_title, R.string.form_not_present);
        } catch (AppNotInstalledException e) {
            new Dialog().notify(activity,Dialog.EmptyListener, R.string.error_title, R.string.odk_app_not_installed);
        } catch (IOException e) {
            new Dialog().notify(activity,Dialog.EmptyListener, R.string.error_title, R.string.something_went_wrong_try_again);
        }

        return true;
    }

    @Override
    public boolean shouldInactivate() {
        boolean selected = household.getStatus() == HouseholdStatus.NOT_DONE;
        boolean deferred = household.getStatus() == HouseholdStatus.DEFERRED;
        return !(selected || deferred );
    }

    @Override
    public void inactivate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.INVISIBLE);
    }

    @Override
    public void activate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.VISIBLE);
    }

    @Override
    public void handleResult(Intent data, int resultCode) {
        if(resultCode != Activity.RESULT_OK)
            return;
        try {
            List<IForm> forms = ODKSavedForm.findAll(activity, String.format(Constants.ODK_FORM_NAME_FORMAT, household.getName()));
            if(forms == null || forms.isEmpty())
                return;
            ODKSavedForm savedForm = (ODKSavedForm)forms.get(0);
            if(Constants.ODK_FORM_COMPLETE_STATUS.equals(savedForm.getStatus())) {
                household.setStatus(HouseholdStatus.DONE);
                household.update(new DatabaseHelper(activity));
            }
        }catch (AppNotInstalledException e) {
            new Dialog().notify(activity,Dialog.EmptyListener, R.string.error_title, R.string.odk_app_not_installed);
        }
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode==Constants.SURVEY_IDENTIFIER;
    }
}
