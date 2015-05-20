package com.onaio.steps.activityHandler;

import android.app.ListActivity;

import com.onaio.steps.activityHandler.Interface.IListItemHandler;
import com.onaio.steps.model.Participant;

/**
 * Created by manisharana on 5/20/15.
 */
public class ParticipantActivityHandler implements IListItemHandler {
    public ParticipantActivityHandler(ListActivity activity, Participant participant) {
    }

    @Override
    public boolean open() {
        return false;
    }
}
