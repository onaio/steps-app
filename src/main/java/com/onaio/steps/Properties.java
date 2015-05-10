package com.onaio.steps;

import java.util.HashMap;
import java.util.Map;

import static com.onaio.steps.helper.Constants.ENDPOINT_URL;
import static com.onaio.steps.helper.Constants.MAX_AGE;
import static com.onaio.steps.helper.Constants.MIN_AGE;

public class Properties {
    private static final Map<String, String> props = new HashMap<String, String>();

    static {
        props.put(ENDPOINT_URL, "http://192.168.1.20:8000");
        props.put(MIN_AGE, "18");
        props.put(MAX_AGE, "69");
    }

    public static String get(String key) {
        return props.get(key);
    }
}
