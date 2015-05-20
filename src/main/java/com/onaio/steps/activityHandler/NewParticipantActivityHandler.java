package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;

public class NewParticipantActivityHandler implements IActivityResultHandler,IMenuHandler {

    public NewParticipantActivityHandler(ListActivity activity) {
    }


    @Override
    public void handleResult(Intent data, int resultCode) {

    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return false;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return false;
    }

    @Override
    public boolean open() {
        return false;
    }
}
