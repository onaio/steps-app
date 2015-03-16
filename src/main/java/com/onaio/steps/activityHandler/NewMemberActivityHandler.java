package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;
import android.widget.ArrayAdapter;

import com.onaio.steps.R;
import com.onaio.steps.activity.NewMemberActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;
import static android.app.Activity.RESULT_OK;

public class NewMemberActivityHandler implements IHandler {

    private Household household;
    private ListActivity activity;

    public NewMemberActivityHandler(ListActivity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.action_add;
    }

    @Override
    public boolean open() {
        if (household== null) return true;
        Intent intent = new Intent(activity.getBaseContext(), NewMemberActivity.class);
        intent.putExtra(Constants.HOUSEHOLD,household);
        activity.startActivityForResult(intent, Constants.NEW_MEMBER_IDENTIFIER);
        return true;
    }

    @Override
    public void handleResult(Intent data, int resultCode) {
        if (resultCode == RESULT_OK) {
            ArrayAdapter<String> listAdapter = (ArrayAdapter<String>) activity.getListView().getAdapter();
            if (listAdapter == null)
                return;
            listAdapter.add(data.getStringExtra(Constants.MEMBER_NAME));
        }
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == Constants.NEW_MEMBER_IDENTIFIER;
    }

}
