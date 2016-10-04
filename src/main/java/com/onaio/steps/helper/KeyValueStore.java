/*
 * Copyright 2016. World Health Organization
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
