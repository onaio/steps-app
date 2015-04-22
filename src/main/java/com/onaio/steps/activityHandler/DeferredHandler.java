package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuPreparer;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.Dialog;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

public class DeferredHandler implements IMenuHandler,IMenuPreparer {

    private final Dialog dialog;
    private ListActivity activity;
    private Household household;
    private int MENU_ID = R.id.action_deferred;

    public DeferredHandler(ListActivity activity, Household household) {
        this(activity,household,new Dialog());
    }

    DeferredHandler(ListActivity activity, Household household, Dialog dialog) {
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
        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new BackHomeHandler(activity).open();
            }
        };
        dialog.notify(activity, confirmListener, R.string.survey_deferred_title, R.string.survey_deferred_message);
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
        item.setVisibility(View.GONE);
    }

    @Override
    public void activate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.VISIBLE);
    }
}
