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
import android.view.Menu;
import android.view.MenuItem;

import com.onaio.steps.R;
import com.onaio.steps.activities.EditParticipantActivity;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;
import com.onaio.steps.model.RequestCode;

import static android.app.Activity.RESULT_OK;


public class EditParticipantActivityHandler implements IMenuHandler, IActivityResultHandler, IMenuPreparer {

    private final int MENU_ID = R.id.action_edit;
    private  Activity activity;
    private Participant participant;
    private Menu menu;

    public EditParticipantActivityHandler(Activity activity, Participant participant) {
        this.activity=activity;
        this.participant=participant;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        if(participant==null) return true;
        Intent intent = new Intent(activity, EditParticipantActivity.class);
        intent.putExtra(Constants.PARTICIPANT,participant);
        activity.startActivityForResult(intent,RequestCode.EDIT_PARTICIPANT.getCode());
        return true;
    }

    @Override
    public void handleResult(Intent intent, int resultCode) {
        if (resultCode != RESULT_OK)
            return ;
        activity.finish();
        participant = (Participant)intent.getSerializableExtra(Constants.PARTICIPANT);
        new ParticipantActivityHandler(activity, this.participant).open();
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == RequestCode.EDIT_PARTICIPANT.getCode();
    }

    @Override
    public boolean shouldInactivate() {
        boolean doneStatus = participant.getStatus() == InterviewStatus.DONE;
        boolean refusedStatus = participant.getStatus() == InterviewStatus.REFUSED;
        boolean incompleteStatus = participant.getStatus() == InterviewStatus.INCOMPLETE;
        boolean incompleteRefused = participant.getStatus() == InterviewStatus.INCOMPLETE_REFUSED;
        return doneStatus || refusedStatus || incompleteStatus || incompleteRefused;
    }

    @Override
    public void inactivate() {
        MenuItem menuItem = menu.findItem(MENU_ID);
        menuItem.setEnabled(false);
    }

    @Override
    public void activate() {
        MenuItem menuItem = menu.findItem(MENU_ID);
        menuItem.setEnabled(true);
    }

    public IMenuPreparer withMenu(Menu menu) { this.menu =menu; return this;    }
}
