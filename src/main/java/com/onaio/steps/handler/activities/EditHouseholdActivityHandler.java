package com.onaio.steps.handler.activities;

import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activities.EditHouseholdActivity;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.RequestCode;

import static android.app.Activity.*;
import static com.onaio.steps.helper.Constants.HH_HOUSEHOLD;

public class EditHouseholdActivityHandler implements IMenuHandler, IActivityResultHandler {

    private final int MENU_ID = R.id.action_household_edit;
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
        Intent intent = new Intent(activity, EditHouseholdActivity.class);
        intent.putExtra(HH_HOUSEHOLD,household);
        activity.startActivityForResult(intent, RequestCode.EDIT_HOUSEHOLD.getCode());
        return true;
    }

    @Override
    public void handleResult(Intent intent, int resultCode) {
        if(resultCode != RESULT_OK)
            return;
        activity.finish();
        household = (Household)intent.getSerializableExtra(HH_HOUSEHOLD);
        new HouseholdActivityHandler(activity,household).open();
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == RequestCode.EDIT_HOUSEHOLD.getCode();
    }
}
