package com.onaio.steps.handler.action;

import android.app.Activity;

import com.onaio.steps.handler.Interface.IMenuHandler;

public class BackHomeHandler implements IMenuHandler {

    private Activity activity;

    public BackHomeHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return android.R.id.home == menu_id;
    }

    @Override
    public boolean open() {
        activity.finish();
        return true;
    }
}
