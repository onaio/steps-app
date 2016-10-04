/*
 * Copyright 2016. World Health Organization
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onaio.steps.model.ODKForm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import com.onaio.steps.exceptions.AppNotInstalledException;
import com.onaio.steps.exceptions.FormNotPresentException;
import com.onaio.steps.model.ODKForm.strategy.interfaces.IFormStrategy;
import com.onaio.steps.helper.Constants;

import java.io.IOException;
import java.util.List;

public class ODKForm {
    private IForm savedForm;
    private IForm blankForm;


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
