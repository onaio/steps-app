package com.onaio.steps.activityHandler;

import android.app.ListActivity;

import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

import java.util.ArrayList;
import java.util.List;

public class ActivityHandlerFactory {
    public static List<IHandler> getMainMenuHandlers(ListActivity activity){
        ArrayList<IHandler> handlers = new ArrayList<IHandler>();
        handlers.add(new NewHouseholdActivityHandler(activity));
        handlers.add(new SettingActivityHandler(activity));
        handlers.add(new ExportHandler(activity).withAllHouseholds());
        return handlers;
    }

    public static List<IHandler> getHouseholdMenuHandlers(ListActivity activity, Household household){
        ArrayList<IHandler> handlers = new ArrayList<IHandler>();
        handlers.add(new NewMemberActivityHandler(activity, household));
        handlers.add(new SettingActivityHandler(activity));
        handlers.add(new ExportHandler(activity).withHousehold(household));
        handlers.add(new SelectParticipantHandler(activity,household));
        handlers.add(new TakeSurveyHandler(activity));
        handlers.add(new StepsActivityHandler(activity));
        return handlers;
    }

    public static IHandler getHouseholdItemHandler(ListActivity activity, Household household){
        return new HouseholdActivityHandler(activity, household);
    }

    public static IHandler getMemberItemHandler(ListActivity activity, Member member){
        return new MemberActivityHandler(activity, member);
    }

    public static List<IPrepare> getHouseholdMenuItemToPrepare(ListActivity activity){
        ArrayList<IPrepare> menuItems = new ArrayList<IPrepare>();
        menuItems.add(new TakeSurveyHandler(activity));
        return menuItems;
    }
}
