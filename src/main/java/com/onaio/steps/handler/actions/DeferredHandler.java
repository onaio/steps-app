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

public class DeferredHandler implements IMenuHandler,IMenuPreparer {

    private IDoNotTakeSurveyStrategy deferredStrategy;
    private final CustomDialog dialog;
    private AppCompatActivity activity;
    private int MENU_ID = R.id.action_deferred;

    public DeferredHandler(AppCompatActivity activity, IDoNotTakeSurveyStrategy deferredStrategy) {
        this(activity,deferredStrategy,new CustomDialog());
    }

    DeferredHandler(AppCompatActivity activity, IDoNotTakeSurveyStrategy deferredStrategy, CustomDialog dialog) {
        this.activity = activity;
        this.deferredStrategy = deferredStrategy;
        this.dialog = dialog;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new BackHomeHandler(activity).open();
            }
        };
        deferredStrategy.open();
        dialog.notify(activity, confirmListener, R.string.survey_deferred_title, deferredStrategy.dialogMessage());
        return true;
    }

    @Override
    public boolean shouldDeactivate() {
        return deferredStrategy.shouldInactivate();
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
