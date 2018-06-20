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
public class ParticipantFlowTest {

    public String error_string;
    private SettingsActivity settingsActivity;
    private ParticipantFlow participantFlow;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup(){
        Intent intent = new Intent();
        error_string = "Invalid %s, please fill or correct the following fields: %s";
        intent =intent.putExtra(Constants.FLOW_TYPE, FlowType.Participant.toString());
        settingsActivity = Robolectric.buildActivity(SettingsActivity.class).withIntent(intent).create().get();
        participantFlow = new ParticipantFlow(settingsActivity);
    }

    @Test
    public void ShouldBeAbleHandleParticipantFlowType(){

        assertTrue(participantFlow.canHandle(FlowType.Participant));
    }

    @Test
    public void ShouldNotBeAbleToHandleHouseholdFlowType(){

        assertFalse(participantFlow.canHandle(FlowType.Household));
    }

    @Test
    public void ShouldBeAbleToPopulateViewWithProperTextFields(){
        participantFlow.prepareSettingScreen(false);
        assertNotNull(settingsActivity.findViewById(R.id.household_flow_disabled));
        assertNotNull(settingsActivity.findViewById(R.id.participant_flow));
        assertNotNull(settingsActivity.findViewById(R.id.min_age_participant));
        assertNotNull(settingsActivity.findViewById(R.id.max_age_participant));
   }

    @Test
    public void ShouldSaveDataFromSettingFields(){
        TextView deviceId = (TextView) settingsActivity.findViewById(R.id.deviceId_participant);
        TextView formId = (TextView) settingsActivity.findViewById(R.id.form_id_participant);
        TextView maxage = (TextView) settingsActivity.findViewById(R.id.max_age_participant);
        TextView minage = (TextView) settingsActivity.findViewById(R.id.min_age_participant);
        deviceId.setText("1234567");
        formId.setText("STEPS_Instrument_V3_1");
        maxage.setText("69");
        minage.setText("18");
        participantFlow.saveSettings();
        assertEquals("1234567", getValue(settingsActivity, Constants.PA_PHONE_ID));
        assertEquals("STEPS_Instrument_V3_1", getValue(settingsActivity, Constants.PA_FORM_ID));
        assertEquals("69", getValue(settingsActivity, Constants.PA_MAX_AGE));
        assertEquals("18",getValue(settingsActivity,Constants.PA_MIN_AGE));
    }

    @Test
    public void ShouldTrimSettingsBeforeSaving() {
        TextView deviceId = (TextView) settingsActivity.findViewById(R.id.deviceId_participant);
        TextView formId = (TextView) settingsActivity.findViewById(R.id.form_id_participant);
        TextView maxage = (TextView) settingsActivity.findViewById(R.id.max_age_participant);
        TextView minage = (TextView) settingsActivity.findViewById(R.id.min_age_participant);
        deviceId.setText(" 1234567 ");
        formId.setText(" STEPS_Instrument_V3_1 ");
        maxage.setText(" 69 ");
        minage.setText(" 18 ");
        participantFlow.saveSettings();
        assertEquals("1234567", getValue(settingsActivity, Constants.PA_PHONE_ID));
        assertEquals("STEPS_Instrument_V3_1", getValue(settingsActivity, Constants.PA_FORM_ID));
        assertEquals("69", getValue(settingsActivity, Constants.PA_MAX_AGE));
        assertEquals("18", getValue(settingsActivity, Constants.PA_MIN_AGE));
    }

    @Test
    public void ShouldNotSaveDataFromSettingFields() throws InvalidDataException {
        TextView deviceId = (TextView) settingsActivity.findViewById(R.id.deviceId_participant);
        TextView formId = (TextView) settingsActivity.findViewById(R.id.form_id_participant);
        TextView maxage = (TextView) settingsActivity.findViewById(R.id.max_age_participant);
        TextView minage = (TextView) settingsActivity.findViewById(R.id.min_age_participant);
        deviceId.setText("");
        formId.setText("STEPS_Instrument_V3_1");
        maxage.setText("");
        minage.setText("18");

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string, "Settings", "Device ID", "Max Age"));

        participantFlow.validateOptions();

    }

    @Test
    public void validateParticipantsSettingsShouldPass() {
        participantFlow.validateParticipantsSettings("did", "fid", "20", "35");
    }

    @Test
    public void validateParticipantsSettingsShouldFail() {
        assertEquals(1, participantFlow.validateParticipantsSettings("", "fid", "20", "35").size());
        assertEquals(1, participantFlow.validateParticipantsSettings("did", null, "20", "35").size());
        assertEquals(1, participantFlow.validateParticipantsSettings("did", "fid", "", "35").size());
        assertEquals(1, participantFlow.validateParticipantsSettings("did", "fid", "20", "").size());
    }

    private String getValue(Activity activity, String key) {
        return KeyValueStoreFactory.instance(activity).getString(key);
    }
}