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

package com.onaio.steps.handler.actions;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.exceptions.AppNotInstalledException;
import com.onaio.steps.handler.exceptions.ExceptionHandler;
import com.onaio.steps.handler.exceptions.IResolvableException;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.handler.strategies.survey.interfaces.ITakeSurveyStrategy;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.ODKForm.IForm;
import com.onaio.steps.model.ODKForm.ODKSavedForm;
import com.onaio.steps.model.RequestCode;

import java.util.List;

public class TakeSurveyHandler implements IMenuHandler, IMenuPreparer, IActivityResultHandler, IResolvableException {
    private final AppCompatActivity activity;
    private final ITakeSurveyStrategy takeSurveyStrategy;
    private static final int MENU_ID = R.id.action_take_survey;
    private final ExceptionHandler exceptionHandler;

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    public TakeSurveyHandler(AppCompatActivity activity, ITakeSurveyStrategy takeSurveyStrategy) {
        this.activity = activity;
        this.takeSurveyStrategy = takeSurveyStrategy;
        exceptionHandler = new ExceptionHandler(activity, this);
    }

    @Override
    public boolean open() {
        try {
            Toast.makeText(activity, R.string.loading_form, Toast.LENGTH_LONG).show();
            takeSurveyStrategy.open();
        } catch (Exception e) {
            exceptionHandler.handle(e);
        }

        return true;
    }

    @Override
    public boolean shouldDeactivate() {
        return takeSurveyStrategy.shouldInactivate();
    }

    @Override
    public void deactivate() {
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
        if (resultCode != Activity.RESULT_OK || data.getData() == null)
            return;
        List<IForm> savedForms = getSavedForms(data);
        if (savedForms == null || savedForms.isEmpty())
            return;
        ODKSavedForm savedForm = (ODKSavedForm) savedForms.get(0);
        takeSurveyStrategy.handleResult(savedForm);

    }
    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == RequestCode.SURVEY.getCode();
    }

    protected List<IForm> getSavedForms(Intent data) {
        try {
            String formId = data.getData().getLastPathSegment();
            return ODKSavedForm.findAll(activity, formId);
        } catch (AppNotInstalledException e) {
            new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.error_title, R.string.odk_app_not_installed);
            return null;
        }
    }

    private String getValue(String key) {
        return KeyValueStoreFactory.instance(activity).getString(key);
    }

    @Override
    public void tryToResolve() {
        open();
    }
}
