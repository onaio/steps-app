package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.onaio.steps.activity.NewHouseholdActivity;
import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IHandler;
import com.onaio.steps.adapter.HouseholdAdapter;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.Dialog;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Household;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class NewHouseholdActivityHandler implements IHandler {

    private ListActivity activity;
    private Dialog dialog = new Dialog();

    public NewHouseholdActivityHandler(ListActivity activity) {
        this(activity,new Dialog());
    }

    public NewHouseholdActivityHandler(ListActivity activity, Dialog dialog) {
        this.activity = activity;
        this.dialog = dialog;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.action_add;
    }

    @Override
    public boolean open() {
        if (getPhoneId(activity)== null) {
            notifyUserToSetPhoneId(activity);
        } else {
            Intent intent = new Intent(activity.getBaseContext(), NewHouseholdActivity.class);
            intent.putExtra(Constants.PHONE_ID,getPhoneId(activity));
            activity.startActivityForResult(intent, Constants.NEW_HOUSEHOLD_IDENTIFIER);
        }
        return true;
    }

    @Override
    public void handleResult(Intent data, int resultCode) {
        if (resultCode == RESULT_OK) {
            HouseholdAdapter householdAdapter = (HouseholdAdapter) activity.getListView().getAdapter();
            if (householdAdapter == null)
                return;
            householdAdapter.reinitialize(Household.getAll(new DatabaseHelper(activity.getApplicationContext())));
            householdAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == Constants.NEW_HOUSEHOLD_IDENTIFIER;
    }

    private void notifyUserToSetPhoneId(ListActivity activity) {
        dialog.notify(activity, Dialog.EmptyListener, R.string.phone_id_message, R.string.phone_id_message_title);
    }

    private String getPhoneId(ListActivity activity) {
        return dataStore(activity).getString(Constants.PHONE_ID, null) ;
    }

    private SharedPreferences dataStore(ListActivity activity) {
        return activity.getPreferences(MODE_PRIVATE);
    }

}
