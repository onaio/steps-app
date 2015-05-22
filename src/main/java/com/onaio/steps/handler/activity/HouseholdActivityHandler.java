package com.onaio.steps.handler.activity;

import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.handler.Interface.IListItemHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;

public class HouseholdActivityHandler implements IListItemHandler {

    private Activity activity;
    private Household household;

    public HouseholdActivityHandler(Activity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean open() {
        if (household == null) return true;
        Intent intent = new Intent(activity, HouseholdActivity.class);
        intent.putExtra(Constants.HOUSEHOLD, household);
        activity.startActivity(intent);
        return true;
    }
}
