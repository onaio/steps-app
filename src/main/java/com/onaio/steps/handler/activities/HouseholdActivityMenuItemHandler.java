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

import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activities.MainActivityOrchestrator;
import com.onaio.steps.activities.ParticipantListActivity;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.orchestrators.flows.HouseholdFlow;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 04/06/2018.
 */

public class HouseholdActivityMenuItemHandler implements IMenuHandler {

    private static final int MENU_ID= R.id.action_open_households;
    private ParticipantListActivity activity;

    public HouseholdActivityMenuItemHandler(Activity activity) {
        if (activity instanceof ParticipantListActivity) {
            this.activity = (ParticipantListActivity) activity;
        }
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return MENU_ID == menu_id;
    }

    @Override
    public boolean open() {
        HouseholdFlow householdFlow = new HouseholdFlow(activity);
        householdFlow.saveSafely(activity, Constants.FLOW_TYPE, FlowType.Household.toString());
        activity.startActivity(new Intent(activity, MainActivityOrchestrator.class));
        activity.finish();

        return true;
    }
}
