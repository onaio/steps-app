package com.onaio.steps.activityHandler;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;

import com.onaio.steps.activity.NewHouseholdActivity;
import com.onaio.steps.R;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.onaio.steps.activity.NewHouseholdActivity.HOUSEHOLD_NAME;
import static com.onaio.steps.activity.StepsActivity.PHONE_ID;

public class NewHouseholdActivityHandler implements IActivityHandler {

    private static final int IDENTIFIER = 2;

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.action_add;
    }

    @Override
    public boolean open(ListActivity activity) {
        if (getPhoneId(activity)== null) {
            alertUserToSetPhoneId(activity);
        } else {
            Intent intent = new Intent(activity.getBaseContext(), NewHouseholdActivity.class);
            intent.putExtra(PHONE_ID,getPhoneId(activity));
            activity.startActivityForResult(intent, IDENTIFIER);
        }
        return true;
    }

    @Override
    public void handleResult(ListActivity activity, Intent data, int resultCode) {
        if (resultCode == RESULT_OK) {
            ArrayAdapter<String> listAdapter = (ArrayAdapter<String>) activity.getListView().getAdapter();
            if (listAdapter == null)
                return;
            listAdapter.insert(data.getStringExtra(HOUSEHOLD_NAME),0);
        }
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == IDENTIFIER;
    }

    @Override
    public IActivityHandler with(Object data) {
        return this;
    }

    private void alertUserToSetPhoneId(ListActivity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.phone_id_message_title).setMessage(R.string.phone_id_message);
        builder.create().show();
    }

    private String getPhoneId(ListActivity activity) {
        return dataStore(activity).getString(PHONE_ID, null) ;
    }

    private SharedPreferences dataStore(ListActivity activity) {
        return activity.getPreferences(MODE_PRIVATE);
    }

}
