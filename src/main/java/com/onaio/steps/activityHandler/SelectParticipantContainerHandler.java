package com.onaio.steps.activityHandler;

import android.app.Activity;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IPrepare;
import com.onaio.steps.model.Household;

public class SelectParticipantContainerHandler implements IPrepare{
    private final int MENU_ID = R.id.selected_participant;
    private Activity activity;
    private Household household;

    public SelectParticipantContainerHandler(Activity activity,Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean shouldInactivate() {
        return household.getSelectedMemberId() == null;
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
