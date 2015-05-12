package com.onaio.steps.activityHandler.Factory;

import android.app.ListActivity;

import com.onaio.steps.activityHandler.BackHomeHandler;
import com.onaio.steps.activityHandler.CancelHandler;
import com.onaio.steps.activityHandler.DeferredHandler;
import com.onaio.steps.activityHandler.EditHouseholdActivityHandler;
import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IListItemHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuPreparer;
import com.onaio.steps.activityHandler.MemberActivityHandler;
import com.onaio.steps.activityHandler.NewMemberActivityHandler;
import com.onaio.steps.activityHandler.RefusedHandler;
import com.onaio.steps.activityHandler.SelectParticipantHandler;
import com.onaio.steps.activityHandler.SelectedParticipantActionsHandler;
import com.onaio.steps.activityHandler.SelectedParticipantContainerHandler;
import com.onaio.steps.activityHandler.TakeSurveyHandler;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

import java.util.ArrayList;
import java.util.List;

public class HouseholdActivityFactory {

    public static List<IMenuHandler> getMenuHandlers(ListActivity activity, Household household){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new SelectParticipantHandler(activity,household));
        handlers.add(new BackHomeHandler(activity));
        handlers.add(new EditHouseholdActivityHandler(activity,household));
        return handlers;
    }

    public static List<IActivityResultHandler> getResultHandlers(ListActivity activity, Household household){
        ArrayList<IActivityResultHandler> handlers = new ArrayList<IActivityResultHandler>();
        handlers.add(new NewMemberActivityHandler(activity, household));
        handlers.add(new EditHouseholdActivityHandler(activity, household));
        handlers.add(new TakeSurveyHandler(activity,household));
        return handlers;
    }

    public static IListItemHandler getMemberItemHandler(ListActivity activity, Member member){
        return new MemberActivityHandler(activity, member);
    }

    public static List<IMenuPreparer> getCustomMenuPreparer(ListActivity activity, Household household){
        ArrayList<IMenuPreparer> menuItems = new ArrayList<IMenuPreparer>();
        menuItems.add(new TakeSurveyHandler(activity,household));
        menuItems.add(new DeferredHandler(activity, household));
        menuItems.add(new RefusedHandler(activity,household));
        menuItems.add(new SelectedParticipantActionsHandler(activity,household));
        menuItems.add(new NewMemberActivityHandler(activity,household));
        menuItems.add(new SelectParticipantHandler(activity,household));
        menuItems.add(new SelectedParticipantContainerHandler(activity,household));
        menuItems.add(new CancelHandler(activity,household));
        return menuItems;
    }

    public static List<IMenuHandler> getCustomMenuHandler(ListActivity activity, Household household){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new TakeSurveyHandler(activity, household));
        handlers.add(new DeferredHandler(activity,household));
        handlers.add(new RefusedHandler(activity,household));
        handlers.add(new NewMemberActivityHandler(activity,household));
        handlers.add(new SelectParticipantHandler(activity,household));
        handlers.add(new CancelHandler(activity,household));

        return handlers;
    }
}
