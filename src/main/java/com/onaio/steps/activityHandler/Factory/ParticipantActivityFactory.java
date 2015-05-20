package com.onaio.steps.activityHandler.Factory;


import android.app.ListActivity;

import com.onaio.steps.activityHandler.FinalisedFormHandler;
import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IListItemHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.NewParticipantActivityHandler;
import com.onaio.steps.activityHandler.ParticipantActivityHandler;
import com.onaio.steps.activityHandler.SettingActivityHandler;
import com.onaio.steps.model.Participant;

import java.util.ArrayList;
import java.util.List;

public class ParticipantActivityFactory {
    public static List<IMenuHandler> getMenuHandlers(ListActivity activity) {

        ArrayList<IMenuHandler> menuHandlers = new ArrayList<IMenuHandler>();
        menuHandlers.add(new SettingActivityHandler(activity));
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
