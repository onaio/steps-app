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

import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

import android.content.Intent;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.ParticipantActivity;
import com.onaio.steps.activities.ParticipantListActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ParticipantActivityHandlerTest extends StepsTestRunner {

    private ParticipantListActivity participantListActivity;
    private Participant participant;
    private ParticipantActivityHandler participantActivityHandler;
    private String date;

    @Before
    public void setup(){
        date = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH).format(new Date());
        participantListActivity = Robolectric.setupActivity(ParticipantListActivity.class);
        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.DONE, date);
        participantActivityHandler = new ParticipantActivityHandler(participantListActivity, participant);
    }

    @Test
    public void ShouldOpenParticipantActivity(){
        participantActivityHandler.open();

        ShadowActivity shadowActivity = shadowOf(participantListActivity);
        Intent nextStartedActivity = shadowActivity.getNextStartedActivity();
        assertEquals(ParticipantActivity.class.getName(),nextStartedActivity.getComponent().getClassName());
        assertEquals(participant,nextStartedActivity.getSerializableExtra(Constants.PARTICIPANT));
    }
}