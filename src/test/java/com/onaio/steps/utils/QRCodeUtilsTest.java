package com.onaio.steps.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.HouseholdListActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

public class QRCodeUtilsTest extends StepsTestRunner {

    private AppCompatActivity activity;
    private KeyValueStore keyValueStore;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(HouseholdListActivity.class)
                .create()
                .get();
        keyValueStore = KeyValueStoreFactory.instance(activity);
    }

    @Test
    public void exportSettingsToJSON() throws JSONException {

        keyValueStore.putString(Constants.HH_MAX_AGE, "78");
        keyValueStore.putString(Constants.HH_MIN_AGE, "17");
        keyValueStore.putString(Constants.HH_USER_ID, "some user id here");
        keyValueStore.putString(Constants.HH_PHONE_ID, "some device id here");
        keyValueStore.putString(Constants.HH_HOUSEHOLD_SEED, "1");
        keyValueStore.putString(Constants.HH_FORM_ID, "some form id here");
        keyValueStore.putString(Constants.IMPORT_URL, "some import url");
        keyValueStore.putString(Constants.ENDPOINT_URL, "some export url");

        keyValueStore.putString(Constants.PA_FORM_ID, "some form id here");
        keyValueStore.putString(Constants.PA_PHONE_ID, "some form id here");
        keyValueStore.putString(Constants.PA_MIN_AGE, "12");
        keyValueStore.putString(Constants.PA_MAX_AGE, "98");


        String exportSettingsJSON = QRCodeUtils.exportSettingsToJSON(activity);
        JSONObject resultJSON = new JSONObject(exportSettingsJSON);
        JSONObject expectedJSON = new JSONObject(settingsJSONString);

        assertEquals(expectedJSON.toString(), resultJSON.toString());

        clearSettings();
    }

    @Test
    public void importSettingsFromJSON() {
        assertTrue(QRCodeUtils.importSettingsFromJSON(activity, settingsJSONString));

        // Participant settings
        assertEquals("", keyValueStore.getString(Constants.PA_PHONE_ID));
        assertEquals("some form id here", keyValueStore.getString(Constants.PA_FORM_ID));
        assertEquals("12", keyValueStore.getString(Constants.PA_MIN_AGE));
        assertEquals("98", keyValueStore.getString(Constants.PA_MAX_AGE));

        // Household settings
        assertEquals("some user id here", keyValueStore.getString(Constants.HH_USER_ID));
        assertEquals("", keyValueStore.getString(Constants.HH_PHONE_ID));
        assertEquals("1", keyValueStore.getString(Constants.HH_HOUSEHOLD_SEED));
        assertEquals("some form id here", keyValueStore.getString(Constants.HH_FORM_ID));
        assertEquals("17", keyValueStore.getString(Constants.HH_MIN_AGE));
        assertEquals("78", keyValueStore.getString(Constants.HH_MAX_AGE));
        assertEquals("some import url", keyValueStore.getString(Constants.IMPORT_URL));
        assertEquals("some export url", keyValueStore.getString(Constants.ENDPOINT_URL));

        clearSettings();
    }

    private void clearSettings() {
        keyValueStore.putString(Constants.HH_MAX_AGE, null);
        keyValueStore.putString(Constants.HH_MIN_AGE, null);
        keyValueStore.putString(Constants.HH_USER_ID, null);
        keyValueStore.putString(Constants.HH_PHONE_ID, null);
        keyValueStore.putString(Constants.HH_HOUSEHOLD_SEED, null);
        keyValueStore.putString(Constants.HH_FORM_ID, null);
        keyValueStore.putString(Constants.IMPORT_URL, null);
        keyValueStore.putString(Constants.ENDPOINT_URL, null);

        keyValueStore.putString(Constants.PA_FORM_ID, null);
        keyValueStore.putString(Constants.PA_PHONE_ID, null);
        keyValueStore.putString(Constants.PA_MIN_AGE, null);
        keyValueStore.putString(Constants.PA_MAX_AGE, null);
    }

    private final String settingsJSONString = "{\n" +
            "  \"participantSettings\": {\n" +
            "    \"paFormId\": \"some form id here\",\n" +
            "    \"paMinAge\": \"12\",\n" +
            "    \"paMaxAge\": \"98\"\n" +
            "  },\n" +
            "  \"householdSettings\": {\n" +
            "    \"hhUserId\": \"some user id here\",\n" +
            "    \"hhHouseholdSeedId\": \"1\",\n" +
            "    \"hhFormId\": \"some form id here\", \n" +
            "    \"hhMinAge\": \"17\",\n" +
            "    \"hhMaxAge\": \"78\",\n" +
            "    \"importUrl\": \"some import url\",\n" +
            "    \"endpointUrl\": \"some export url\"\n" +
            "  }\n" +
            "}";
}