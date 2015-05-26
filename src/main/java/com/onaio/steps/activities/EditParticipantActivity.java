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


    public void save(View view) {
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
