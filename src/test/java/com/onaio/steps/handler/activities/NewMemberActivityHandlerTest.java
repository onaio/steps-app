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


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;

import com.onaio.steps.R;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.activities.NewMemberActivity;
import com.onaio.steps.adapters.MemberAdapter;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.RequestCode;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;

public class NewMemberActivityHandlerTest {
    private DatabaseHelper dbMock;
    HouseholdActivity householdActivityMock;
    Household householdMock;
    NewMemberActivityHandler newMemberActivityHandler;
    @Mock
    private MemberAdapter memberAdapterMock;
    private CustomDialog customDialogMock;


    @Before
    public  void Setup() {
        dbMock = Mockito.mock(DatabaseHelper.class);
        householdActivityMock = Mockito.mock(HouseholdActivity.class);
        householdMock = Mockito.mock(Household.class);
        memberAdapterMock = Mockito.mock(MemberAdapter.class);
        customDialogMock = Mockito.mock(CustomDialog.class);
        newMemberActivityHandler = new NewMemberActivityHandler(householdMock,householdActivityMock, memberAdapterMock, dbMock);
    }

    @Test
    public void ShouldOpenActivityWhenProperMenuIdIsPassedAndWhenSurveyIsNotRefused(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.NOT_DONE);
        assertTrue(newMemberActivityHandler.shouldOpen(R.id.action_add_member));
    }

    @Test
    public void ShouldNotOpenForOtherMenuIdAndForRefusedState(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.REFUSED);
        assertFalse(newMemberActivityHandler.shouldOpen(R.id.action_deferred));
    }

    @Test
    public void ShouldStartNewMemberActivityIfHouseholdIsNotNullAndHouseholdSurveyIsNotSelected() {
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.SELECTION_NOT_DONE);

        newMemberActivityHandler.open();

        Mockito.verify(householdActivityMock).startActivityForResult(Mockito.argThat(matchIntent()), Mockito.eq(RequestCode.NEW_MEMBER.getCode()));
    }

    @Test
    public void ShouldNotStartNewMemberActivityIfHouseholdIsNull() {
        NewMemberActivityHandler memberActivityHandler = new NewMemberActivityHandler(null,householdActivityMock, memberAdapterMock, dbMock);

        memberActivityHandler.open();

        Mockito.verify(householdActivityMock,Mockito.never()).startActivityForResult(Mockito.argThat(matchIntent()), Mockito.eq(RequestCode.NEW_MEMBER.getCode()));
    }

    @Test
    public void ShouldStartNewMemberActivityIfHouseholdIsNotNull() {
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.NOT_DONE);

        newMemberActivityHandler.open();

        Mockito.verify(householdActivityMock).startActivityForResult(Mockito.argThat(matchIntent()), Mockito.eq(RequestCode.NEW_MEMBER.getCode()));

    }

    @Test
    public void ShouldUpdateHouseholdForResultCodeOk(){
        Cursor cursorMock = Mockito.mock(Cursor.class);
        Mockito.when(dbMock.exec(Mockito.anyString())).thenReturn(cursorMock);
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.NOT_DONE);

        newMemberActivityHandler.handleResult(null, Activity.RESULT_OK);

        Mockito.verify(householdMock).setSelectedMemberId(null);
        Mockito.verify(householdMock).update(Mockito.any(DatabaseHelper.class));
    }

    @Test
    public void ShouldNotReInitialiseViewForOtherResultCode(){
        newMemberActivityHandler.handleResult(null, Activity.RESULT_CANCELED);

        Mockito.verify(memberAdapterMock,Mockito.never()).reinitialize(Mockito.anyList());
        Mockito.verify(memberAdapterMock,Mockito.never()).notifyDataSetChanged();
    }

    @Test
    public void ShouldBeAbleToHandleResultForProperRequestCode(){
        assertTrue(newMemberActivityHandler.canHandleResult(RequestCode.NEW_MEMBER.getCode()));
    }


    @Test
    public void ShouldNotBeAbleToHandleResultForOtherRequestCode(){
        assertTrue(newMemberActivityHandler.canHandleResult(RequestCode.NEW_MEMBER.getCode()));
    }

    @Test
    public void ShouldInactivateWhenHouseholdIsSurveyed(){
        when(householdMock.getSelectedMemberId()).thenReturn("");
        when(householdMock.getStatus()).thenReturn(InterviewStatus.DONE);
        Assert.assertTrue(newMemberActivityHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInactivateWhenHouseholdSurveyIsIncomplete(){
        when(householdMock.getSelectedMemberId()).thenReturn("");
        when(householdMock.getStatus()).thenReturn(InterviewStatus.INCOMPLETE);
        Assert.assertTrue(newMemberActivityHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsRefused(){
        when(householdMock.getSelectedMemberId()).thenReturn("");
        when(householdMock.getStatus()).thenReturn(InterviewStatus.REFUSED);
        Assert.assertTrue(newMemberActivityHandler.shouldDeactivate());
    }

    @Test
    public void ShouldDisableItemWhenInactivated(){
        View viewMock = Mockito.mock(Button.class);
        when(householdActivityMock.findViewById(R.id.action_add_member)).thenReturn(viewMock);

        newMemberActivityHandler.deactivate();

        verify(viewMock).setVisibility(View.GONE);
    }

    @Test
    public void ShouldShowItemWhenActivated(){
        View viewMock = Mockito.mock(Button.class);
        when(householdActivityMock.findViewById(R.id.action_add_member)).thenReturn(viewMock);

        newMemberActivityHandler.activate();

        verify(viewMock).setVisibility(View.VISIBLE);
    }

    private ArgumentMatcher<Intent> matchIntent() {
        return intent -> {
            Household actualHousehold = (Household) intent.getSerializableExtra(Constants.HH_HOUSEHOLD);
            Assert.assertEquals(householdMock, actualHousehold);
            Assert.assertEquals(NewMemberActivity.class.getName(),intent.getComponent().getClassName());
            return true;
        };
    }



}