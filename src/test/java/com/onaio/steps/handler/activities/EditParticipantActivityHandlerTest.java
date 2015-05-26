package com.onaio.steps.handler.activities;


import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.onaio.steps.R;
import com.onaio.steps.activities.EditParticipantActivity;
import com.onaio.steps.activities.ParticipantActivity;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;
import com.onaio.steps.model.RequestCode;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class EditParticipantActivityHandlerTest {

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
        Mockito.stub(participant.getStatus()).toReturn(InterviewStatus.DONE);
        Assert.assertTrue(editParticipantActivityHandler.withMenu(menuMock).shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenParticipantSurveyIsRefused(){
        Menu menuMock = Mockito.mock(Menu.class);
        Mockito.stub(participant.getStatus()).toReturn(InterviewStatus.REFUSED);
        Assert.assertTrue(editParticipantActivityHandler.withMenu(menuMock).shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenParticipantSurveyIsIncomplete(){
        Menu menuMock = Mockito.mock(Menu.class);
        Mockito.stub(participant.getStatus()).toReturn(InterviewStatus.INCOMPLETE);
        Assert.assertTrue(editParticipantActivityHandler.withMenu(menuMock).shouldInactivate());
    }

    @Test
    public void ShouldNotInactivateWhenParticipantIsSelected(){
        Menu menuMock = Mockito.mock(Menu.class);
        Mockito.stub(participant.getStatus()).toReturn(InterviewStatus.NOT_SELECTED);
        Assert.assertFalse(editParticipantActivityHandler.withMenu(menuMock).shouldInactivate());
    }

    @Test
    public void ShouldBeAbleToActivateEditOptionInMenuItem(){
        Menu menuMock = Mockito.mock(Menu.class);
        MenuItem menuItemMock = Mockito.mock(MenuItem.class);
        Mockito.stub(menuMock.findItem(R.id.action_edit)).toReturn(menuItemMock);

        editParticipantActivityHandler.withMenu(menuMock).activate();

        Mockito.verify(menuItemMock).setEnabled(true);
    }

    @Test
    public void ShouldBeAbleToInactivateEditOptionInMenuItem() {
        Menu menuMock = Mockito.mock(Menu.class);
        MenuItem menuItemMock = Mockito.mock(MenuItem.class);
        Mockito.stub(menuMock.findItem(R.id.action_edit)).toReturn(menuItemMock);

        editParticipantActivityHandler.withMenu(menuMock).inactivate();

        Mockito.verify(menuItemMock).setEnabled(false);
    }


    @Test
    public void ShouldOpenWhenMemberIsNotNull(){
        editParticipantActivityHandler.open();
        Mockito.verify(participantActivity).startActivityForResult(Mockito.argThat(matchIntent()), Mockito.eq(RequestCode.EDIT_PARTICIPANT.getCode()));
    }

    private ArgumentMatcher<Intent> matchIntent() {
        return new ArgumentMatcher<Intent>() {
            @Override
            public boolean matches(Object argument) {
                Intent intent = (Intent) argument;
                Assert.assertEquals(EditParticipantActivity.class.getName(),intent.getComponent().getClassName());
                return true;
            }
        };
    }

    @Test
    public void ShouldHandleResultForResultOkCode(){
        Intent intentMock = Mockito.mock(Intent.class);
        editParticipantActivityHandler.handleResult(intentMock, Activity.RESULT_OK);
        Mockito.verify(participantActivity).finish();
    }

}