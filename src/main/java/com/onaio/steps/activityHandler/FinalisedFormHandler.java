package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.Dialog;

public class FinalisedFormHandler implements IMenuHandler{
   private ListActivity activity;
    private static final int MENU_ID= R.id.action_saved_form;


    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    public FinalisedFormHandler(ListActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean open() {
        launchODKCollect();
        return true;
    }

    private void launchODKCollect() {
        try {
            Intent surveyIntent = new Intent();
            surveyIntent.setComponent(new ComponentName(Constants.ODK_COLLECT_PACKAGE, Constants.ODK_COLLECT_UPLOADER_CLASS));
            surveyIntent.setAction(Intent.ACTION_EDIT);
            activity.startActivity(surveyIntent);
        } catch (ActivityNotFoundException e) {
            new Dialog().notify(activity, Dialog.EmptyListener, R.string.odk_app_not_installed, R.string.error_title);

        }
    }
}
