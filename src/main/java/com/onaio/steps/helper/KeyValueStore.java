package com.onaio.steps.helper;

import android.app.Activity;
import android.content.SharedPreferences;
import com.onaio.steps.Properties;

import static android.content.Context.MODE_PRIVATE;

public class KeyValueStore {

    private Activity activity;

    public KeyValueStore(Activity activity) {
        this.activity = activity;
    }

    public String getString(String key) {
        return dataStore(activity).getString(key, Properties.get(key));
    }

    public boolean putString(String key, String value) {
        SharedPreferences.Editor editor = dataStoreEditor(activity);
        editor.putString(key, value);
        return editor.commit();
    }

    private SharedPreferences dataStore(Activity activity) {
        return activity.getPreferences(MODE_PRIVATE);
    }

    private SharedPreferences.Editor dataStoreEditor(Activity activity) {
        return dataStore(activity).edit();
    }

}
