package com.onaio.steps.handler.factories;

import android.app.ListActivity;

import com.onaio.steps.handler.actions.BackHomeHandler;
import com.onaio.steps.handler.actions.CancelParticipantSelectionHandler;
import com.onaio.steps.handler.actions.DeferredHandler;
import com.onaio.steps.handler.activities.EditHouseholdActivityHandler;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IListItemHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.handler.activities.MemberActivityHandler;
import com.onaio.steps.handler.activities.NewMemberActivityHandler;
import com.onaio.steps.handler.actions.RefusedHandler;
import com.onaio.steps.handler.actions.SelectParticipantHandler;
import com.onaio.steps.handler.actions.SelectedParticipantActionsHandler;
import com.onaio.steps.handler.SelectedParticipantContainerHandler;
import com.onaio.steps.handler.actions.TakeSurveyHandler;
import com.onaio.steps.handler.strategies.survey.DeferSurveyForHouseholdStrategy;
import com.onaio.steps.handler.strategies.survey.RefuseSurveyForHouseholdStrategy;
import com.onaio.steps.handler.strategies.survey.TakeSurveyForHouseholdStrategy;
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
        handlers.add(new TakeSurveyHandler(activity,new TakeSurveyForHouseholdStrategy(household,activity)));
        return handlers;
    }

    public static IListItemHandler getMemberItemHandler(ListActivity activity, Member member){
        return new MemberActivityHandler(activity, member);
    }

    public static List<IMenuPreparer> getCustomMenuPreparer(ListActivity activity, Household household){
        ArrayList<IMenuPreparer> menuItems = new ArrayList<IMenuPreparer>();
        menuItems.add(new TakeSurveyHandler(activity,new TakeSurveyForHouseholdStrategy(household,activity)));
        menuItems.add(new DeferredHandler(activity, new DeferSurveyForHouseholdStrategy(household,activity)));
        menuItems.add(new RefusedHandler(activity,new RefuseSurveyForHouseholdStrategy(household,activity)));
        menuItems.add(new SelectedParticipantActionsHandler(activity,household));
        menuItems.add(new NewMemberActivityHandler(activity,household));
        menuItems.add(new SelectParticipantHandler(activity,household));
        menuItems.add(new SelectedParticipantContainerHandler(activity,household));
        menuItems.add(new CancelParticipantSelectionHandler(activity,household));
        return menuItems;
    }

    public static List<IMenuHandler> getCustomMenuHandler(ListActivity activity, Household household){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new TakeSurveyHandler(activity, new TakeSurveyForHouseholdStrategy(household,activity)));
        handlers.add(new DeferredHandler(activity, new DeferSurveyForHouseholdStrategy(household,activity)));
        handlers.add(new RefusedHandler(activity,new RefuseSurveyForHouseholdStrategy(household,activity)));
        handlers.add(new NewMemberActivityHandler(activity,household));
        handlers.add(new SelectParticipantHandler(activity,household));
        handlers.add(new CancelParticipantSelectionHandler(activity,household));

        return handlers;
    }
}
