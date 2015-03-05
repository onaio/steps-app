package com.onaio.steps;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class StepsActivity extends ListActivity {

    public static final String PHONE_ID = "phoneId";
    private static final int IDENTIFIER = 1;
    private String phoneId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        phoneId = fetchPhoneId();
        getListView().setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fetchHouseholds()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_add:
                addHousehold();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            phoneId = savePhoneId(data.getStringExtra(PHONE_ID));
        } else {
            savePhoneIdErrorHandler();
        }
    }

    private void openSettings() {
        Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
        startActivityForResult(intent, IDENTIFIER);
    }

    private void addHousehold() {
        if (phoneId == null) {
            alertUserToSetPhoneId();
        } else {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListView().getAdapter();
            adapter.add(String.format("%s-%d", phoneId, adapter.getCount() + 1));
        }
    }

    private String fetchPhoneId() {
        return dataStore().getString(PHONE_ID, null);
    }

    private ArrayList<String> fetchHouseholds() {
        return new ArrayList<String>();
    }

    private String savePhoneId(String phoneId) {
        SharedPreferences.Editor editor = dataStoreEditor();
        editor.putString(PHONE_ID, phoneId);
        if (editor.commit()) {
            return phoneId;
        }
        savePhoneIdErrorHandler();
        return null;
    }

    private SharedPreferences dataStore() {
        return getPreferences(MODE_PRIVATE);
    }

    private SharedPreferences.Editor dataStoreEditor() {
        return dataStore().edit();
    }

    private void alertUserToSetPhoneId() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.phone_id_message_title).setMessage(R.string.phone_id_message);
        builder.create().show();
    }

    private void savePhoneIdErrorHandler() {
        //TODO: toast message for save phone id failure
    }
}
