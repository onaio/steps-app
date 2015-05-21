package com.onaio.steps.activityHandler.Factory;

import android.app.ListActivity;
import android.view.Menu;

import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.activityHandler.ExportHandler;
import com.onaio.steps.activityHandler.FinalisedFormHandler;
import com.onaio.steps.activityHandler.HouseholdActivityHandler;
import com.onaio.steps.activityHandler.ImportHandler;
import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IListItemHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuPreparer;
import com.onaio.steps.activityHandler.NewHouseholdActivityHandler;
import com.onaio.steps.activityHandler.SettingActivityHandler;
import com.onaio.steps.model.Household;

import java.util.ArrayList;
import java.util.List;

public class HouseholdListActivityFactory {
    public static List<IMenuHandler> getMenuHandlers(ListActivity activity, List<Household> households){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new SettingActivityHandler(activity).prepareFor(FlowType.Household));
        handlers.add(new ExportHandler(activity).with(households));
        handlers.add(new ImportHandler(activity));
        handlers.add(new FinalisedFormHandler(activity));
        return handlers;
    }


    public static List<IActivityResultHandler> getResultHandlers(ListActivity activity){
        ArrayList<IActivityResultHandler> handlers = new ArrayList<IActivityResultHandler>();
        handlers.add(new NewHouseholdActivityHandler(activity));
        handlers.add(new ImportHandler(activity));
        handlers.add(new SettingActivityHandler(activity));
        return handlers;
    }

    public static IListItemHandler getHouseholdItemHandler(ListActivity activity, Household household){
        return new HouseholdActivityHandler(activity, household);
    }

    public static List<IMenuHandler> getCustomMenuHandler(ListActivity activity){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new NewHouseholdActivityHandler(activity));
        return handlers;
    }

    public static List<IMenuPreparer> getMenuPreparer(ListActivity activity, List<Household> households, Menu menu) {
        ArrayList<IMenuPreparer> handlers = new ArrayList<IMenuPreparer>();
        handlers.add(new ExportHandler(activity).with(households).withMenu(menu));
        return handlers;
    }
}
