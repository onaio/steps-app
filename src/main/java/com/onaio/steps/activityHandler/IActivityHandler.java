package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;

public interface IActivityHandler {
    public boolean shouldOpen(int menu_id);
    public boolean open(ListActivity activity);
    public void handleResult(ListActivity activity, Intent data, int resultCode);
    public boolean canHandleResult(int requestCode);
}
