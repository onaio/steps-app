package com.onaio.steps.activityHandler.Factory;

import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;

import com.onaio.steps.activityHandler.DeleteMemberHandler;
import com.onaio.steps.activityHandler.ExportHandler;
import com.onaio.steps.activityHandler.HouseholdActivityHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IPrepare;
import com.onaio.steps.activityHandler.MemberActivityHandler;
import com.onaio.steps.activityHandler.NewHouseholdActivityHandler;
import com.onaio.steps.activityHandler.SelectParticipantHandler;
import com.onaio.steps.activityHandler.SettingActivityHandler;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberActivityFactory {
    public static List<IMenuHandler> getMenuHandlers(Activity activity, Member member){
        ArrayList<IMenuHandler> handlers = new ArrayList<IMenuHandler>();
        handlers.add(new DeleteMemberHandler(activity,member));
        return handlers;
    }

    public static IPrepare getMenuPreparer(Activity activity, Member member, Menu menu){
        return new DeleteMemberHandler(activity, member).withMenu(menu);
    }
}
