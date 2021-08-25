package com.onaio.steps.handler.activities;

import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activities.HouseholdSummaryActivity;
import com.onaio.steps.handler.interfaces.IMenuHandler;

public class ViewHouseholdSummaryActivityMenuHandler implements IMenuHandler {

    private final ListActivity activity;

    public ViewHouseholdSummaryActivityMenuHandler(ListActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.action_view_totals;
    }

    @Override
    public boolean open() {
        activity.startActivity(new Intent(activity, HouseholdSummaryActivity.class));
        return true;
    }
}
