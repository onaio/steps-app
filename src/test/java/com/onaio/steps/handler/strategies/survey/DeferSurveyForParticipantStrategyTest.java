package com.onaio.steps.handler.strategies.survey;

import com.onaio.steps.activities.ParticipantActivity;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class DeferSurveyForParticipantStrategyTest {

    private Participant participant;
    private ParticipantActivity participantActivity;
    private DeferSurveyForParticipantStrategy deferSurveyForParticipantStrategy;

    @Before
    public void Setup(){
        participant = Mockito.mock(Participant.class);
        participantActivity = Mockito.mock(ParticipantActivity.class);
        deferSurveyForParticipantStrategy = new DeferSurveyForParticipantStrategy(participant, participantActivity);
    }

    @Test
    public void ShouldNotInactivateWhenSurveyIsDone(){
        Mockito.stub(participant.getStatus()).toReturn(InterviewStatus.NOT_DONE);
        assertFalse(deferSurveyForParticipantStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsDone(){
        Mockito.stub(participant.getStatus()).toReturn(InterviewStatus.DONE);
        assertTrue(deferSurveyForParticipantStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsRefused(){
        Mockito.stub(participant.getStatus()).toReturn(InterviewStatus.REFUSED);
        assertTrue(deferSurveyForParticipantStrategy.shouldInactivate());
    }

    @Test
    public void ShouldOpenAndUpdateStatus(){
        Mockito.stub(participant.getStatus()).toReturn(InterviewStatus.REFUSED);
        deferSurveyForParticipantStrategy.open();
        Mockito.verify(participant).setStatus(InterviewStatus.DEFERRED);
        Mockito.verify(participant).update(Mockito.any(DatabaseHelper.class));
    }
}