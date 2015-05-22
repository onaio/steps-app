package com.onaio.steps.handler.factories;


import android.app.ListActivity;

import com.onaio.steps.handler.actions.FinalisedFormHandler;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IListItemHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.activities.NewParticipantActivityHandler;
import com.onaio.steps.handler.activities.ParticipantActivityHandler;
import com.onaio.steps.handler.activities.SettingActivityHandler;
import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.model.Participant;

import java.util.ArrayList;
import java.util.List;

public class ParticipantListActivityFactory {
    public static List<IMenuHandler> getMenuHandlers(ListActivity activity) {

        ArrayList<IMenuHandler> menuHandlers = new ArrayList<IMenuHandler>();
        menuHandlers.add(new SettingActivityHandler(activity).prepareFor(FlowType.Participant));
        menuHandlers.add(new FinalisedFormHandler(activity));
        return menuHandlers;
    }

    public static List<IActivityResultHandler> getResultHandlers(ListActivity activity) {
        ArrayList<IActivityResultHandler> resultHandlers = new ArrayList<IActivityResultHandler>();
        resultHandlers.add(new NewParticipantActivityHandler(activity));
        resultHandlers.add(new SettingActivityHandler(activity));
        return resultHandlers;
    }

    public static IListItemHandler getParticipantItemHandler(ListActivity activity, Participant participant) {
        return new ParticipantActivityHandler(activity,participant);

    }

    public static List<IMenuHandler> getCustomMenuHandler(ListActivity activity) {
        ArrayList<IMenuHandler> menuHandlers = new ArrayList<IMenuHandler>();
        menuHandlers.add(new NewParticipantActivityHandler(activity));
        menuHandlers.add(new SettingActivityHandler(activity));
        return menuHandlers;
    }
}
