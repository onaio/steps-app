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

import android.app.ListActivity;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

public class SelectedParticipantActionsHandler implements IMenuPreparer {
    private ListActivity activity;
    private Household household;
    private static final int MENU_ID= R.id.selected_participant_actions;

    public SelectedParticipantActionsHandler(ListActivity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean shouldInactivate() {
        boolean selected = household.getStatus() == InterviewStatus.NOT_DONE;
        boolean deferred = household.getStatus() == InterviewStatus.DEFERRED;
        boolean incomplete = household.getStatus() == InterviewStatus.INCOMPLETE;
        return !(selected || deferred || incomplete);
    }

    @Override
    public void inactivate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.GONE);
    }

    @Override
    public void activate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.VISIBLE);
    }
}
