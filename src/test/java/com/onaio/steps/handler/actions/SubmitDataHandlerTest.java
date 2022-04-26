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

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Dialog;
import android.widget.CheckBox;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.HouseholdListActivity;
import com.onaio.steps.dialogs.HouseholdUploadResultDialog;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import java.util.ArrayList;

/**
 * Created by Jason Rogena - jrogena@ona.io on 27/10/2016.
 */
public class SubmitDataHandlerTest extends StepsTestRunner {
    private HouseholdListActivity householdListActivity;
    private SubmitDataHandler submitDataHandler;

    @Before
    public void setup() {
        householdListActivity = spy(Robolectric.buildActivity(HouseholdListActivity.class).create().get());
        submitDataHandler = new SubmitDataHandler(householdListActivity);
    }
    /**
     * This method tests whether the checkboxes in the submit data dialog are checked by default
     */
    @Test
    public void testSubmitDialogCheckboxes() {
        final Dialog dialog = new Dialog(householdListActivity);
        dialog.setContentView(R.layout.dialog_submit_data);

        CheckBox exportHouseholdListCheckBox = dialog.findViewById(R.id.exportHouseholdListCheckBox);
        assertTrue(exportHouseholdListCheckBox.isChecked());
        CheckBox submitRecordsCheckBox = dialog.findViewById(R.id.submitRecordsCheckBox);
        assertTrue(submitRecordsCheckBox.isChecked());
    }

    @Test
    public void testDisplayUploadResultShouldShouldADialog() {

        FragmentManager supportFragmentManager = mock(FragmentManager.class);
        FragmentTransaction fragmentTransaction = mock(FragmentTransaction.class);

        when(householdListActivity.getSupportFragmentManager()).thenReturn(supportFragmentManager);
        when(supportFragmentManager.beginTransaction()).thenReturn(fragmentTransaction);

        submitDataHandler.displayUploadResult(new ArrayList<>());

        verify(fragmentTransaction, times(1)).add(any(HouseholdUploadResultDialog.class), eq(HouseholdUploadResultDialog.class.getSimpleName()));
        verify(fragmentTransaction, times(1)).commit();
    }
}