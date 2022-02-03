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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.ParticipantActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.ODKForm.ODKSavedForm;
import com.onaio.steps.model.Participant;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TakeSurveyForParticipantStrategyTest extends StepsTestRunner {

    private Participant participant;
    private TakeSurveyForParticipantStrategy takeSurveyForParticipantStrategy;

    @Before
    public void Setup(){
        participant = Mockito.mock(Participant.class);
        ParticipantActivity participantActivity = Mockito.mock(ParticipantActivity.class);
        takeSurveyForParticipantStrategy = new TakeSurveyForParticipantStrategy(participant, participantActivity);
    }

    @Test
    public void ShouldNotInactivateWhenMemberIsSelected(){
        Mockito.when(participant.getStatus()).thenReturn(InterviewStatus.NOT_DONE);
        assertFalse(takeSurveyForParticipantStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsDone(){
        Mockito.when(participant.getStatus()).thenReturn(InterviewStatus.DONE);
        assertTrue(takeSurveyForParticipantStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsDeferred(){
        Mockito.when(participant.getStatus()).thenReturn(InterviewStatus.DEFERRED);
        assertFalse(takeSurveyForParticipantStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsIncomplete(){
        Mockito.when(participant.getStatus()).thenReturn(InterviewStatus.INCOMPLETE);
        assertFalse(takeSurveyForParticipantStrategy.shouldInactivate());
    }


    @Test
    public void ShouldHandleResultForSurveyCompleted(){
        ODKSavedForm odkSavedForm = Mockito.mock(ODKSavedForm.class);
        Mockito.when(odkSavedForm.getStatus()).thenReturn(Constants.ODK_FORM_COMPLETE_STATUS);
        takeSurveyForParticipantStrategy.handleResult(odkSavedForm);
        Mockito.verify(participant).setStatus(InterviewStatus.DONE);
        Mockito.verify(participant).update(Mockito.any(DatabaseHelper.class));
    }

    @Test
    public void ShouldGetNameFormat(){
        String formNameFormat = "STEPS_Instrument_V3_1-%s";
        Mockito.when(participant.getParticipantID()).thenReturn("123-100-3");
        assertEquals("STEPS_Instrument_V3_1-123-100-3",takeSurveyForParticipantStrategy.getFormName(formNameFormat));

    }

}