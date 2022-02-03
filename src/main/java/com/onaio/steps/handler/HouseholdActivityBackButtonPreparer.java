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

package com.onaio.steps.handler;

import android.app.ActionBar;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

/**
 * This preparer determines whether the back button in the Household Activity should be shown
 *
 * Created by Jason Rogena - jrogena@ona.io on 21/10/2016.
 */

public class HouseholdActivityBackButtonPreparer implements IMenuPreparer {
    private AppCompatActivity activity;
    private Household household;

    public HouseholdActivityBackButtonPreparer(AppCompatActivity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean shouldDeactivate() {
        InterviewStatus status = household.getStatus();
        boolean notDone = status.equals(InterviewStatus.NOT_DONE);
        return notDone;
    }

    @Override
    public void deactivate() {
        //hide the go up button
        ActionBar actionBar = activity.getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
        }
    }

    @Override
    public void activate() {
        //show the go up button
        ActionBar actionBar = activity.getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true); // enable the button
            actionBar.setDisplayShowHomeEnabled(true); // add the icon
        }
    }
}
