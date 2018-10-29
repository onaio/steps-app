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

package com.onaio.steps.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import com.onaio.steps.helper.Device;
import com.onaio.steps.helper.RegisterUniqueDeviceIdTask;
import com.onaio.steps.orchestrators.FlowOrchestrator;
import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStoreFactory;

public class MainActivityOrchestrator extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RegisterUniqueDeviceIdTask registerUniqueDeviceIdTask = new RegisterUniqueDeviceIdTask(getApplicationContext());
        registerUniqueDeviceIdTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        FlowType flowType = FlowType.valueOf(getValue(Constants.FLOW_TYPE));
        new FlowOrchestrator(this).start(flowType);
        finish();
    }

    private String getValue(String key) {
        return KeyValueStoreFactory.instance(this).getString(key) ;
    }
}
