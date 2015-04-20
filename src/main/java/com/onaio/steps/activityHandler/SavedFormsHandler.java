package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.helper.Constants;

public class SavedFormsHandler implements IMenuHandler{
   private ListActivity activity;
    private static final int MENU_ID= R.id.action_saved_form;


    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    public SavedFormsHandler(ListActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean open() {
        launchODKCollect();
        return true;
    }

    private void launchODKCollect() {
        Intent surveyIntent = new Intent();
        surveyIntent.setComponent(new ComponentName(Constants.ODK_COLLECT_PACKAGE, Constants.ODK_COLLECT_SAVED_FORMS_CLASS));
        surveyIntent.setAction(Intent.ACTION_EDIT);
        activity.startActivity(surveyIntent);
    }
}
