package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IPrepare;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.Dialog;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

public class DeferredHandler implements IMenuHandler,IPrepare {

    private final Dialog dialog;
    private ListActivity activity;
    private Household household;
    private int MENU_ID = R.id.action_deferred;

    public DeferredHandler(ListActivity activity, Household household) {
        this(activity,household,new Dialog());
    }

    public DeferredHandler(ListActivity activity, Household household, Dialog dialog) {
        this.activity = activity;
        this.household = household;
        this.dialog = dialog;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        household.setStatus(HouseholdStatus.DEFERRED);
        household.update(new DatabaseHelper(activity.getApplicationContext()));
        dialog.notify(activity, Dialog.EmptyListener, R.string.survey_deferred_message, R.string.survey_deferred_title);
        activity.finish();
        new StepsActivityHandler(activity).open();
        return true;
    }

    @Override
    public boolean shouldInactivate() {
        boolean memberSelected = household.getStatus() == HouseholdStatus.NOT_DONE;
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
