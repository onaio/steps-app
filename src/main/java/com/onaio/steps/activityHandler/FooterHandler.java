package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IPrepare;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

public class FooterHandler implements IPrepare {
    private ListActivity activity;
    private Household household;
    private static final int MENU_ID= R.id.footer;

    public FooterHandler(ListActivity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean shouldInactivate() {
        boolean selected = household.getStatus() == HouseholdStatus.NOT_DONE;
        boolean deferred = household.getStatus() == HouseholdStatus.DEFERRED;
        return !(selected || deferred);
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
