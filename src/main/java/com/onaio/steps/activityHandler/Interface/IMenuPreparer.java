package com.onaio.steps.activityHandler.Interface;

import android.view.Menu;

import com.onaio.steps.model.Household;

public interface IMenuPreparer {
    public boolean shouldInactivate();
    public void inactivate();
    public void activate();
}
