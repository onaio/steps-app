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

package com.onaio.steps.handler.strategies.survey;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.ParticipantActivity;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DeferSurveyForParticipantStrategyTest extends StepsTestRunner {

    private Participant participant;
    private DeferSurveyForParticipantStrategy deferSurveyForParticipantStrategy;

    @Before
    public void Setup(){
        participant = Mockito.mock(Participant.class);
        ParticipantActivity participantActivity = Mockito.mock(ParticipantActivity.class);
        deferSurveyForParticipantStrategy = new DeferSurveyForParticipantStrategy(participant, participantActivity);
    }

    @Test
    public void ShouldNotInactivateWhenSurveyIsDone(){
        Mockito.when(participant.getStatus()).thenReturn(InterviewStatus.NOT_DONE);
        assertFalse(deferSurveyForParticipantStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsDone(){
        Mockito.when(participant.getStatus()).thenReturn(InterviewStatus.DONE);
        assertTrue(deferSurveyForParticipantStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsRefused(){
        Mockito.when(participant.getStatus()).thenReturn(InterviewStatus.REFUSED);
        assertTrue(deferSurveyForParticipantStrategy.shouldInactivate());
    }

    @Test
    public void ShouldOpenAndUpdateStatus(){
        Mockito.when(participant.getStatus()).thenReturn(InterviewStatus.REFUSED);
        deferSurveyForParticipantStrategy.open();
        Mockito.verify(participant).setStatus(InterviewStatus.DEFERRED);
        Mockito.verify(participant).update(Mockito.any(DatabaseHelper.class));
    }
}