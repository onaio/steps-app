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

import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activities.NewParticipantActivity;
import com.onaio.steps.activities.ParticipantActivity;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.adapters.ParticipantAdapter;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Participant;
import com.onaio.steps.model.RequestCode;

import static android.app.Activity.RESULT_OK;

public class NewParticipantActivityHandler implements IActivityResultHandler, IMenuHandler {

    private ListActivity activity;

    public NewParticipantActivityHandler(ListActivity activity) {
        this.activity = activity;
    }


    @Override
    public void handleResult(Intent data, int resultCode) {

        if (resultCode == RESULT_OK) {
            ParticipantAdapter participantAdapter = (ParticipantAdapter) activity.getListView().getAdapter();
            if (participantAdapter == null)
                return;
            participantAdapter.reinitialize(Participant.getAllParticipants(new DatabaseHelper(activity.getApplicationContext())));
            participantAdapter.notifyDataSetChanged();

            Intent participantActivityIntent = new Intent(activity, ParticipantActivity.class);
            participantActivityIntent.putExtra(Constants.PARTICIPANT, data.getSerializableExtra(Constants.PARTICIPANT));
            activity.startActivity(participantActivityIntent);
        }

    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == RequestCode.NEW_PARTICIPANT.getCode();
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.action_add_new_item;
    }

    @Override
    public boolean open() {
        Intent intent = new Intent(activity.getBaseContext(), NewParticipantActivity.class);
        activity.startActivityForResult(intent,RequestCode.NEW_PARTICIPANT.getCode());
        return true;
    }
}
