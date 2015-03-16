package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.model.Household;

public class SelectParticipantHandler implements IHandler{

    private ListActivity activity;
    private Household household;

    public SelectParticipantHandler(ListActivity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.action_select_participant;
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
