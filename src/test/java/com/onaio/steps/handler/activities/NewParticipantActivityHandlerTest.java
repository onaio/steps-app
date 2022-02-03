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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.app.Activity;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.NewParticipantActivity;
import com.onaio.steps.activities.ParticipantActivity;
import com.onaio.steps.activities.ParticipantListActivity;
import com.onaio.steps.adapters.ParticipantAdapter;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;
import com.onaio.steps.model.RequestCode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewParticipantActivityHandlerTest extends StepsTestRunner {

    private ParticipantListActivity participantListActivity;
    private NewParticipantActivityHandler newParticipantActivityHandler;
    private Participant participantMock;
    private String date;
    private Participant participant;

    @Before
    public  void Setup() {
        participantMock = Mockito.mock(Participant.class);
        participantListActivity = Robolectric.buildActivity(ParticipantListActivity.class).create().get();
        newParticipantActivityHandler = new NewParticipantActivityHandler(participantListActivity);
    }

    @Test
    public void ShouldOpenActivityWhenProperMenuIdIsPassed(){
           assertTrue(newParticipantActivityHandler.shouldOpen(R.id.action_add_new_item));
    }

    @Test
    public void ShouldNotOpenForOtherMenuId(){
        assertFalse(newParticipantActivityHandler.shouldOpen(R.id.action_add_member));
    }

    @Test
    public void ShouldBeAbleToHandleRequestForProperRequestCode(){
        assertTrue(newParticipantActivityHandler.canHandleResult(RequestCode.NEW_PARTICIPANT.getCode()));
    }

    @Test
    public void ShouldNotHandleRequestForImproperRequestCode(){
        assertFalse(newParticipantActivityHandler.canHandleResult(RequestCode.NEW_MEMBER.getCode()));
    }

    @Test
    public void ShouldHandleResultAndStartParticipantActivityForResultCodeOk(){
        Intent intent = new Intent();
        date = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH).format(new Date());
        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.DONE, date);

        participant.save(new DatabaseHelper(participantListActivity));
        intent.putExtra(Constants.PARTICIPANT,participant);
        ParticipantAdapter participantAdapter = Mockito.mock(ParticipantAdapter.class);
        //Mockito.when(participantAdapter.getViewTypeCount()).thenReturn(1);
        ((RecyclerView) participantListActivity.findViewById(R.id.list)).setAdapter(participantAdapter);
        ShadowActivity stepsActivityShadow = shadowOf(participantListActivity);

        newParticipantActivityHandler.handleResult(intent, Activity.RESULT_OK);

        Mockito.verify(participantAdapter).reinitialize(Mockito.anyList());
        Mockito.verify(participantAdapter).notifyDataSetChanged();


        Intent newIntent = stepsActivityShadow.getNextStartedActivity();
        assertEquals(ParticipantActivity.class.getName(),newIntent.getComponent().getClassName());
        assertEquals(participant,newIntent.getSerializableExtra(Constants.PARTICIPANT));
    }

    @Test
    public void ShouldStartNewParticipantActivity() {

        newParticipantActivityHandler.open();

        ShadowActivity shadowActivity = shadowOf(participantListActivity);
        Intent nextStartedActivity = shadowActivity.getNextStartedActivity();
        assertEquals(NewParticipantActivity.class.getName(),nextStartedActivity.getComponent().getClassName());
    }

}
