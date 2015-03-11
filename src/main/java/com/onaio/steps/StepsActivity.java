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

import com.onaio.steps.helper.DatabaseHelper;

import java.util.List;

public class StepsActivity extends ListActivity {

    public static final String PHONE_ID = "phoneId";
    public static final String HOUSEHOLD_NAME = "household_name";
    private static final int SETTINGS_IDENTIFIER = 1;
    private static final int HOUSEHOLD_IDENTIFIER = 2;
    private String phoneId;
    private DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        phoneId = fetchPhoneId();
        db = new DatabaseHelper(getApplicationContext());
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
            switch (requestCode){
                case SETTINGS_IDENTIFIER:
                    phoneId = savePhoneId(data.getStringExtra(PHONE_ID));
                    break;
                case HOUSEHOLD_IDENTIFIER:
                    ArrayAdapter<String> listAdapter = (ArrayAdapter<String>) getListView().getAdapter();
                    listAdapter.add(data.getStringExtra(HOUSEHOLD_NAME));
            }
        } else {
            savePhoneIdErrorHandler();
        }
    }

    private void openSettings() {
        Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
        startActivityForResult(intent, SETTINGS_IDENTIFIER);
    }

    private void addHousehold() {
        if (phoneId == null) {
            alertUserToSetPhoneId();
        } else {
            Intent intent = new Intent(getBaseContext(), NewHouseholdActivity.class);
            startActivityForResult(intent, HOUSEHOLD_IDENTIFIER);
        }
    }

    private String fetchPhoneId() {
        return dataStore().getString(PHONE_ID, null);
    }

    private List<String> fetchHouseholds() {
        return db.getHouseholdNames();
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
