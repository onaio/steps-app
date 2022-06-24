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

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.onaio.steps.Properties;
import com.onaio.steps.activities.MainActivityOrchestrator;

public class KeyValueStore {

    private final Context context;

    public KeyValueStore(Context context) {
        this.context = context;
    }

    public String getString(String key) {
        return dataStore(context).getString(key, Properties.get(key));
    }

    public boolean putString(String key, String value) {
        SharedPreferences.Editor editor = dataStoreEditor(context);
        editor.putString(key, value);
        return editor.commit();
    }

    private SharedPreferences dataStore(Context context) {
        return context.getSharedPreferences(getPreferenceFileName(), MODE_PRIVATE);
    }

    private SharedPreferences.Editor dataStoreEditor(Context context) {
        return dataStore(context).edit();
    }

    public void clear(Context context) {
        dataStoreEditor(context).clear().apply();
    }

    @NonNull
    public String getPreferenceFileName() {
        String className = MainActivityOrchestrator.class.getSimpleName();
        String packageName = MainActivityOrchestrator.class.getPackage().getName();
        int startIndex = packageName.lastIndexOf(".");
        return packageName.substring(startIndex + 1) + "." + className;
    }
}
