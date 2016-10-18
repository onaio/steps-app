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

package com.onaio.steps;

import java.util.HashMap;
import java.util.Map;

import static com.onaio.steps.helper.Constants.ENDPOINT_URL;
import static com.onaio.steps.helper.Constants.FLOW_TYPE;
import static com.onaio.steps.helper.Constants.HH_FORM_ID;
import static com.onaio.steps.helper.Constants.HH_MAX_AGE;
import static com.onaio.steps.helper.Constants.HH_MIN_AGE;
import static com.onaio.steps.helper.Constants.HH_PHONE_ID;
import static com.onaio.steps.helper.Constants.IMPORT_URL;
import static com.onaio.steps.helper.Constants.PA_FORM_ID;
import static com.onaio.steps.helper.Constants.PA_MAX_AGE;
import static com.onaio.steps.helper.Constants.PA_MIN_AGE;

public class Properties {
    private static final Map<String, String> props = new HashMap<String, String>();

    static {
        props.put(ENDPOINT_URL, "http://steps.ona.io/upload-file");
        props.put(HH_FORM_ID,"STEPS_Instrument_V3_1");
        props.put(HH_MIN_AGE, "18");
        props.put(HH_MAX_AGE, "69");
        props.put(IMPORT_URL, "http://steps.ona.io/export");
        props.put(PA_FORM_ID,"STEPS_Instrument_V3_1");
        props.put(PA_MIN_AGE, "18");
        props.put(PA_MAX_AGE, "69");
        props.put(FLOW_TYPE, "None");
        props.put(HH_PHONE_ID, null);
    }

    public static String get(String key) {
        return props.get(key);
    }
}
