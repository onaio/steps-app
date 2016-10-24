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
import android.content.DialogInterface;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.handler.strategies.survey.interfaces.IDoNotTakeSurveyStrategy;
import com.onaio.steps.helper.CustomDialog;

/**
 * Created by onamacuser on 15/12/2015.
 */
public class IncompleteRefusedHandler  implements IMenuHandler,IMenuPreparer {

    private IDoNotTakeSurveyStrategy refusedSurveyStrategy;
    private final CustomDialog dialog;
    private Activity activity;
    private int MENU_ID = R.id.action_refused_incomplete;

    public IncompleteRefusedHandler(Activity activity, IDoNotTakeSurveyStrategy refusedSurveyStrategy) {
        this(activity, refusedSurveyStrategy, new CustomDialog());
    }

    //Constructor to be used for Testing
    IncompleteRefusedHandler(Activity activity, IDoNotTakeSurveyStrategy refusedSurveyStrategy, CustomDialog dialog) {
        this.activity = activity;
        this.refusedSurveyStrategy = refusedSurveyStrategy;
        this.dialog=dialog;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id==MENU_ID;
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
        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                refuse();
            }
        };
        dialog.confirm(activity, confirmListener, CustomDialog.EmptyListener, refusedSurveyStrategy.dialogMessage(), R.string.survey_refusal_title);
    }

    @Override
    public boolean shouldDeactivate() {
        return refusedSurveyStrategy.shouldInactivate();
    }

    @Override
    public void deactivate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.GONE);
    }

    @Override
    public void activate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.VISIBLE);
    }
}
