package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;

public interface IActivityHandler {
    public boolean shouldOpen(int menu_id);
    public boolean open();
    public void handleResult(Intent data, int resultCode);
    public boolean canHandleResult(int requestCode);
}
