package com.onaio.steps.handler.activities;

import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activities.MainActivityOrchestrator;
import com.onaio.steps.activities.ParticipantListActivity;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.orchestrators.flows.HouseholdFlow;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 04/06/2018.
 */

public class HouseHoldActivityMenuItemHandler implements IMenuHandler {

    private static final int MENU_ID= R.id.action_open_households;
    private ParticipantListActivity activity;

    public HouseHoldActivityMenuItemHandler(Activity activity) {
        if (activity instanceof ParticipantListActivity) {
            this.activity = (ParticipantListActivity) activity;
        }
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return MENU_ID == menu_id;
    }

    @Override
    public boolean open() {
        HouseholdFlow householdFlow = new HouseholdFlow(activity);
        householdFlow.saveSafely(activity, Constants.FLOW_TYPE, FlowType.Household.toString());
        activity.startActivity(new Intent(activity, MainActivityOrchestrator.class));
        activity.finish();

        return true;
    }
}
