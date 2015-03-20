package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IHandler;
import com.onaio.steps.activityHandler.Interface.IPrepare;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

public class RefusedHandler implements IHandler,IPrepare {

    private Household household;
    private ListActivity activity;
    private int MENU_ID = R.id.action_refused;

    public RefusedHandler(ListActivity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id==MENU_ID;
    }

    @Override
    public boolean open() {
        household.setStatus(HouseholdStatus.REFUSED);
        household.update(new DatabaseHelper(activity.getApplicationContext()));
        StepsActivityHandler handler = new StepsActivityHandler(activity);
        handler.open();
        return true;
    }

    @Override
    public void handleResult(Intent data, int resultCode) {

    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return false;
    }

    @Override
    public boolean shouldInactivate() {
        boolean memberSelected = household.getStatus() == HouseholdStatus.SELECTED;
        boolean memberDeferred = household.getStatus() == HouseholdStatus.DEFERRED;
        return !(memberSelected || memberDeferred);
    }

    @Override
    public void inactivate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.INVISIBLE);
    }
}
