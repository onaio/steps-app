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
import static org.mockito.Mockito.verify;

import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class SelectedParticipantActionsHandlerTest extends StepsTestRunner {

    private final int MENU_ID = R.id.selected_participant_actions;
    @Mock
    private HouseholdActivity activityMock;
    @Mock
    private Household householdMock;
    private SelectedParticipantActionsHandler selectedParticipantActionsHandler;

    @Before
    public void Setup(){
        activityMock = mock(HouseholdActivity.class);
        householdMock = mock(Household.class);
        selectedParticipantActionsHandler = new SelectedParticipantActionsHandler(activityMock, householdMock);
    }

    @Test
    public void ShouldInactivateWhenMemberIsNotSelected(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.SELECTION_NOT_DONE);

        assertTrue(selectedParticipantActionsHandler.shouldDeactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyNotDone(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.NOT_DONE);

        assertFalse(selectedParticipantActionsHandler.shouldDeactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyDone(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.DONE);

        assertTrue(selectedParticipantActionsHandler.shouldDeactivate());
    }

    @Test
    public void ShouldActivateWhenSurveyIncomplete(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.INCOMPLETE);

        assertFalse(selectedParticipantActionsHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyDeferred(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.DEFERRED);

        assertFalse(selectedParticipantActionsHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyRefused(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.REFUSED);

        assertTrue(selectedParticipantActionsHandler.shouldDeactivate());
    }

    @Test
    public void ShouldHideItemWhenInactivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.when(activityMock.findViewById(MENU_ID)).thenReturn(viewMock);

        selectedParticipantActionsHandler.deactivate();

        verify(viewMock).setVisibility(View.GONE);
    }

    @Test
    public void ShouldShowItemWhenActivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.when(activityMock.findViewById(MENU_ID)).thenReturn(viewMock);

        selectedParticipantActionsHandler.activate();

        verify(viewMock).setVisibility(View.VISIBLE);
    }
}