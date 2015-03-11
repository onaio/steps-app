package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.R;

import static android.app.Activity.RESULT_OK;

public class ExportActivityHandler implements IActivityHandler {

    private static final int IDENTIFIER = 3;

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.action_export;
    }

    @Override
    public boolean open(ListActivity activity) {
        return true;
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == IDENTIFIER;
    }

    @Override
    public void handleResult(ListActivity activity, Intent data, int resultCode) {
        if (resultCode == RESULT_OK)
            handleSuccess(activity, data);
        else
            exportErrorHandler();
    }

    private void handleSuccess(ListActivity activity, Intent data) {
    }

    private void exportErrorHandler() {
    }

}
