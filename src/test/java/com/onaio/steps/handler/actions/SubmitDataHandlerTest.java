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

package com.onaio.steps.handler.actions;

import android.app.Dialog;
import android.widget.CheckBox;

import com.onaio.steps.R;
import com.onaio.steps.activities.HouseholdListActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by Jason Rogena - jrogena@ona.io on 27/10/2016.
 */
@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SubmitDataHandlerTest {
    private HouseholdListActivity householdListActivity;

    @Before
    public void setup() {
        householdListActivity = Robolectric.buildActivity(HouseholdListActivity.class).create().get();
    }
    /**
     * This method tests whether the checkboxes in the submit data dialog are checked by default
     */
    @Test
    public void testSubmitDialogCheckboxes() {
        final Dialog dialog = new Dialog(householdListActivity);
        dialog.setContentView(R.layout.dialog_submit_data);

        CheckBox exportHouseholdListCheckBox = (CheckBox) dialog.findViewById(R.id.exportHouseholdListCheckBox);
        assertTrue(exportHouseholdListCheckBox.isChecked());
        CheckBox submitRecordsCheckBox = (CheckBox) dialog.findViewById(R.id.submitRecordsCheckBox);
        assertTrue(submitRecordsCheckBox.isChecked());
    }
}