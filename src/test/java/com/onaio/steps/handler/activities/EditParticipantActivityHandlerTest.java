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

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.EditParticipantActivity;
import com.onaio.steps.activities.ParticipantActivity;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;
import com.onaio.steps.model.RequestCode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

public class EditParticipantActivityHandlerTest extends StepsTestRunner {

    private ParticipantActivity participantActivity;
    private Participant participant;
    private EditParticipantActivityHandler editParticipantActivityHandler;

    @Before
    public void setup(){
        participantActivity = Mockito.mock(ParticipantActivity.class);
        participant = Mockito.mock(Participant.class);
        editParticipantActivityHandler = new EditParticipantActivityHandler(participantActivity, participant);
    }

    @Test
    public void ShouldBeAbleToOpenEditParticipantActivityWhenMenuIdMatches(){
        assertTrue(editParticipantActivityHandler.shouldOpen(R.id.action_edit));
    }

    @Test
    public void ShouldNotBeAbleToOpenEditParticipantActivityForOtherMenuId(){
        assertFalse(editParticipantActivityHandler.shouldOpen(R.id.action_settings));
    }

    @Test
    public void ShouldCheckWhetherResultForProperRequestCodeCanBeHandled(){
        assertTrue(editParticipantActivityHandler.canHandleResult(RequestCode.EDIT_PARTICIPANT.getCode()));
    }

    @Test
    public void ShouldCheckWhetherResultForOtherRequestCodeCanNotBeHandled(){
        assertFalse(editParticipantActivityHandler.canHandleResult(RequestCode.NEW_MEMBER.getCode()));
    }

    @Test
    public void ShouldInactivateWhenParticipantSurveyIsDone(){
        Menu menuMock = Mockito.mock(Menu.class);
        Mockito.when(participant.getStatus()).thenReturn(InterviewStatus.DONE);
        Assert.assertTrue(editParticipantActivityHandler.withMenu(menuMock).shouldDeactivate());
    }

    @Test
    public void ShouldInactivateWhenParticipantSurveyIsRefused(){
        Menu menuMock = Mockito.mock(Menu.class);
        Mockito.when(participant.getStatus()).thenReturn(InterviewStatus.REFUSED);
        Assert.assertTrue(editParticipantActivityHandler.withMenu(menuMock).shouldDeactivate());
    }

    @Test
    public void ShouldInactivateWhenParticipantSurveyIsIncomplete(){
        Menu menuMock = Mockito.mock(Menu.class);
        Mockito.when(participant.getStatus()).thenReturn(InterviewStatus.INCOMPLETE);
        Assert.assertTrue(editParticipantActivityHandler.withMenu(menuMock).shouldDeactivate());
    }

    @Test
    public void ShouldNotInactivateWhenParticipantIsSelected(){
        Menu menuMock = Mockito.mock(Menu.class);
        Mockito.when(participant.getStatus()).thenReturn(InterviewStatus.SELECTION_NOT_DONE);
        Assert.assertFalse(editParticipantActivityHandler.withMenu(menuMock).shouldDeactivate());
    }

    @Test
    public void ShouldBeAbleToActivateEditOptionInMenuItem(){
        Menu menuMock = Mockito.mock(Menu.class);
        MenuItem menuItemMock = Mockito.mock(MenuItem.class);
        Mockito.when(menuMock.findItem(R.id.action_edit)).thenReturn(menuItemMock);

        editParticipantActivityHandler.withMenu(menuMock).activate();

        Mockito.verify(menuItemMock).setEnabled(true);
    }

    @Test
    public void ShouldBeAbleToInactivateEditOptionInMenuItem() {
        Menu menuMock = Mockito.mock(Menu.class);
        MenuItem menuItemMock = Mockito.mock(MenuItem.class);
        Mockito.when(menuMock.findItem(R.id.action_edit)).thenReturn(menuItemMock);

        editParticipantActivityHandler.withMenu(menuMock).deactivate();

        Mockito.verify(menuItemMock).setEnabled(false);
    }


    @Test
    public void ShouldOpenWhenMemberIsNotNull(){
        editParticipantActivityHandler.open();
        Mockito.verify(participantActivity).startActivityForResult(Mockito.argThat(matchIntent()), Mockito.eq(RequestCode.EDIT_PARTICIPANT.getCode()));
    }

    private ArgumentMatcher<Intent> matchIntent() {
        return intent -> {
            Assert.assertEquals(EditParticipantActivity.class.getName(),intent.getComponent().getClassName());
            return true;
        };
    }

    @Test
    public void ShouldHandleResultForResultOkCode(){
        Intent intentMock = Mockito.mock(Intent.class);
        editParticipantActivityHandler.handleResult(intentMock, Activity.RESULT_OK);
        Mockito.verify(participantActivity).finish();
    }

}