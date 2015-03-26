package com.onaio.steps.activityHandler.Interface;

import android.app.ListActivity;
import android.content.Intent;

public interface IMenuHandler {
    public boolean shouldOpen(int menu_id);
    public boolean open();
}
