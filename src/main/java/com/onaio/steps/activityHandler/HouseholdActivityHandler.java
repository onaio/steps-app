package com.onaio.steps.activityHandler;

import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.activityHandler.Interface.IItemHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;

public class HouseholdActivityHandler implements IMenuHandler {

    private Activity activity;
    private Household household;

    public HouseholdActivityHandler(Activity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == android.R.id.home;
    }

    @Override
    public boolean open() {
        if (household == null) return true;
        Intent intent = new Intent(activity, HouseholdActivity.class);
        intent.putExtra(Constants.HOUSEHOLD, household);
        activity.startActivity(intent);
        activity.finish();
        return true;
    }
}
