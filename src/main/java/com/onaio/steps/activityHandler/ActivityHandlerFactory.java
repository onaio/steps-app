package com.onaio.steps.activityHandler;

import java.util.ArrayList;
import java.util.List;

public class ActivityHandlerFactory {
    public static List<IActivityHandler> getMenuHandlers(){
        ArrayList<IActivityHandler> handlers = new ArrayList<IActivityHandler>();
        handlers.add(new NewHouseholdActivityHandler());
        handlers.add(new SettingActivityHandler());
        return handlers;
    }

    public static IActivityHandler getHouseholdListItemHandler(){
        return new HouseholdActivityHandler();
    }
}
