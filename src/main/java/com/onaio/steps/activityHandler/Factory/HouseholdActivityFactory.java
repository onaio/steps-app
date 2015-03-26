package com.onaio.steps.activityHandler.Factory;

import android.app.ListActivity;
import android.view.Menu;

import com.onaio.steps.activityHandler.DeferredHandler;
import com.onaio.steps.activityHandler.ExportHandler;
import com.onaio.steps.activityHandler.FooterHandler;
import com.onaio.steps.activityHandler.Interface.IItemHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuResultHandler;
import com.onaio.steps.activityHandler.Interface.IPrepare;
import com.onaio.steps.activityHandler.MemberActivityHandler;
import com.onaio.steps.activityHandler.NewMemberActivityHandler;
import com.onaio.steps.activityHandler.RefusedHandler;
import com.onaio.steps.activityHandler.SelectParticipantHandler;
import com.onaio.steps.activityHandler.StepsActivityHandler;
import com.onaio.steps.activityHandler.TakeSurveyHandler;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

import java.util.ArrayList;
import java.util.List;

public class HouseholdActivityFactory {

    public static List<IMenuHandler> getMenuHandlers(ListActivity activity, Household household){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new NewMemberActivityHandler(activity, household));
        handlers.add(new ExportHandler(activity).withHousehold(household));
        handlers.add(new SelectParticipantHandler(activity,household));
        handlers.add(new StepsActivityHandler(activity));
        return handlers;
    }

    public static List<IMenuResultHandler> getMenuResultHandlers(ListActivity activity, Household household){
        ArrayList<IMenuResultHandler> handlers = new ArrayList<IMenuResultHandler>();
        handlers.add(new NewMemberActivityHandler(activity, household));
        return handlers;
    }

    public static IItemHandler getMemberItemHandler(ListActivity activity, Member member){
        return new MemberActivityHandler(activity, member);
    }

    public static IPrepare getMenuPreparer(ListActivity activity, Household household, Menu menu){
        return new SelectParticipantHandler(activity, household).withMenu(menu);
    }

    public static List<IPrepare> getBottomMenuPreparer(ListActivity activity, Household household){
        ArrayList<IPrepare> menuItems = new ArrayList<IPrepare>();
        menuItems.add(new TakeSurveyHandler(activity,household));
        menuItems.add(new DeferredHandler(activity, household));
        menuItems.add(new RefusedHandler(activity,household));
        menuItems.add(new FooterHandler(activity,household));
        return menuItems;
    }

    public static List<IMenuHandler> getBottomMenuHandler(ListActivity activity, Household household){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new TakeSurveyHandler(activity, household));
        handlers.add(new DeferredHandler(activity,household));
        handlers.add(new RefusedHandler(activity,household));
        return handlers;
    }
}
