package com.onaio.steps.activityHandler;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;

import com.onaio.steps.R;
import com.onaio.steps.activity.NewHouseholdActivity;
import com.onaio.steps.activity.NewMemberActivity;
import com.onaio.steps.model.Household;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.onaio.steps.activity.NewHouseholdActivity.HOUSEHOLD_NAME;
import static com.onaio.steps.activity.StepsActivity.PHONE_ID;

public class NewMemberActivityHandler implements IActivityHandler {

    private static final int IDENTIFIER = 4;
    private Household household;

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.action_add;
    }

    @Override
    public boolean open(ListActivity activity) {
        if (household== null) return true;
        Intent intent = new Intent(activity.getBaseContext(), NewMemberActivity.class);
        intent.putExtra("HOUSEHOLD",household);
        activity.startActivityForResult(intent, IDENTIFIER);
        return true;
    }

    @Override
    public void handleResult(ListActivity activity, Intent data, int resultCode) {
        if (resultCode == RESULT_OK) {
            ArrayAdapter<String> listAdapter = (ArrayAdapter<String>) activity.getListView().getAdapter();
            if (listAdapter == null)
                return;
            listAdapter.insert(data.getStringExtra(HOUSEHOLD_NAME), 0);
        }
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == IDENTIFIER;
    }

    @Override
    public IActivityHandler with(Object data) {
        household = ((Household) data);
        return this;
    }

}
