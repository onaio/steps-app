package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;

public class HouseholdActivityHandler implements IActivityHandler {

    private Household listViewItem;

    @Override
    public boolean shouldOpen(int menu_id) {
        return true;
    }

    @Override
    public boolean open(ListActivity activity) {
        if (listViewItem == null) return true;
        Intent intent = new Intent(activity.getBaseContext(), HouseholdActivity.class);
        intent.putExtra(Constants.HOUSEHOLD,listViewItem);
        activity.startActivityForResult(intent, Constants.HOUSEHOLD_IDENTIFIER);
        return true;
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == Constants.HOUSEHOLD_IDENTIFIER;
    }

    @Override
    public IActivityHandler with(Object data) {
        listViewItem = ((Household) data);
        return this;
    }

    @Override
    public void handleResult(ListActivity activity, Intent data, int resultCode) {
    }
}
