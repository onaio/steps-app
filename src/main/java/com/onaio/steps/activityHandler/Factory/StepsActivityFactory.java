package com.onaio.steps.activityHandler.Factory;

import android.app.ListActivity;

import com.onaio.steps.activityHandler.ExportHandler;
import com.onaio.steps.activityHandler.HouseholdActivityHandler;
import com.onaio.steps.activityHandler.Interface.IHandler;
import com.onaio.steps.activityHandler.MemberActivityHandler;
import com.onaio.steps.activityHandler.NewHouseholdActivityHandler;
import com.onaio.steps.activityHandler.SettingActivityHandler;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

import java.util.ArrayList;
import java.util.List;

public class StepsActivityFactory {
    public static List<IHandler> getMainMenuHandlers(ListActivity activity){
        ArrayList<IHandler> handlers = new ArrayList<IHandler>();
        handlers.add(new NewHouseholdActivityHandler(activity));
        handlers.add(new SettingActivityHandler(activity));
        handlers.add(new ExportHandler(activity).withAllHouseholds());
        return handlers;
    }

    public static IHandler getMemberItemHandler(ListActivity activity, Member member){
        return new MemberActivityHandler(activity, member);
    }




    public static IHandler getHouseholdItemHandler(ListActivity activity, Household household){
        return new HouseholdActivityHandler(activity, household);
    }
}
