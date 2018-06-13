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

package com.onaio.steps.handler.activities;

import android.content.Context;
import android.content.SharedPreferences;

import com.onaio.steps.activities.ParticipantListActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.orchestrators.flows.FlowType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 05/06/2018.
 */
@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class HouseholdActivityMenuItemHandlerTest {

    private HouseholdActivityMenuItemHandler householdActivityMenuItemHandler;
    private ParticipantListActivity participantListActivity;

    @Before
    public void setUp() {
         participantListActivity = Robolectric.buildActivity(ParticipantListActivity.class)
                .create()
                .get();
        householdActivityMenuItemHandler = new HouseholdActivityMenuItemHandler(participantListActivity);
    }

    @Test
    public void openShouldSaveFlowTypeWithCompleteParticipantsSettings() {
        SharedPreferences sharedPreferences = participantListActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.HH_SURVEY_ID, "YU");
        editor.putString(Constants.HH_PHONE_ID, "hhPhone");
        editor.commit();

        householdActivityMenuItemHandler.open();

        String expectedValue = FlowType.Household.toString();
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(participantListActivity);
        String actualValue = keyValueStore.getString(Constants.FLOW_TYPE);

        assertEquals(expectedValue, actualValue);
        assertTrue(participantListActivity.isFinishing());

        // Remove the strings

        editor.remove(Constants.HH_SURVEY_ID);
        editor.remove(Constants.HH_PHONE_ID);
        editor.commit();
    }

}