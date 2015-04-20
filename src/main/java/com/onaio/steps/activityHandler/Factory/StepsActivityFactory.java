package com.onaio.steps.activityHandler.Factory;

import android.app.ListActivity;

import com.onaio.steps.activityHandler.ExportHandler;
import com.onaio.steps.activityHandler.HouseholdActivityHandler;
import com.onaio.steps.activityHandler.ImportHandler;
import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IListItemHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.NewHouseholdActivityHandler;
import com.onaio.steps.activityHandler.SavedFormsHandler;
import com.onaio.steps.activityHandler.SettingActivityHandler;
import com.onaio.steps.model.Household;

import java.util.ArrayList;
import java.util.List;

public class StepsActivityFactory {
    public static List<IMenuHandler> getMenuHandlers(ListActivity activity, List<Household> households){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new SettingActivityHandler(activity));
        handlers.add(new ExportHandler(activity).with(households));
        handlers.add(new ImportHandler(activity));
        handlers.add(new SavedFormsHandler(activity));
        return handlers;
    }

    public static List<IActivityResultHandler> getResultHandlers(ListActivity activity){
        ArrayList<IActivityResultHandler> handlers = new ArrayList<IActivityResultHandler>();
        handlers.add(new NewHouseholdActivityHandler(activity));
        handlers.add(new SettingActivityHandler(activity));
        handlers.add(new ImportHandler(activity));
        return handlers;
    }

    public static IListItemHandler getHouseholdItemHandler(ListActivity activity, Household household){
        return new HouseholdActivityHandler(activity, household);
    }

    public static List<IMenuHandler> getCustomMenuHandler(ListActivity activity){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new NewHouseholdActivityHandler(activity));
        handlers.add(new SettingActivityHandler(activity));
        return handlers;
    }
}
