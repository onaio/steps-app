package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;

public class HouseholdActivityHandler implements IActivityHandler {

    private ListActivity activity;
    private Household household;

    public HouseholdActivityHandler(ListActivity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return true;
    }

    @Override
    public boolean open() {
        if (household == null) return true;
        Intent intent = new Intent(activity.getBaseContext(), HouseholdActivity.class);
        intent.putExtra(Constants.HOUSEHOLD, household);
        activity.startActivityForResult(intent, Constants.HOUSEHOLD_IDENTIFIER);
        return true;
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == Constants.HOUSEHOLD_IDENTIFIER;
    }

    @Override
    public void handleResult(Intent data, int resultCode) {
    }
}
