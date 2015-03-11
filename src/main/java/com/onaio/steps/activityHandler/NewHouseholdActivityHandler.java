package com.onaio.steps.activityHandler;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;

import com.onaio.steps.NewHouseholdActivity;
import com.onaio.steps.R;

public class NewHouseholdActivityHandler implements IActivityHandler {
    private static final int IDENTIFIER = 2;
    public static final String HOUSEHOLD_NAME = "household_name";
    private static final String PHONE_ID = "phoneId";

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
        if (resultCode == activity.RESULT_OK) {
            ArrayAdapter<String> listAdapter = (ArrayAdapter<String>) activity.getListView().getAdapter();
            listAdapter.add(data.getStringExtra(HOUSEHOLD_NAME));
        }
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == IDENTIFIER;
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
        return activity.getPreferences(activity.MODE_PRIVATE);
    }

}
