package com.onaio.steps;

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
import static com.onaio.steps.helper.Constants.SETTINGS_AUTH_TIME;
import static com.onaio.steps.helper.Constants.SETTINGS_PASSWORD_HASH;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PropertiesTest {

    @Test
    public void testVerifyDefaultPropertyValues() {

        verifyAssert(ENDPOINT_URL, "https://preview.steps.ona.io/upload-file");
        verifyAssert(HH_FORM_ID,"STEPS_Instrument_V3_1");
        verifyAssert(HH_MIN_AGE, "18");
        verifyAssert(HH_MAX_AGE, "69");
        verifyAssert(IMPORT_URL, "https://preview.steps.ona.io/export");
        verifyAssert(PA_FORM_ID,"STEPS_Instrument_V3_1");
        verifyAssert(PA_MIN_AGE, "18");
        verifyAssert(PA_MAX_AGE, "69");
        verifyAssert(FLOW_TYPE, "None");
        verifyAssert(HH_PHONE_ID, null);
        verifyAssert(SETTINGS_PASSWORD_HASH, null);
        verifyAssert(SETTINGS_AUTH_TIME, null);

    }

    private void verifyAssert(String key, String expectedValue) {
        assertEquals(expectedValue, Properties.get(key));
    }
}
