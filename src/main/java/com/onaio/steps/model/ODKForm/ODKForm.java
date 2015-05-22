package com.onaio.steps.model.ODKForm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import com.onaio.steps.exception.AppNotInstalledException;
import com.onaio.steps.exception.FormNotPresentException;
import com.onaio.steps.handler.strategies.form.interfaces.IFormStrategy;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Participant;

import java.io.IOException;
import java.util.List;

public class ODKForm {
    private IForm savedForm;
    private IForm blankForm;

//    public ODKForm(IForm blankForm, IForm savedForm){
//        this(blankForm,savedForm);
//    }

    public ODKForm(IForm blankForm, IForm savedForm){
        this.blankForm = blankForm;
        this.savedForm = savedForm;
    }

    private IForm getForm(){
        if(savedForm!= null)
            return savedForm;
        return blankForm;
    }

    public void open(IFormStrategy formStrategy, Activity activity, int requestCode) throws IOException{
        String pathToSaveDataFile = blankForm.getPath();
        formStrategy.saveDataFile(activity,pathToSaveDataFile);
        launchODKCollect(activity, requestCode);
    }

    public static ODKForm create(Activity activity, String formId, String formName) throws FormNotPresentException, AppNotInstalledException {
        List<IForm> savedForms = ODKSavedForm.findAll(activity, formName);
        IForm blankForm = ODKBlankForm.find(activity, formId);
        if(savedForms != null && !savedForms.isEmpty())
            return new ODKForm(blankForm,savedForms.get(0));
        return new ODKForm(blankForm,null);
    }

    private void launchODKCollect(Activity activity, int requestCode) {
        Intent surveyIntent = new Intent();
        surveyIntent.setComponent(new ComponentName(Constants.ODK_COLLECT_PACKAGE,Constants.ODK_COLLECT_FORM_CLASS));
        surveyIntent.setAction(Intent.ACTION_EDIT);
        surveyIntent.setData(getForm().getUri());
        activity.startActivityForResult(surveyIntent, requestCode);
    }
}
