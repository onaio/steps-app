package com.onaio.steps.activityHandler;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IHandler;
import com.onaio.steps.activityHandler.Interface.IPrepare;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

public class DeferredHandler implements IHandler,IPrepare {

    private ListActivity activity;
    private Household household;
    private int MENU_ID = R.id.action_deferred;

    public DeferredHandler(ListActivity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        household.setStatus(HouseholdStatus.DEFERRED);
        household.update(new DatabaseHelper(activity.getApplicationContext()));
        notifyUser();
        return true;
    }

    private void notifyUser() {
        new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.survey_deferred_title))
                .setMessage(activity.getString(R.string.survey_deferred_message))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StepsActivityHandler handler = new StepsActivityHandler(activity);
                        handler.open();
                    }
                })
                .create().show();
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
        return !(memberSelected);
    }

    @Override
    public void inactivate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.INVISIBLE);
    }

    @Override
    public void activate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.VISIBLE);
    }
}
