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

package com.onaio.steps.handler.activities;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.handler.interfaces.IListItemHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;

public class HouseholdActivityHandler implements IListItemHandler {

    private AppCompatActivity activity;
    private Household household;

    public HouseholdActivityHandler(AppCompatActivity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean open() {
        if (household == null) return true;
        Intent intent = new Intent(activity, HouseholdActivity.class);
        intent.putExtra(Constants.HH_HOUSEHOLD, household);
        activity.startActivity(intent);
        return true;
    }
}
