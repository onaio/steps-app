package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.activity.ParticipantActivity;
import com.onaio.steps.activityHandler.Interface.IListItemHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Participant;


public class ParticipantActivityHandler implements IListItemHandler {

    private ListActivity activity;
    private Participant participant;

    public ParticipantActivityHandler(ListActivity activity, Participant participant) {
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
