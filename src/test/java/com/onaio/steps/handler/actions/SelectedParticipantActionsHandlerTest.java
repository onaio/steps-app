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

import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SelectedParticipantActionsHandlerTest {

    private final int MENU_ID = R.id.selected_participant_actions;
    @Mock
    private HouseholdActivity activityMock;
    @Mock
    private Household householdMock;
    @Mock
    private CustomDialog dialogMock;
    private SelectedParticipantActionsHandler selectedParticipantActionsHandler;

    @Before
    public void Setup(){
        activityMock = mock(HouseholdActivity.class);
        householdMock = mock(Household.class);
        dialogMock = mock(CustomDialog.class);
        selectedParticipantActionsHandler = new SelectedParticipantActionsHandler(activityMock, householdMock);
    }

    @Test
    public void ShouldInactivateWhenMemberIsNotSelected(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.NOT_SELECTED);

        assertTrue(selectedParticipantActionsHandler.shouldInactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyNotDone(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.NOT_DONE);

        assertFalse(selectedParticipantActionsHandler.shouldInactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyDone(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.DONE);

        assertTrue(selectedParticipantActionsHandler.shouldInactivate());
    }

    @Test
    public void ShouldActivateWhenSurveyIncomplete(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.INCOMPLETE);

        assertFalse(selectedParticipantActionsHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyDeferred(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.DEFERRED);

        assertFalse(selectedParticipantActionsHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyRefused(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.REFUSED);

        assertTrue(selectedParticipantActionsHandler.shouldInactivate());
    }

    @Test
    public void ShouldHideItemWhenInactivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(activityMock.findViewById(MENU_ID)).toReturn(viewMock);

        selectedParticipantActionsHandler.inactivate();

        verify(viewMock).setVisibility(View.GONE);
    }

    @Test
    public void ShouldShowItemWhenActivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(activityMock.findViewById(MENU_ID)).toReturn(viewMock);

        selectedParticipantActionsHandler.activate();

        verify(viewMock).setVisibility(View.VISIBLE);
    }
}