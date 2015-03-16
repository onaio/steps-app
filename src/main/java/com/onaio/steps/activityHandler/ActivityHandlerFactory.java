package com.onaio.steps.activityHandler;

import android.app.ListActivity;

import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

import java.util.ArrayList;
import java.util.List;

public class ActivityHandlerFactory {
    public static List<IActivityHandler> getMainMenuHandlers(ListActivity activity){
        ArrayList<IActivityHandler> handlers = new ArrayList<IActivityHandler>();
        handlers.add(new NewHouseholdActivityHandler(activity));
        handlers.add(new SettingActivityHandler(activity));
        handlers.add(new ExportActivityHandler(activity).withAllHouseholds());
        return handlers;
    }

    public static List<IActivityHandler> getHouseholdMenuHandlers(ListActivity activity, Household household){
        ArrayList<IActivityHandler> handlers = new ArrayList<IActivityHandler>();
        handlers.add(new NewMemberActivityHandler(activity, household));
        handlers.add(new SettingActivityHandler(activity));
        handlers.add(new ExportActivityHandler(activity).withHousehold(household));
        return handlers;
    }

    public static IActivityHandler getHouseholdItemHandler(ListActivity activity, Household household){
        return new HouseholdActivityHandler(activity, household);
    }

    public static IActivityHandler getMemberItemHandler(ListActivity activity, Member member){
        return new MemberActivityHandler(activity, member);
    }
}
