package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.onaio.steps.R;
import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.activity.NewHouseholdActivity;
import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.adapter.HouseholdAdapter;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.RequestCode;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.onaio.steps.helper.Constants.HOUSEHOLD_SEED;
import static com.onaio.steps.helper.Constants.PHONE_ID;

public class NewHouseholdActivityHandler implements IMenuHandler, IActivityResultHandler {

    private ListActivity activity;
    private CustomDialog dialog = new CustomDialog();

    public NewHouseholdActivityHandler(ListActivity activity) {
        this(activity,new CustomDialog());
    }

    NewHouseholdActivityHandler(ListActivity activity, CustomDialog dialog) {
        this.activity = activity;
        this.dialog = dialog;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.action_add_household;
    }

    @Override
    public boolean open() {
        if (isEmpty(getValue(PHONE_ID))) {
            notifyUserToSetPhoneId(activity);
        } else {
            Intent intent = new Intent(activity.getBaseContext(), NewHouseholdActivity.class);
            intent.putExtra(PHONE_ID, getValue(PHONE_ID));
            intent.putExtra(HOUSEHOLD_SEED, getValue(HOUSEHOLD_SEED));
            activity.startActivityForResult(intent, RequestCode.NEW_HOUSEHOLD.getCode());
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

            Intent householdActivityIntent = new Intent(activity, HouseholdActivity.class);
            householdActivityIntent.putExtra(Constants.HOUSEHOLD,data.getSerializableExtra(Constants.HOUSEHOLD));
            activity.startActivity(householdActivityIntent);
        }
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == RequestCode.NEW_HOUSEHOLD.getCode();
    }

    private void notifyUserToSetPhoneId(ListActivity activity) {
        dialog.notify(activity, CustomDialog.EmptyListener, R.string.phone_id_message_title, R.string.phone_id_message);
    }

    private String getValue(String key) {
        return dataStore(activity).getString(key, null) ;
    }

    private SharedPreferences dataStore(ListActivity activity) {
        return activity.getPreferences(MODE_PRIVATE);
    }

    private boolean isEmpty(String value) {
        return value == null || value.equals("");
    }
}
