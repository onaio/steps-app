package com.onaio.steps.activityHandler.Factory;

import android.app.Activity;
import android.view.Menu;

import com.onaio.steps.activityHandler.BackHomeHandler;
import com.onaio.steps.activityHandler.DeleteMemberHandler;
import com.onaio.steps.activityHandler.EditMemberActivityHandler;
import com.onaio.steps.activityHandler.HouseholdActivityHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IResultHandler;
import com.onaio.steps.activityHandler.Interface.IPrepare;
import com.onaio.steps.model.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberActivityFactory {
    public static List<IMenuHandler> getMenuHandlers(Activity activity, Member member){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new DeleteMemberHandler(activity,member));
        handlers.add(new EditMemberActivityHandler(activity,member));
        handlers.add(new BackHomeHandler(activity));
        return handlers;
    }

    public static List<IPrepare> getMenuPreparer(Activity activity, Member member, Menu menu){
        ArrayList<IPrepare> handlers = new ArrayList<IPrepare>();
        handlers.add(new DeleteMemberHandler(activity, member).withMenu(menu));
        handlers.add(new EditMemberActivityHandler(activity,member).withMenu(menu));
        return handlers;
    }

    public static List<IResultHandler> getMenuResultHandlers(Activity activity, Member member) {
        ArrayList<IResultHandler> handlers = new ArrayList<IResultHandler>();
        handlers.add(new EditMemberActivityHandler(activity,member));
        return handlers;
    }
}
