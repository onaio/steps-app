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

import com.onaio.steps.activities.ParticipantActivity;
import com.onaio.steps.handler.interfaces.IListItemHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Participant;


public class ParticipantActivityHandler implements IListItemHandler {

    private Activity activity;
    private Participant participant;

    public ParticipantActivityHandler(Activity activity, Participant participant) {
        this.activity = activity;
        this.participant=participant;
    }

    @Override
    public boolean open() {
        if (participant == null) return true;
        Intent intent = new Intent(activity, ParticipantActivity.class);
        intent.putExtra(Constants.PARTICIPANT, participant);
        activity.startActivity(intent);
        return true;
    }
}
