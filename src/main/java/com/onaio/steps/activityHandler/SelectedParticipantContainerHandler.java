package com.onaio.steps.activityHandler;

import android.app.Activity;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IMenuPreparer;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

public class SelectedParticipantContainerHandler implements IMenuPreparer {
    private final int MENU_ID = R.id.selected_participant;
    private Activity activity;
    private Household household;

    public SelectedParticipantContainerHandler(Activity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean shouldInactivate() {
        HouseholdStatus status = household.getStatus();
        boolean notDone = status.equals(HouseholdStatus.NOT_DONE);
        boolean deferred = status.equals(HouseholdStatus.DEFERRED);
        return !notDone && !deferred;
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
