package com.onaio.steps.activityHandler;

import java.util.ArrayList;
import java.util.List;

public class ActivityHandlerFactory {
    public static List<IActivityHandler> getHandlers(){
        ArrayList<IActivityHandler> handlers = new ArrayList<IActivityHandler>();
        handlers.add(new NewHouseholdActivityHandler());
        handlers.add(new SettingActivityHandler());
        return handlers;
    }
}
