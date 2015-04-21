package com.onaio.steps.model.ODKForm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;

import com.onaio.steps.exception.AppNotInstalledException;
import com.onaio.steps.exception.FormNotPresentException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;

import java.io.IOException;
import java.util.List;

public abstract class ODKForm {
    String _id;
    String jrFormId;
    String displayName;
    String jrVersion;

    public ODKForm(String jrVersion, String displayName, String jrFormId, String id) {
        this.jrVersion = jrVersion;
        this.displayName = displayName;
        this.jrFormId = jrFormId;
        _id = id;
    }

    public abstract Uri getUri();
    public abstract void open(Household household, Activity activity) throws IOException;

    public static ODKForm get(Activity activity, String formId, String formName) throws FormNotPresentException, AppNotInstalledException {
        List<ODKSavedForm> savedForms = ODKSavedForm.getWithName(activity, formName);
        if(savedForms == null || savedForms.isEmpty())
            return ODKEntryForm.getWithId(activity,formId);
        return savedForms.get(0);
    }

    protected void launchODKCollect(Activity activity) {
        Intent surveyIntent = new Intent();
        surveyIntent.setComponent(new ComponentName(Constants.ODK_COLLECT_PACKAGE,Constants.ODK_COLLECT_FORM_CLASS));
        surveyIntent.setAction(Intent.ACTION_EDIT);
        surveyIntent.setData(getUri());
        activity.startActivityForResult(surveyIntent, Constants.SURVEY_IDENTIFIER);
    }

}
