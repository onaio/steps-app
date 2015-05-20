package com.onaio.steps.activityHandler.Factory;

import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;

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

public class WelcomeActivityFactory {


    public static List<IActivityResultHandler> getResultHandlers(Activity activity){
        ArrayList<IActivityResultHandler> handlers = new ArrayList<IActivityResultHandler>();
        handlers.add(new SettingActivityHandler(activity));
        return handlers;
    }

    public static List<IMenuHandler> getCustomMenuHandler(Activity activity){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new SettingActivityHandler(activity));
        return handlers;
    }
}
