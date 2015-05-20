package com.onaio.steps.activityHandler;

import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.model.Participant;

/**
 * Created by manisharana on 5/20/15.
 */
public class EditParticipantActivityHandler implements IMenuHandler ,IActivityResultHandler {
    public EditParticipantActivityHandler(Activity activity, Participant participant) {
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return false;
    }

    @Override
    public boolean open() {
        return false;
    }

    @Override
    public void handleResult(Intent data, int resultCode) {

    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return false;
    }
}
