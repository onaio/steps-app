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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.app.Activity;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.activities.HouseholdListActivity;
import com.onaio.steps.activities.NewHouseholdActivity;
import com.onaio.steps.adapters.HouseholdAdapter;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.RequestCode;
import com.onaio.steps.model.ServerStatus;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowActivity;

public class NewHouseholdActivityHandlerTest extends StepsTestRunner {

    private HouseholdListActivity householdListActivity;
    private NewHouseholdActivityHandler handler;
    private CustomDialog dialogMock;
    private String PHONE_ID = "123";
    private String HOUSEHOLD_SEED = "100";

    @Before
    public void Setup(){
        householdListActivity = Robolectric.setupActivity(HouseholdListActivity.class);
        dialogMock = Mockito.mock(CustomDialog.class);
        handler = new NewHouseholdActivityHandler(householdListActivity, dialogMock);
    }

    @Test
    public void ShouldOpenWhenProperMenuItemIsClicked(){
        assertTrue(handler.shouldOpen(R.id.action_add_new_item));
    }

    @Test
    public void ShouldNotOpenWhenOtherMenuItemIsClicked(){
        assertFalse(handler.shouldOpen(R.id.action_settings));
    }

    @Test
    public void ShouldNotifyUserWhenPhoneIdIsNotSet(){
        KeyValueStore keyValueStoreMock = Mockito.mock(KeyValueStore.class);
        Mockito.when(keyValueStoreMock.getString(PHONE_ID)).thenReturn("");
        handler.open();

        Mockito.verify(dialogMock).notify(householdListActivity, CustomDialog.EmptyListener, R.string.phone_id_message_title, R.string.phone_id_message);
    }


    @Test
    public void ShouldNotifyUserWhenOnlyHouseholdSeedSet(){
        setValue(Constants.HH_HOUSEHOLD_SEED, HOUSEHOLD_SEED);

        handler.open();

        Mockito.verify(dialogMock).notify(householdListActivity, CustomDialog.EmptyListener, R.string.phone_id_message_title, R.string.phone_id_message);
    }

    @Test
    public void ShouldNotNotifyUserWhenPhoneIdAndHouseholdSeedSet(){
        setValue(Constants.HH_HOUSEHOLD_SEED, HOUSEHOLD_SEED);
        setValue(Constants.HH_PHONE_ID, PHONE_ID);

        handler.open();

        Mockito.verify(dialogMock,Mockito.never()).notify(householdListActivity, CustomDialog.EmptyListener, R.string.phone_id_message_title, R.string.phone_id_message);
    }

    @Test
    public void ShouldOpenNewHouseholdActivityWithDataWhenPhoneIdIsSet(){
        setValue(Constants.HH_PHONE_ID, PHONE_ID);
        setValue(Constants.HH_HOUSEHOLD_SEED, HOUSEHOLD_SEED);
        ShadowActivity stepsActivityShadow = shadowOf(householdListActivity);

        handler.open();

        Intent newIntent = stepsActivityShadow.getNextStartedActivityForResult().intent;
        assertTrue(newIntent.getComponent().getClassName().equals(NewHouseholdActivity.class.getName()));
        assertTrue(newIntent.getStringExtra(Constants.HH_PHONE_ID).equals(PHONE_ID));
    }

    @Test
    public void ShouldBeAbleToHandleResultForNewHouseholdRequestCode(){
        assertTrue(handler.canHandleResult(RequestCode.NEW_HOUSEHOLD.getCode()));
    }

    @Test
    public void ShouldNotBeAbleToHandleResultForOtherRequestCode(){
        assertFalse(handler.canHandleResult(RequestCode.EDIT_HOUSEHOLD.getCode()));
    }

    @Test
    public void ShouldHandleResultAndStartHouseholdActivityForResultCodeOk(){
        Intent intent = new Intent();
        Household name = new Household("name", "123321412312", InterviewStatus.SELECTION_NOT_DONE, "123", "testDeviceId","Dummy comments");
        name.setServerStatus(ServerStatus.NOT_SENT);
        name.save(new DatabaseHelper(householdListActivity));
        intent.putExtra(Constants.HH_HOUSEHOLD,name);
        HouseholdAdapter householdAdapterMock = Mockito.mock(HouseholdAdapter.class);
        //Mockito.when(householdAdapterMock.getViewTypeCount()).thenReturn(1);
        ((RecyclerView) householdListActivity.findViewById(R.id.list)).setAdapter(householdAdapterMock);
        ShadowActivity stepsActivityShadow = shadowOf(householdListActivity);

        handler.handleResult(intent, Activity.RESULT_OK);

        Mockito.verify(householdAdapterMock).reinitialize(Mockito.anyList());
        Mockito.verify(householdAdapterMock).notifyDataSetChanged();


        Intent newIntent = stepsActivityShadow.getNextStartedActivity();
        assertEquals(HouseholdActivity.class.getName(),newIntent.getComponent().getClassName());
        assertEquals(name,newIntent.getSerializableExtra(Constants.HH_HOUSEHOLD));
    }

    @Test
    public void ShouldNotHandleResultForOtherResultCode(){
        HouseholdAdapter householdAdapterMock = Mockito.mock(HouseholdAdapter.class);
        //Mockito.when(householdAdapterMock.getViewTypeCount()).thenReturn(1);
        ((RecyclerView) householdListActivity.findViewById(R.id.list)).setAdapter(householdAdapterMock);
        handler.handleResult(null, Activity.RESULT_CANCELED);

        Mockito.verify(householdAdapterMock,Mockito.never()).reinitialize(Mockito.anyList());
        Mockito.verify(householdAdapterMock,Mockito.never()).notifyDataSetChanged();
    }

    private void setValue(String key, String value) {
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(householdListActivity);
        keyValueStore.putString(key, value);
    }
}