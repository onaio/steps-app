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

import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.RequestCode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class EditHouseholdActivityHandlerTest {

    private final int MENU_ID = R.id.action_household_edit;
    @Mock
    private HouseholdActivity activityMock;
    @Mock
    private Household householdMock;
    private EditHouseholdActivityHandler editHouseholdActivityHandler;

    @Before
    public void Setup(){
        activityMock = mock(HouseholdActivity.class);
        householdMock = mock(Household.class);
        editHouseholdActivityHandler = new EditHouseholdActivityHandler(activityMock, householdMock);
    }

    @Test
    public void ShouldBeAbleToOpenWhenMenuIdMatches(){
        assertTrue(editHouseholdActivityHandler.shouldOpen(MENU_ID));
    }

    @Test
    public void ShouldNotBeAbleToOpenForDifferentMenuId(){
        assertFalse(editHouseholdActivityHandler.shouldOpen(R.id.action_refused));
    }

    @Test
    public void ShouldPopForConfirmationWhenOpened(){
        editHouseholdActivityHandler.open();

        verify(activityMock).startActivityForResult(Mockito.argThat(householdIntentMatcher()), Mockito.eq(RequestCode.EDIT_HOUSEHOLD.getCode()));
    }

    private ArgumentMatcher<Intent> householdIntentMatcher() {
        return new ArgumentMatcher<Intent>() {
            @Override
            public boolean matches(Object argument) {
                Intent intent = (Intent) argument;
                Household householdActual = (Household) intent.getSerializableExtra(Constants.HH_HOUSEHOLD);
                assertEquals(householdMock,householdActual);
                return true;
            }
        };
    }

    @Test
    public void ShouldBeAbleToHandleResultForEditHouseholdIdentifier(){
        assertTrue(editHouseholdActivityHandler.canHandleResult(RequestCode.EDIT_HOUSEHOLD.getCode()));
    }

    @Test
    public void ShouldNotBeAbleToHandleResultForOtherIdentifier(){
        assertFalse(editHouseholdActivityHandler.canHandleResult(RequestCode.NEW_HOUSEHOLD.getCode()));
    }

    @Test
    public void ShouldFinishTheActivityWhenResultHandledWithOkResult(){
        editHouseholdActivityHandler.handleResult(new Intent(),Activity.RESULT_OK);

        verify(activityMock).finish();
    }

    @Test
    public void ShouldNotFinishTheActivityWhenResultHandledForOtherResult(){
        editHouseholdActivityHandler.handleResult(new Intent(), Activity.RESULT_CANCELED);

        verify(activityMock,never()).finish();
    }

}