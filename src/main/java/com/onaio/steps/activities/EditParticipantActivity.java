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

package com.onaio.steps.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Participant;
import com.onaio.steps.modelViewWrapper.ParticipantViewWrapper;

import static com.onaio.steps.helper.Constants.PARTICIPANT;

public class EditParticipantActivity extends Activity{


    private ParticipantViewWrapper participantViewWrapper;
    private Participant participant;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateView();
        intent = this.getIntent();
        participant=(Participant) intent.getSerializableExtra(Constants.PARTICIPANT);
        participantViewWrapper = new ParticipantViewWrapper(this);
        populateFields();
    }

    private void populateView() {
        setContentView(R.layout.participant_form);
        TextView header = (TextView) findViewById(R.id.form_header);
        header.setText(R.string.edit_participant_header);
    }

    private void populateFields() {
        participantViewWrapper.updateView(participant);
    }


    public void doneBtnClicked(View view) {
        try{
            participant = participantViewWrapper.updateFromView(participant);
            participant.update(new DatabaseHelper(this));
            intent.putExtra(PARTICIPANT,participant);
            setResult(RESULT_OK, intent);
            finish();
        } catch (InvalidDataException e) {
            new CustomDialog().notify(this, CustomDialog.EmptyListener,e.getMessage(),R.string.error_title);
        }
    }

    public void cancel(View view){
        finish();
    }
}
