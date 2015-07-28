package com.onaio.steps.handler.factories;

import android.app.ListActivity;
import android.view.Menu;

import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.handler.actions.ExportHandler;
import com.onaio.steps.handler.actions.FinalisedFormHandler;
import com.onaio.steps.handler.activities.HouseholdActivityHandler;
import com.onaio.steps.handler.actions.ImportHandler;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IListItemHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.handler.activities.NewHouseholdActivityHandler;
import com.onaio.steps.handler.activities.SettingActivityHandler;
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
