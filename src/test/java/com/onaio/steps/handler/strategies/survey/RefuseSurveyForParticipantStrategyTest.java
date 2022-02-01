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

public class RefuseSurveyForParticipantStrategyTest extends StepsTestRunner {

    private Participant mock;
    private RefuseSurveyForParticipantStrategy refuseSurveyForParticipantStrategy;

    @Before
    public void Setup(){
        mock = Mockito.mock(Participant.class);
        ParticipantActivity participantActivity = Mockito.mock(ParticipantActivity.class);
        refuseSurveyForParticipantStrategy = new RefuseSurveyForParticipantStrategy(mock, participantActivity);
    }

    @Test
    public void ShouldNotInactivateWhenSurveyIsDone(){
        Mockito.when(mock.getStatus()).thenReturn(InterviewStatus.NOT_DONE);
        assertFalse(refuseSurveyForParticipantStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsDone(){
        Mockito.when(mock.getStatus()).thenReturn(InterviewStatus.DONE);
        assertTrue(refuseSurveyForParticipantStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsDeferred(){
        Mockito.when(mock.getStatus()).thenReturn(InterviewStatus.DEFERRED);
        assertFalse(refuseSurveyForParticipantStrategy.shouldInactivate());
    }

    @Test
    public void ShouldOpenAndUpdateStatus(){
        Mockito.when(mock.getStatus()).thenReturn(InterviewStatus.DEFERRED);
        refuseSurveyForParticipantStrategy.open();
        Mockito.verify(mock).setStatus(InterviewStatus.REFUSED);
        Mockito.verify(mock).update(Mockito.any(DatabaseHelper.class));
    }
}