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
public class RefuseSurveyForParticipantStrategyTest {

    private Participant mock;
    private ParticipantActivity participantActivity;
    private RefuseSurveyForParticipantStrategy refuseSurveyForParticipantStrategy;

    @Before
    public void Setup(){
        mock = Mockito.mock(Participant.class);
        participantActivity = Mockito.mock(ParticipantActivity.class);
        refuseSurveyForParticipantStrategy = new RefuseSurveyForParticipantStrategy(mock, participantActivity);
    }

    @Test
    public void ShouldNotInactivateWhenSurveyIsDone(){
        Mockito.stub(mock.getStatus()).toReturn(InterviewStatus.NOT_DONE);
        assertFalse(refuseSurveyForParticipantStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsDone(){
        Mockito.stub(mock.getStatus()).toReturn(InterviewStatus.DONE);
        assertTrue(refuseSurveyForParticipantStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsDeferred(){
        Mockito.stub(mock.getStatus()).toReturn(InterviewStatus.DEFERRED);
        assertFalse(refuseSurveyForParticipantStrategy.shouldInactivate());
    }

    @Test
    public void ShouldOpenAndUpdateStatus(){
        Mockito.stub(mock.getStatus()).toReturn(InterviewStatus.DEFERRED);
        refuseSurveyForParticipantStrategy.open();
        Mockito.verify(mock).setStatus(InterviewStatus.REFUSED);
        Mockito.verify(mock).update(Mockito.any(DatabaseHelper.class));
    }
}