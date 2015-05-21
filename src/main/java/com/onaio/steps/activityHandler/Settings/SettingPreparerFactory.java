package com.onaio.steps.activityHandler.Settings;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class SettingPreparerFactory {
    public static List<ISettingPreparer> getPreparers(Activity activity){
        List<ISettingPreparer> settingPreparers = new ArrayList<ISettingPreparer>();
        settingPreparers.add(new DefaultSettingPreparer(activity));
        settingPreparers.add(new ParticipantSettingPreparer(activity));
        settingPreparers.add(new HouseholdSettingPreparer(activity));
        return settingPreparers;
    }
}
