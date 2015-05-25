package com.onaio.steps.handler.actions;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.onaio.steps.R;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.exceptions.AppNotInstalledException;
import com.onaio.steps.exceptions.FormNotPresentException;
import com.onaio.steps.handler.strategies.survey.interfaces.ITakeSurveyStrategy;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.helper.Logger;
import com.onaio.steps.model.ODKForm.IForm;
import com.onaio.steps.model.ODKForm.ODKSavedForm;
import com.onaio.steps.model.RequestCode;

import java.io.IOException;
import java.util.List;

public class TakeSurveyHandler implements IMenuHandler, IMenuPreparer, IActivityResultHandler {
    private Activity activity;
    private ITakeSurveyStrategy takeSurveyStrategy;
    private static final int MENU_ID = R.id.action_take_survey;

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    public TakeSurveyHandler(Activity activity, ITakeSurveyStrategy takeSurveyStrategy) {
        this.activity = activity;
        this.takeSurveyStrategy = takeSurveyStrategy;
    }

    @Override
    public boolean open() {
        try {
            String formId = getValue(Constants.FORM_ID);
            takeSurveyStrategy.open(formId);
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
        return takeSurveyStrategy.shouldInactivate();
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

        takeSurveyStrategy.activate();
    }

    @Override
    public void handleResult(Intent data, int resultCode) {
        if (resultCode != Activity.RESULT_OK)
            return;
        List<IForm> savedForms = getSavedForms();
        if (savedForms == null || savedForms.isEmpty())
            return;
        ODKSavedForm savedForm = (ODKSavedForm) savedForms.get(0);
        takeSurveyStrategy.handleResult(savedForm);

    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == RequestCode.SURVEY.getCode();
    }

    protected List<IForm> getSavedForms() {
        try {
            String formNameFormat = getValue(Constants.FORM_ID) + "-%s";
            return ODKSavedForm.findAll(activity, takeSurveyStrategy.getFormName(formNameFormat));
        } catch (AppNotInstalledException e) {
            new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.error_title, R.string.odk_app_not_installed);
            return null;
        }
    }

    private String getValue(String key) {
        return KeyValueStoreFactory.instance(activity).getString(key);
    }


}
