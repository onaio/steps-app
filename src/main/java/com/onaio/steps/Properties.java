package com.onaio.steps;

import java.util.HashMap;
import java.util.Map;

import static com.onaio.steps.helper.Constants.CHILD_MAX_AGE;
import static com.onaio.steps.helper.Constants.CHILD_MIN_AGE;
import static com.onaio.steps.helper.Constants.ENDPOINT_URL;
import static com.onaio.steps.helper.Constants.FLOW_TYPE;
import static com.onaio.steps.helper.Constants.HH_FORM_ID;
import static com.onaio.steps.helper.Constants.HH_MAX_AGE;
import static com.onaio.steps.helper.Constants.HH_MIN_AGE;
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
        props.put(CHILD_MIN_AGE, "4");
        props.put(HH_MAX_AGE, "69");
        props.put(CHILD_MAX_AGE, "9");
        props.put(IMPORT_URL, "http://steps.ona.io/export");
        props.put(PA_FORM_ID,"STEPS_Instrument_V3_1");
        props.put(PA_MIN_AGE, "18");
        props.put(PA_MAX_AGE, "69");
        props.put(FLOW_TYPE, "None");
    }

    public static String get(String key) {
        return props.get(key);
    }
}
