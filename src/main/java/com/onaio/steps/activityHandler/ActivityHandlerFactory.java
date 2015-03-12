package com.onaio.steps.activityHandler;

import java.util.ArrayList;
import java.util.List;

public class ActivityHandlerFactory {
    public static List<IActivityHandler> getMainMenuHandlers(){
        ArrayList<IActivityHandler> handlers = new ArrayList<IActivityHandler>();
        handlers.add(new NewHouseholdActivityHandler());
        handlers.add(new SettingActivityHandler());
        return handlers;
    }

    public static List<IActivityHandler> getHouseholdMenuHandlers(){
        ArrayList<IActivityHandler> handlers = new ArrayList<IActivityHandler>();
        handlers.add(new NewMemberActivityHandler());
        handlers.add(new SettingActivityHandler());
        return handlers;
    }

    public static IActivityHandler getHouseholdListItemHandler(){
        return new HouseholdActivityHandler();
    }
}
