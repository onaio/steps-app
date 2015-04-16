package com.onaio.steps.activityHandler.Factory;

import android.app.Activity;
import android.view.Menu;

import com.onaio.steps.activityHandler.BackHomeHandler;
import com.onaio.steps.activityHandler.DeleteMemberHandler;
import com.onaio.steps.activityHandler.EditMemberActivityHandler;
import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuPreparer;
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

    public static List<IMenuPreparer> getMenuPreparer(Activity activity, Member member, Menu menu){
        ArrayList<IMenuPreparer> handlers = new ArrayList<IMenuPreparer>();
        handlers.add(new DeleteMemberHandler(activity, member).withMenu(menu));
        handlers.add(new EditMemberActivityHandler(activity,member).withMenu(menu));
        return handlers;
    }

    public static List<IActivityResultHandler> getMenuResultHandlers(Activity activity, Member member) {
        ArrayList<IActivityResultHandler> handlers = new ArrayList<IActivityResultHandler>();
        handlers.add(new EditMemberActivityHandler(activity,member));
        return handlers;
    }
}
