package com.onaio.steps.activityHandler;

import android.app.Activity;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IMenuPreparer;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

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
        InterviewStatus status = household.getStatus();
        boolean notDone = status.equals(InterviewStatus.NOT_DONE);
        boolean deferred = status.equals(InterviewStatus.DEFERRED);
        boolean incomplete = status.equals(InterviewStatus.INCOMPLETE);
        return !notDone && !deferred && !incomplete;
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
