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

import android.content.ComponentName;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.exceptions.AppNotInstalledException;
import com.onaio.steps.exceptions.FormNotPresentException;
import com.onaio.steps.helper.Constants;

import java.util.List;

public class ODKForm {
    private final IForm savedForm;
    private final IForm blankForm;


    public ODKForm(IForm blankForm, IForm savedForm){
        this.blankForm = blankForm;
        this.savedForm = savedForm;
    }

    private IForm getForm(){
        if(savedForm!= null)
            return savedForm;
        return blankForm;
    }

    public void open(AppCompatActivity activity, int requestCode){
        launchODKCollect(activity, requestCode);
    }

    public static ODKForm create(AppCompatActivity activity, String formId, String odkFormId) throws FormNotPresentException, AppNotInstalledException {
        List<IForm> savedForms = ODKSavedForm.findAll(activity, odkFormId);
        IForm blankForm = ODKBlankForm.find(activity, formId);
        if(!savedForms.isEmpty())
            return new ODKForm(blankForm,savedForms.get(0));
        return new ODKForm(blankForm,null);
    }

    private void launchODKCollect(AppCompatActivity activity, int requestCode) {
        Intent surveyIntent = new Intent();
        surveyIntent.setComponent(new ComponentName(Constants.ODK_COLLECT_PACKAGE,Constants.ODK_COLLECT_FORM_CLASS));
        surveyIntent.setAction(Intent.ACTION_EDIT);
        surveyIntent.setData(getForm().getUri());
        activity.startActivityForResult(surveyIntent, requestCode);
    }
}
