package com.onaio.steps.handler.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.onaio.steps.R;
import com.onaio.steps.activities.HouseholdListActivity;
import com.onaio.steps.activities.MainActivityOrchestrator;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.orchestrators.flows.ParticipantFlow;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 04/06/2018.
 */

public class ParticipantActivityMenuItemHandler implements IMenuHandler {

    private static final int MENU_ID= R.id.action_open_participants;
    private HouseholdListActivity activity;

    public ParticipantActivityMenuItemHandler(@NonNull Activity activity) {
        if (activity instanceof HouseholdListActivity) {
            this.activity = (HouseholdListActivity) activity;
        }
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return MENU_ID == menu_id;
    }

    @Override
    public boolean open() {
        ParticipantFlow participantFlow = new ParticipantFlow(activity);
        participantFlow.saveSafely(activity, Constants.FLOW_TYPE, FlowType.Participant.toString());

        activity.startActivity(new Intent(activity, MainActivityOrchestrator.class));
        activity.finish();

        return true;
    }
}
