package com.onaio.steps.helper;

import android.app.Activity;

public class KeyValueStoreFactory {

    private static KeyValueStore keyValueStore;

    public static KeyValueStore instance(Activity activity) {
        if(keyValueStore == null) {
            keyValueStore = new KeyValueStore(activity);
        }
        return keyValueStore;
    }

}
