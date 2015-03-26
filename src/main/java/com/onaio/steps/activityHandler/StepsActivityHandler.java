package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.activity.StepsActivity;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.helper.Constants;

public class StepsActivityHandler implements IMenuHandler {

    private ListActivity activity;

    public StepsActivityHandler(ListActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return android.R.id.home == menu_id;
    }

    @Override
    public boolean open() {
        Intent intent = new Intent(activity.getBaseContext(), StepsActivity.class);
        activity.startActivityForResult(intent, Constants.STEPS_IDENTIFIER);
        return true;
    }
}
