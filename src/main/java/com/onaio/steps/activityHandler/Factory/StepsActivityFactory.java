package com.onaio.steps.activityHandler.Factory;

import android.app.ListActivity;

import com.onaio.steps.activityHandler.ExportHandler;
import com.onaio.steps.activityHandler.HouseholdActivityHandler;
import com.onaio.steps.activityHandler.Interface.IItemHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuResultHandler;
import com.onaio.steps.activityHandler.NewHouseholdActivityHandler;
import com.onaio.steps.activityHandler.SettingActivityHandler;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;

import java.util.ArrayList;
import java.util.List;

public class StepsActivityFactory {
    public static List<IMenuHandler> getMenuHandlers(ListActivity activity){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new NewHouseholdActivityHandler(activity));
        handlers.add(new SettingActivityHandler(activity));
        List<Household> households = Household.getAll(new DatabaseHelper(activity));
        handlers.add(new ExportHandler(activity).with(households));
        return handlers;
    }

    public static List<IMenuResultHandler> getMenuResultsHandlers(ListActivity activity){
        ArrayList<IMenuResultHandler> handlers = new ArrayList<IMenuResultHandler>();
        handlers.add(new NewHouseholdActivityHandler(activity));
        handlers.add(new SettingActivityHandler(activity));
        return handlers;
    }

    public static IItemHandler getHouseholdItemHandler(ListActivity activity, Household household){
        return new HouseholdActivityHandler(activity, household);
    }
}
