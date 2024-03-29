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

import android.content.DialogInterface;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.handler.strategies.survey.interfaces.IDoNotTakeSurveyStrategy;
import com.onaio.steps.helper.CustomDialog;

public class NotReachableHandler implements IMenuHandler,IMenuPreparer {

    private final IDoNotTakeSurveyStrategy refusedSurveyStrategy;
    private final CustomDialog dialog;
    private final AppCompatActivity activity;

    public NotReachableHandler(AppCompatActivity activity, IDoNotTakeSurveyStrategy refusedSurveyStrategy) {
        this(activity, refusedSurveyStrategy, new CustomDialog());
    }

    //Constructor to be used for Testing
    NotReachableHandler(AppCompatActivity activity, IDoNotTakeSurveyStrategy refusedSurveyStrategy, CustomDialog dialog) {
        this.activity = activity;
        this.refusedSurveyStrategy = refusedSurveyStrategy;
        this.dialog=dialog;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id==getViewId();
    }

    @Override
    public boolean open() {
        confirm();
        return true;
    }

    private void refuse() {
        refusedSurveyStrategy.open();
        new BackHomeHandler(activity).open();
    }

    private void confirm() {
        DialogInterface.OnClickListener confirmListener = (dialogInterface, i) -> refuse();
        dialog.confirm(activity, confirmListener, CustomDialog.EmptyListener, refusedSurveyStrategy.dialogMessage(), R.string.survey_not_reachable_title);
    }

    @Override
    public boolean shouldDeactivate() {
        return refusedSurveyStrategy.shouldInactivate();
    }

    @Override
    public void deactivate() {
        View item = activity.findViewById(getViewId());
        item.setVisibility(View.GONE);
    }

    @Override
    public void activate() {
        View item = activity.findViewById(getViewId());
        item.setVisibility(View.VISIBLE);
    }

    public int getViewId() {
        return R.id.action_not_reachable;
    }
}
