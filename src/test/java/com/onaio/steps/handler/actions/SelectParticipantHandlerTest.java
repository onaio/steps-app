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

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SelectParticipantHandlerTest {

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
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.SELECTION_NOT_DONE);
        Mockito.stub(householdMock.getPhoneNumber()).toReturn("8050342");
        Mockito.stub(householdMock.getComments()).toReturn("dummy comments");

        Intent intent = new Intent();
        intent.putExtra(Constants.HH_HOUSEHOLD, householdMock);
        householdActivity = Robolectric.buildActivity(HouseholdActivity.class).withIntent(intent).create().get();
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
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.SELECTION_NOT_DONE);
        Button buttonMock = Mockito.mock(Button.class);
        stub(androidDialogMock.findViewById(R.id.confirm)).toReturn(buttonMock);
        stub(androidDialogMock.findViewById(R.id.cancel)).toReturn(buttonMock);

        selectParticipantHandler.open();

        verify(androidDialogMock).show();
        verify(androidDialogMock).setCancelable(true);
        verify(androidDialogMock).requestWindowFeature(Window.FEATURE_NO_TITLE);
        verify(androidDialogMock).setContentView(R.layout.select_participant_dialog);
    }

    @Test
    public void ShouldInActivateWhenThereAreNoMembers(){
        Mockito.stub(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).toReturn(0);

        Assert.assertTrue(selectParticipantHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInActivateWhenHouseholdStatusIsIncomplete(){
        Mockito.stub(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).toReturn(1);
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.INCOMPLETE);

        Assert.assertTrue(selectParticipantHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInActivateWhenHouseholdStatusIsDone(){
        Mockito.stub(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).toReturn(1);
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.DONE);

        Assert.assertTrue(selectParticipantHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInActivateWhenHouseholdStatusIsDeferred(){
        Mockito.stub(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).toReturn(1);
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.DEFERRED);

        Assert.assertTrue(selectParticipantHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInActivateWhenHouseholdStatusIsRefused(){
        Mockito.stub(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).toReturn(1);
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.REFUSED);

        Assert.assertTrue(selectParticipantHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInActivateWhenHouseholdStatusIsNotDone(){
        Mockito.stub(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).toReturn(1);
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.NOT_DONE);

        Assert.assertTrue(selectParticipantHandler.shouldDeactivate());
    }

    @Test
    public void ShouldActivateWhenHouseholdStatusIsNotSelected(){
        Mockito.stub(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).toReturn(1);
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.SELECTION_NOT_DONE);

        Assert.assertFalse(selectParticipantHandler.shouldDeactivate());
    }

    @Test
    public void ShouldHideTheMenuItemWhenInactivated(){
        HouseholdActivity householdActivityMock = Mockito.mock(HouseholdActivity.class);
        SelectParticipantHandler handler = new SelectParticipantHandler(householdActivityMock,householdMock, dbMock, androidDialogMock);
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(householdActivityMock.findViewById(R.id.action_select_participant)).toReturn(viewMock);

        handler.deactivate();

        Mockito.verify(viewMock).setVisibility(View.GONE);
    }

    @Test
    public void ShouldMakeTheMenuVisibleItemWhenActivated(){
        HouseholdActivity householdActivityMock = Mockito.mock(HouseholdActivity.class);
        SelectParticipantHandler handlerStub = new SelectParticipantHandler(householdActivityMock,householdMock, dbMock, androidDialogMock);
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(householdActivityMock.findViewById(R.id.action_select_participant)).toReturn(viewMock);

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