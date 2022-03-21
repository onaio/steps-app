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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.ODKForm.ODKBlankForm;
import com.onaio.steps.model.ServerStatus;
import com.onaio.steps.orchestrators.flows.FlowType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
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

    @SuppressWarnings("rawtypes")
    @Test
    public void testPrepareAvailableFormListShouldVerifyDataPopulation() throws RemoteException {
        SettingsActivity spySettingActivity = Mockito.spy(settingsActivity);

        ContentResolver contentResolver = mock(ContentResolver.class);
        ContentProviderClient contentProviderClient = mock(ContentProviderClient.class);
        Cursor cursor = mock(Cursor.class);
        AutoCompleteTextView actvFormId = mock(AutoCompleteTextView.class);

        when(spySettingActivity.findViewById(R.id.form_id_household)).thenReturn(actvFormId);
        when(spySettingActivity.findViewById(R.id.form_id_participant)).thenReturn(actvFormId);
        when(spySettingActivity.getContentResolver()).thenReturn(contentResolver);
        when(contentResolver.acquireContentProviderClient(any(Uri.class))).thenReturn(contentProviderClient);
        when(contentProviderClient.query(any(Uri.class), nullable(String[].class), nullable(String.class), nullable(String[].class), nullable(String.class))).thenReturn(cursor);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.getColumnIndex("_id")).thenReturn(0);
        when(cursor.getColumnIndex("jrFormId")).thenReturn(1);
        when(cursor.getColumnIndex("displayName")).thenReturn(2);
        when(cursor.getColumnIndex("jrVersion")).thenReturn(3);
        when(cursor.getColumnIndex("formMediaPath")).thenReturn(4);
        when(cursor.getString(0)).thenReturn("1");
        when(cursor.getString(1)).thenReturn("test_form");
        when(cursor.getString(2)).thenReturn("Test Form");
        when(cursor.getString(3)).thenReturn("0");
        when(cursor.getString(4)).thenReturn("media");

        spySettingActivity.prepareAvailableFormList();

        ArgumentCaptor<ArrayAdapter> acArrayAdapter = ArgumentCaptor.forClass(ArrayAdapter.class);
        ArgumentCaptor<AdapterView.OnItemClickListener> acClickListener = ArgumentCaptor.forClass(AdapterView.OnItemClickListener.class);

        verify(actvFormId, times(2)).setThreshold(Mockito.eq(1));
        verify(actvFormId, times(2)).setAdapter(acArrayAdapter.capture());
        verify(actvFormId, times(2)).setOnItemClickListener(acClickListener.capture());

        ODKBlankForm odkBlankForm = (ODKBlankForm) acArrayAdapter.getValue().getItem(0);

        assertEquals("1", odkBlankForm.getId());
        assertEquals("test_form", odkBlankForm.getJrFormId());
        assertEquals("Test Form", odkBlankForm.getDisplayName());
        assertEquals("0", odkBlankForm.getJrVersion());
        assertEquals("media", odkBlankForm.getFormMediaPath());

        AdapterView adapterView = mock(AdapterView.class);
        when(adapterView.getItemAtPosition(0)).thenReturn(odkBlankForm);

        acClickListener.getValue().onItemClick(adapterView, mock(View.class), 0, 0);

        verify(actvFormId, times(1)).setText(Mockito.eq(odkBlankForm.getJrFormId()));
        verify(actvFormId, times(1)).setSelection(Mockito.eq(actvFormId.length()));
    }
}