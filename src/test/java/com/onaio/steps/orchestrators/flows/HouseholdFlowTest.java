package com.onaio.steps.orchestrators.flows;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.activities.SettingsActivity;
import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStoreFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class HouseholdFlowTest {

    public String error_string;
    private SettingsActivity settingsActivity;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private HouseholdFlow householdFlow;

    @Before
    public void setup(){
        Intent intent = new Intent();
        error_string = "Invalid %s, please fill or correct the following fields: %s";
        intent =intent.putExtra(Constants.FLOW_TYPE, FlowType.Household.toString());
        settingsActivity = Robolectric.buildActivity(SettingsActivity.class).withIntent(intent).create().get();
        householdFlow = new HouseholdFlow(settingsActivity);
    }

    @Test
    public void ShouldBeAbleHandleHouseholdFlowType(){

        assertTrue(householdFlow.canHandle(FlowType.Household));
    }

    @Test
    public void ShouldNotBeAbleToHandleParticipantFlowType(){

        assertFalse(householdFlow.canHandle(FlowType.Participant));
    }

    @Test
    public void ShouldBeAbleToPopulateViewWithProperTextFields(){
        householdFlow.prepareSettingScreen();
        assertNotNull(settingsActivity.findViewById(R.id.household_flow_disabled));
        assertNotNull(settingsActivity.findViewById(R.id.participant_flow));
        assertNotNull(settingsActivity.findViewById(R.id.household_seed));
        assertNotNull(settingsActivity.findViewById(R.id.household_seed_label));
        assertNotNull(settingsActivity.findViewById(R.id.endpointUrl));
        assertNotNull(settingsActivity.findViewById(R.id.endpointUrl_label));
        assertNotNull(settingsActivity.findViewById(R.id.min_age));
        assertNotNull(settingsActivity.findViewById(R.id.max_age));
    }

    @Test
    public void ShouldSaveDataFromSettingFields(){
        TextView deviceId = (TextView) settingsActivity.findViewById(R.id.deviceId);
        TextView formId = (TextView) settingsActivity.findViewById(R.id.form_id);
        TextView maxage = (TextView) settingsActivity.findViewById(R.id.max_age);
        TextView minage = (TextView) settingsActivity.findViewById(R.id.min_age);
        TextView endPointUrl = (TextView) settingsActivity.findViewById(R.id.endpointUrl);
        TextView householdseed = (TextView) settingsActivity.findViewById(R.id.household_seed);
        deviceId.setText("1234567");
        formId.setText("STEPS_Instrument_V3_1");
        maxage.setText("69");
        minage.setText("18");
        endPointUrl.setText("http://192.168.1.20:8000");
        householdseed.setText("100");
        householdFlow.saveSettings();
        assertEquals("1234567", getValue(settingsActivity, Constants.PHONE_ID));
        assertEquals("STEPS_Instrument_V3_1", getValue(settingsActivity, Constants.FORM_ID));
        assertEquals("69", getValue(settingsActivity, Constants.MAX_AGE));
        assertEquals("18",getValue(settingsActivity,Constants.MIN_AGE));
    }

    @Test
    public void ShouldNotSaveDataFromSettingFields() throws InvalidDataException {
        TextView deviceId = (TextView) settingsActivity.findViewById(R.id.deviceId);
        TextView formId = (TextView) settingsActivity.findViewById(R.id.form_id);
        TextView maxage = (TextView) settingsActivity.findViewById(R.id.max_age);
        TextView minage = (TextView) settingsActivity.findViewById(R.id.min_age);
        TextView endPointUrl = (TextView) settingsActivity.findViewById(R.id.endpointUrl);
        TextView householdseed = (TextView) settingsActivity.findViewById(R.id.household_seed);
        deviceId.setText("");
        formId.setText("");
        maxage.setText("");
        minage.setText("18");
        endPointUrl.setText("http://192.168.1.20:8000");
        householdseed.setText("");

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string, "Settings", "Device ID,Form ID,Max age."));

        householdFlow.validateOptions();
    }


    private String getValue(Activity activity, String key) {
        return KeyValueStoreFactory.instance(activity).getString(key);
    }



}