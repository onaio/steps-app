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

import android.app.Activity;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

public class SelectedParticipantContainerHandler implements IMenuPreparer {
    private final int MENU_ID = R.id.selected_participant;
    private Activity activity;
    private Household household;

    public SelectedParticipantContainerHandler(Activity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean shouldDeactivate() {
        InterviewStatus status = household.getStatus();
        boolean notDone = status.equals(InterviewStatus.NOT_DONE);
        boolean deferred = status.equals(InterviewStatus.DEFERRED);
        boolean incomplete = status.equals(InterviewStatus.INCOMPLETE);
        return !notDone && !deferred && !incomplete;
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
