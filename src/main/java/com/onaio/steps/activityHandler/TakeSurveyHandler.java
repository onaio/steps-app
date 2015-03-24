package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IHandler;
import com.onaio.steps.activityHandler.Interface.IPrepare;
import com.onaio.steps.exception.AppNotInstalledException;
import com.onaio.steps.exception.FormNotPresentException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.Dialog;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.ODKForm;

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
//        Intent surveyIntent = packageManager.getLaunchIntentForPackage("org.odk.collect.android");
//        Uri uri = Uri.parse("vnd.android.cursor.item/vnd.odk.form");
//        Intent surveyIntent = new Intent(Intent.ACTION_EDIT, uri);
//        surveyIntent.setClassName("org.odk.collect.android", "org.odk.collect.android..activities.FormEntryActivity");
//        surveyIntent.setClassName("org.odk.collect.android","org.odk.collect.android.activities.FormEntryActivity");
//        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(surveyIntent, 0);

//        PackageManager packageManager = activity.getPackageManager();
//        Intent surveyIntent = packageManager.getLaunchIntentForPackage("org.odk.collect.android");
//        surveyIntent.addCategory(Intent.ACTION_EDIT);

        Intent surveyIntent = new Intent();
        surveyIntent.setComponent(new ComponentName("org.odk.collect.android","org.odk.collect.android.activities.FormEntryActivity"));
        ODKForm requiredForm = null;
        try {
            requiredForm = ODKForm.getFrom(activity, Constants.ODK_FORM_ID);
            surveyIntent.setAction(Intent.ACTION_EDIT);
            surveyIntent.setData(requiredForm.getUri());


            activity.startActivity(surveyIntent);
            household.setStatus(HouseholdStatus.CLOSED);
            household.update(new DatabaseHelper(activity));
        } catch (FormNotPresentException e) {
            Dialog.notify(activity,Dialog.EmptyListener,R.string.form_not_present, R.string.form_not_present_title);
        } catch (AppNotInstalledException e) {
            Dialog.notify(activity,Dialog.EmptyListener,R.string.odk_app_not_installed, R.string.participant_no_re_elect_title);
        }

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

    @Override
    public void activate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.VISIBLE);
    }
}
