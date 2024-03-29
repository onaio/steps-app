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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

public class SelectParticipantHandlerTest extends StepsTestRunner {

    @Mock
    private Household householdMock;
    private SelectParticipantHandler selectParticipantHandler;
    private DatabaseHelper dbMock;
    private CustomDialog dialogMock;
    private HouseholdActivity householdActivity;
    @Mock
    private android.app.Dialog androidDialogMock;

    @Before
    public void Setup(){
        householdMock = mock(Household.class);
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.SELECTION_NOT_DONE);
        Mockito.when(householdMock.getPhoneNumber()).thenReturn("8050342");
        Mockito.when(householdMock.getComments()).thenReturn("dummy comments");

        Intent intent = new Intent();
        intent.putExtra(Constants.HH_HOUSEHOLD, householdMock);
        householdActivity = Robolectric.buildActivity(HouseholdActivity.class, intent).create().get();
        dbMock = mock(DatabaseHelper.class);
        dialogMock = mock(CustomDialog.class);

        androidDialogMock = Mockito.mock(android.app.Dialog.class);
        selectParticipantHandler = new SelectParticipantHandlerStub(householdActivity, householdMock, dialogMock, dbMock, androidDialogMock,Mockito.mock(View.class));
    }

    @Test
    public void ShouldBeAbleToOpenWhenMenuIdMatches(){
        int MENU_ID = R.id.action_select_participant;
        assertTrue(selectParticipantHandler.shouldOpen(MENU_ID));
    }

    @Test
    public void ShouldNotBeAbleToOpenForDifferentMenuId(){
        assertFalse(selectParticipantHandler.shouldOpen(R.id.action_refused));
    }

    @Test
    public void ShouldNotifyUserBeforeSelection(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.SELECTION_NOT_DONE);
        Button buttonMock = Mockito.mock(Button.class);
        when(androidDialogMock.findViewById(R.id.confirm)).thenReturn(buttonMock);
        when(androidDialogMock.findViewById(R.id.cancel)).thenReturn(buttonMock);

        selectParticipantHandler.open();

        verify(androidDialogMock).show();
        verify(androidDialogMock).setCancelable(true);
        verify(androidDialogMock).requestWindowFeature(Window.FEATURE_NO_TITLE);
        verify(androidDialogMock).setContentView(R.layout.select_participant_dialog);
    }

    @Test
    public void ShouldInActivateWhenThereAreNoMembers(){
        Mockito.when(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).thenReturn(0);

        Assert.assertTrue(selectParticipantHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInActivateWhenHouseholdStatusIsIncomplete(){
        Mockito.when(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).thenReturn(1);
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.INCOMPLETE);

        Assert.assertTrue(selectParticipantHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInActivateWhenHouseholdStatusIsDone(){
        Mockito.when(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).thenReturn(1);
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.DONE);

        Assert.assertTrue(selectParticipantHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInActivateWhenHouseholdStatusIsDeferred(){
        Mockito.when(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).thenReturn(1);
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.DEFERRED);

        Assert.assertTrue(selectParticipantHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInActivateWhenHouseholdStatusIsRefused(){
        Mockito.when(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).thenReturn(1);
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.REFUSED);

        Assert.assertTrue(selectParticipantHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInActivateWhenHouseholdStatusIsNotDone(){
        Mockito.when(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).thenReturn(1);
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.NOT_DONE);

        Assert.assertTrue(selectParticipantHandler.shouldDeactivate());
    }

    @Test
    public void ShouldActivateWhenHouseholdStatusIsNotSelected(){
        Mockito.when(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).thenReturn(1);
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.SELECTION_NOT_DONE);

        Assert.assertFalse(selectParticipantHandler.shouldDeactivate());
    }

    @Test
    public void ShouldHideTheMenuItemWhenInactivated(){
        HouseholdActivity householdActivityMock = Mockito.mock(HouseholdActivity.class);
        SelectParticipantHandler handler = new SelectParticipantHandler(householdActivityMock,householdMock, dbMock, androidDialogMock);
        View viewMock = Mockito.mock(View.class);
        Mockito.when(householdActivityMock.findViewById(R.id.action_select_participant)).thenReturn(viewMock);

        handler.deactivate();

        Mockito.verify(viewMock).setVisibility(View.GONE);
    }

    @Test
    public void ShouldMakeTheMenuVisibleItemWhenActivated(){
        HouseholdActivity householdActivityMock = Mockito.mock(HouseholdActivity.class);
        SelectParticipantHandler handlerStub = new SelectParticipantHandler(householdActivityMock,householdMock, dbMock, androidDialogMock);
        View viewMock = Mockito.mock(View.class);
        Mockito.when(householdActivityMock.findViewById(R.id.action_select_participant)).thenReturn(viewMock);

        handlerStub.activate();

        Mockito.verify(viewMock).setVisibility(View.VISIBLE);
    }


    private class SelectParticipantHandlerStub extends SelectParticipantHandler{
        private View view;

        public SelectParticipantHandlerStub(AppCompatActivity activity, Household household, CustomDialog dialog, DatabaseHelper db, android.app.Dialog androidDialog, View view) {
            super(activity, household, db, androidDialog);
            this.view = view;
        }

        @Override
        protected View getView() {
            return view;
        }
    }

}