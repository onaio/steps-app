package com.onaio.steps.activityHandler.Factory;


import android.app.Activity;
import android.view.Menu;

import com.onaio.steps.activityHandler.BackHomeHandler;
import com.onaio.steps.activityHandler.DeferredHandler;
import com.onaio.steps.activityHandler.EditParticipantActivityHandler;
import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuPreparer;
import com.onaio.steps.activityHandler.RefusedHandler;
import com.onaio.steps.activityHandler.TakeSurveyHandler;
import com.onaio.steps.model.Participant;

import java.util.ArrayList;
import java.util.List;

public class ParticipantActivityFactory {
    public static List<IMenuHandler> getMenuHandlers(Activity activity, Participant participant) {
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new BackHomeHandler(activity));
        handlers.add(new EditParticipantActivityHandler(activity,participant));
        return handlers;
    }

    public static List<IActivityResultHandler> getResultHandlers(Activity activity, Participant participant){
        ArrayList<IActivityResultHandler> handlers = new ArrayList<IActivityResultHandler>();
        handlers.add(new EditParticipantActivityHandler(activity, participant));
        handlers.add(new TakeSurveyHandler(activity,participant));
        return handlers;
    }

    public static List<IMenuPreparer> getCustomMenuPreparer(Activity activity, Participant participant){
        ArrayList<IMenuPreparer> menuItems = new ArrayList<IMenuPreparer>();
        menuItems.add(new TakeSurveyHandler(activity,participant));
        menuItems.add(new DeferredHandler(activity, participant));
        menuItems.add(new RefusedHandler(activity,participant));
        return menuItems;
    }

    public static List<IMenuHandler> getCustomMenuHandler(Activity activity, Participant participant){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new TakeSurveyHandler(activity, participant));
        handlers.add(new DeferredHandler(activity,participant));
        handlers.add(new RefusedHandler(activity,participant));
        return handlers;
    }

    public static List<IMenuPreparer> getMenuPreparer(Activity activity, Participant participant, Menu menu)
        {
            ArrayList<IMenuPreparer> menuPreparers = new ArrayList<IMenuPreparer>();
            menuPreparers.add(new EditParticipantActivityHandler(activity, participant).withMenu(menu));
            return menuPreparers;
        }
}
