package com.onaio.steps.handler.activities;

import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activities.MainActivityOrchestrator;
import com.onaio.steps.activities.ParticipantListActivity;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.IncompleteSettingsActivitySwitchDialog;
import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.orchestrators.flows.HouseholdFlow;

import java.util.List;

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

        List<String> errors = householdFlow.validateHouseHoldSettings(
                householdFlow.getValue(activity, Constants.HH_SURVEY_ID),
                householdFlow.getValue(activity, Constants.HH_PHONE_ID),
                householdFlow.getValue(activity, Constants.HH_FORM_ID),
                householdFlow.getValue(activity, Constants.HH_MIN_AGE),
                householdFlow.getValue(activity, Constants.HH_MAX_AGE)
        );

        if (errors != null && !errors.isEmpty()) {
            IncompleteSettingsActivitySwitchDialog dialog = new IncompleteSettingsActivitySwitchDialog(activity);
            dialog.show();
        } else {
            householdFlow.saveSafely(activity, Constants.FLOW_TYPE, FlowType.Household.toString());
            activity.startActivity(new Intent(activity, MainActivityOrchestrator.class));
            activity.finish();
        }

        return true;
    }
}
