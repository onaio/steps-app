package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IMenuPreparer;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

public class SelectedParticipantActionsHandler implements IMenuPreparer {
    private ListActivity activity;
    private Household household;
    private static final int MENU_ID= R.id.selected_participant_actions;

    public SelectedParticipantActionsHandler(ListActivity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean shouldInactivate() {
        boolean selected = household.getStatus() == InterviewStatus.NOT_DONE;
        boolean deferred = household.getStatus() == InterviewStatus.DEFERRED;
        boolean incomplete = household.getStatus() == InterviewStatus.INCOMPLETE;
        return !(selected || deferred || incomplete);
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
