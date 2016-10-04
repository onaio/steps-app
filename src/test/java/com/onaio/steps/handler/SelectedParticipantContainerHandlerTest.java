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

import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SelectedParticipantContainerHandlerTest {

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
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.NOT_DONE);

        assertFalse(selectedParticipantContainerHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenMemberIsNotSelected(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.NOT_SELECTED);

        assertTrue(selectedParticipantContainerHandler.shouldInactivate());
    }

    @Test
    public void ShouldActivateWhenHouseholdStatusIsIncomplete(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.INCOMPLETE);

        assertFalse(selectedParticipantContainerHandler.shouldInactivate());
    }

    @Test
    public void ShouldActivateWhenSurveyIsDeferred(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.DEFERRED);

        assertFalse(selectedParticipantContainerHandler.shouldInactivate());
    }

    @Test
    public void ShouldActivateWhenSurveyIsRefused(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.REFUSED);

        assertTrue(selectedParticipantContainerHandler.shouldInactivate());
    }

    @Test
    public void ShouldHideItemWhenInactivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(householdActivity.findViewById(R.id.selected_participant)).toReturn(viewMock);

        selectedParticipantContainerHandler.inactivate();

        verify(viewMock).setVisibility(View.GONE);
    }

    @Test
    public void ShouldShowItemWhenActivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(householdActivity.findViewById(R.id.selected_participant)).toReturn(viewMock);

       selectedParticipantContainerHandler.activate();

        verify(viewMock).setVisibility(View.VISIBLE);
    }



}