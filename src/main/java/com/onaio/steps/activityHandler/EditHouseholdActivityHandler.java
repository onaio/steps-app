package com.onaio.steps.activityHandler;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activity.EditHouseholdActivity;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuResultHandler;
import com.onaio.steps.model.Household;

import static android.app.Activity.*;
import static com.onaio.steps.helper.Constants.EDIT_HOUSEHOLD_IDENTIFIER;
import static com.onaio.steps.helper.Constants.HOUSEHOLD;

public class EditHouseholdActivityHandler implements IMenuHandler, IMenuResultHandler {

    private final int MENU_ID = R.id.action_edit;
    private ListActivity activity;
    private Household household;

    public EditHouseholdActivityHandler(ListActivity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        Intent intent = new Intent(activity.getBaseContext(), EditHouseholdActivity.class);
        intent.putExtra(HOUSEHOLD,household);
        activity.startActivityForResult(intent, EDIT_HOUSEHOLD_IDENTIFIER);
        return true;
    }

    @Override
    public void handleResult(Intent intent, int resultCode) {
        if(resultCode != RESULT_OK)
            return;
        household = (Household)intent.getSerializableExtra(HOUSEHOLD);
        new HouseholdActivityHandler(activity,household).open();
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == EDIT_HOUSEHOLD_IDENTIFIER;
    }
}
