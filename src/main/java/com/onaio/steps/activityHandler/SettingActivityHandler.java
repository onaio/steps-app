package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.onaio.steps.R;
import com.onaio.steps.activity.SettingsActivity;
import com.onaio.steps.helper.Constants;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class SettingActivityHandler implements IActivityHandler {

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.action_settings;
    }

    @Override
    public boolean open(ListActivity activity) {
        Intent intent = new Intent(activity.getBaseContext(), SettingsActivity.class);
        intent.putExtra(Constants.PHONE_ID,getPhoneId(activity));
        activity.startActivityForResult(intent, Constants.SETTING_IDENTIFIER);
        return true;
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == Constants.SETTING_IDENTIFIER;
    }

    @Override
    public IActivityHandler with(Object data) {
        return this;
    }

    @Override
    public void handleResult(ListActivity activity, Intent data, int resultCode) {
        if (resultCode == RESULT_OK)
            handleSuccess(activity, data);
        else
            savePhoneIdErrorHandler();
    }

    private void handleSuccess(ListActivity activity, Intent data) {
        SharedPreferences.Editor editor = dataStoreEditor(activity);
        String phoneId = data.getStringExtra(Constants.PHONE_ID);
        editor.putString(Constants.PHONE_ID, phoneId);
        if (!editor.commit())
            savePhoneIdErrorHandler();
    }

    private void savePhoneIdErrorHandler() {
        //TODO: toast message for save phone id failure
    }

    private String getPhoneId(ListActivity activity) {
        return dataStore(activity).getString(Constants.PHONE_ID, null) ;
    }

    private SharedPreferences dataStore(ListActivity activity) {
        return activity.getPreferences(MODE_PRIVATE);
    }

    private SharedPreferences.Editor dataStoreEditor(ListActivity activity) {
        return dataStore(activity).edit();
    }

}
