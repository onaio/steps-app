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

package com.onaio.steps.handler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SelectedParticipantContainerHandlerTest extends StepsTestRunner {

    private Household householdMock;
    private HouseholdActivity householdActivity;
    private SelectedParticipantContainerHandler selectedParticipantContainerHandler;

    @Before
    public void setup(){
        householdMock = Mockito.mock(Household.class);
        householdActivity = Mockito.mock(HouseholdActivity.class);
        selectedParticipantContainerHandler = new SelectedParticipantContainerHandler(householdActivity, householdMock);
    }

    @Test
    public void ShouldNotInactivateWhenSurveyNotDone(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.NOT_DONE);

        assertFalse(selectedParticipantContainerHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInactivateWhenMemberIsNotSelected(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.SELECTION_NOT_DONE);

        assertTrue(selectedParticipantContainerHandler.shouldDeactivate());
    }

    @Test
    public void ShouldActivateWhenHouseholdStatusIsIncomplete(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.INCOMPLETE);

        assertFalse(selectedParticipantContainerHandler.shouldDeactivate());
    }

    @Test
    public void ShouldActivateWhenSurveyIsDeferred(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.DEFERRED);

        assertFalse(selectedParticipantContainerHandler.shouldDeactivate());
    }

    @Test
    public void ShouldActivateWhenSurveyIsRefused(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.REFUSED);

        assertTrue(selectedParticipantContainerHandler.shouldDeactivate());
    }

    @Test
    public void ShouldHideItemWhenInactivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.when(householdActivity.findViewById(R.id.selected_participant)).thenReturn(viewMock);

        selectedParticipantContainerHandler.deactivate();

        verify(viewMock).setVisibility(View.GONE);
    }

    @Test
    public void ShouldShowItemWhenActivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.when(householdActivity.findViewById(R.id.selected_participant)).thenReturn(viewMock);

       selectedParticipantContainerHandler.activate();

        verify(viewMock).setVisibility(View.VISIBLE);
    }



}