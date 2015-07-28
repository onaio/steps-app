package com.onaio.steps;

import java.util.HashMap;
import java.util.Map;

import static com.onaio.steps.helper.Constants.ENDPOINT_URL;
import static com.onaio.steps.helper.Constants.FLOW_TYPE;
import static com.onaio.steps.helper.Constants.FORM_ID;
import static com.onaio.steps.helper.Constants.IMPORT_URL;
import static com.onaio.steps.helper.Constants.MAX_AGE;
import static com.onaio.steps.helper.Constants.MIN_AGE;

public class Properties {
    private static final Map<String, String> props = new HashMap<String, String>();

    static {
        props.put(FORM_ID,"STEPS_Instrument_V3_1");
        props.put(ENDPOINT_URL, "http://steps.ona.io/upload-file");
        props.put(IMPORT_URL, "http://steps.ona.io/export");
        props.put(MIN_AGE, "18");
        props.put(MAX_AGE, "69");
        props.put(FLOW_TYPE, "None");
    }

    public static String get(String key) {
        return props.get(key);
    }
}
