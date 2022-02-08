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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.content.Intent;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.ServerStatus;
import com.onaio.steps.orchestrators.flows.FlowType;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

public class SettingsActivityTest extends StepsTestRunner {

    private SettingsActivity settingsActivity;
    private Intent intent;


    @Before
    public void setup() {
        intent = new Intent();
        intent.putExtra(Constants.FLOW_TYPE, FlowType.Household.toString());

        settingsActivity=Robolectric.buildActivity(SettingsActivity.class, intent).create().get();
    }

    @Test
    public void ShouldSetInitialSettingPage() {
        TextView header = (TextView) settingsActivity.findViewById(R.id.form_header);

        assertEquals(R.id.settings, shadowOf(settingsActivity).getContentView().getId());
        assertEquals(settingsActivity.getString(R.string.action_settings), header.getText());
    }

    @Test
    public void ShouldFinishActivityOnCancel(){
        settingsActivity.cancel(null);
        assertTrue(settingsActivity.isFinishing());
    }

    /**
     * This method test whether the wipe data feature works
     * @throws Exception
     */
    @Test
    public void testWipeData() throws Exception {
        DatabaseHelper databaseHelper = new DatabaseHelper(settingsActivity);
        //add data to the database
        Household household = new Household("householdName", "phoneNumber", InterviewStatus.SELECTION_NOT_DONE, "2016-10-10", "testDeviceId", "Test comments", null);
        household.setServerStatus(ServerStatus.NOT_SENT);
        household.save(databaseHelper);

        //truncate the data
        assertEquals(Household.getAllInOrder(databaseHelper).size(), 1);
        databaseHelper.truncate();

        //check if data still there
        assertEquals(Household.getAllInOrder(databaseHelper).size(), 0);
    }

}