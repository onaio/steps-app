package com.onaio.steps.activityHandler;

import android.view.Menu;

import com.onaio.steps.model.Household;

public interface IPrepare {
    public boolean shouldDisable(Household household);
    public void disable(Menu menu);
}
