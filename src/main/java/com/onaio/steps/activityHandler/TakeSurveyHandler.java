package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IHandler;
import com.onaio.steps.activityHandler.Interface.IPrepare;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

public class TakeSurveyHandler implements IHandler, IPrepare {
    private ListActivity activity;
    private Household household;
    private static final int MENU_ID= R.id.action_take_survey;

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    public TakeSurveyHandler(ListActivity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean open() {
        PackageManager packageManager = activity.getPackageManager();
//        Intent surveyIntent = packageManager.getLaunchIntentForPackage("org.odk.collect.android");
//        Uri uri = Uri.parse("vnd.android.cursor.item/vnd.odk.form");
//        Intent surveyIntent = new Intent(Intent.ACTION_EDIT, uri);
//        surveyIntent.setClassName("org.odk.collect.android", "org.odk.collect.android..activities.FormEntryActivity");
        Intent surveyIntent = packageManager.getLaunchIntentForPackage("org.odk.collect.android");
        surveyIntent.addCategory(Intent.ACTION_EDIT);
//        surveyIntent.setClassName("org.odk.collect.android","org.odk.collect.android.activities.FormEntryActivity");
//        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(surveyIntent, 0);


        activity.startActivity(surveyIntent);
        return true;
    }

    @Override
    public void handleResult(Intent data, int resultCode) {

    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return false;
    }

    @Override
    public boolean shouldInactivate() {
        boolean selected = household.getStatus() == HouseholdStatus.SELECTED;
        boolean deferred = household.getStatus() == HouseholdStatus.DEFERRED;
        return !(selected || deferred);
    }

    @Override
    public void inactivate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.INVISIBLE);
    }
}
