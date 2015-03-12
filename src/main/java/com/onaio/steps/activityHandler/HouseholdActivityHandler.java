package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.activity.NewHouseholdActivity;
import com.onaio.steps.model.Household;

import static android.app.Activity.RESULT_OK;
import static com.onaio.steps.activity.StepsActivity.PHONE_ID;

public class HouseholdActivityHandler implements IActivityHandler {

    private static final int IDENTIFIER = 3;
    private Household listViewItem;

    @Override
    public boolean shouldOpen(int menu_id) {
        return true;
    }

    @Override
    public boolean open(ListActivity activity) {
        if (listViewItem == null) return true;
        Intent intent = new Intent(activity.getBaseContext(), HouseholdActivity.class);
        intent.putExtra("HOUSEHOLD",listViewItem);
        activity.startActivityForResult(intent, IDENTIFIER);
        return true;
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == IDENTIFIER;
    }

    @Override
    public IActivityHandler with(Object data) {
        listViewItem = ((Household) data);
        return this;
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
