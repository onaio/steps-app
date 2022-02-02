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

package com.onaio.steps.activities;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.handler.actions.DeferredHandler;
import com.onaio.steps.handler.factories.ParticipantActivityFactory;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.fakes.RoboMenu;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.util.ReflectionHelpers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ParticipantActivityTest extends StepsTestRunner {

    private String date;
    private Participant participant;
    private Intent intent;

    @Before
    public void setup() {
        intent = new Intent();
        date = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH).format(new Date());
    }

    @Test
    public void ShouldBeAbleToStyleActionBarAndCustomizeOptionStrings() {
        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.DONE, date);
        intent.putExtra(Constants.PARTICIPANT, participant);
        ParticipantActivity participantActivity = getParticipantActivity(intent);

        Button cancelButton = participantActivity.findViewById(R.id.action_cancel_participant);
        Button takeSurveyButton = participantActivity.findViewById(R.id.action_take_survey);
        Button deferredButton = participantActivity.findViewById(R.id.action_deferred);

        assertEquals(participant.getFormattedName(), participantActivity.getSupportActionBar().getTitle());
        assertEquals("ENTER DATA NOW", takeSurveyButton.getText().toString());
        assertEquals("ENTER DATA LATER", deferredButton.getText().toString());
        assertEquals(View.GONE, cancelButton.getVisibility());
    }

    @Test
    public void ShouldBeAbleToPopulateViewWithParticipantWithDoneStatus() {
        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.DONE, date);
        intent.putExtra(Constants.PARTICIPANT, participant);
        ParticipantActivity participantActivity = getParticipantActivity(intent);

        TextView participantName = participantActivity.findViewById(R.id.selected_participant_name);
        TextView participantDetails = participantActivity.findViewById(R.id.selected_participant_details);
        String participantFormattedName = participant.getFormattedName() + " (" + participantActivity.getString(R.string.pid) + " " + participant.getParticipantID() + " )";
        TextView viewById = participantActivity.findViewById(R.id.survey_message);

        assertNotNull(participantName);
        assertNotNull(participantDetails);
        assertEquals(participantFormattedName, participantName.getText().toString());
        assertEquals(participant.getFormattedDetail(participantActivity), participantDetails.getText().toString());
        assertEquals(participantActivity.getString(R.string.interview_done_message),viewById.getText());
    }

    @Test
    public void ShouldBeAbleToPopulateViewWithParticipantWithRefused() {
        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.REFUSED, date);
        intent.putExtra(Constants.PARTICIPANT, participant);
        ParticipantActivity participantActivity = getParticipantActivity(intent);

        TextView participantName = participantActivity.findViewById(R.id.selected_participant_name);
        TextView participantDetails = participantActivity.findViewById(R.id.selected_participant_details);
        String participantFormattedName = participant.getFormattedName() + " (" + participantActivity.getString(R.string.pid) + " " + participant.getParticipantID() + " )";
        TextView viewById = participantActivity.findViewById(R.id.survey_message);

        assertNotNull(participantName);
        assertNotNull(participantDetails);
        assertEquals(participantFormattedName, participantName.getText().toString());
        assertEquals(participant.getFormattedDetail(participantActivity), participantDetails.getText().toString());
        assertEquals(participantActivity.getString(R.string.interview_refused_message),viewById.getText());
    }

    @Test
    public void ShouldBeAbleToPopulateViewWithParticipantWithIncomplete() {
        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.INCOMPLETE, date);
        intent.putExtra(Constants.PARTICIPANT, participant);
        ParticipantActivity participantActivity = getParticipantActivity(intent);

        TextView participantName = participantActivity.findViewById(R.id.selected_participant_name);
        TextView participantDetails = participantActivity.findViewById(R.id.selected_participant_details);
        String participantFormattedName = participant.getFormattedName() + " (" + participantActivity.getString(R.string.pid) + " " + participant.getParticipantID() + " )";
        TextView viewById = participantActivity.findViewById(R.id.survey_message);

        assertNotNull(participantName);
        assertNotNull(participantDetails);
        assertEquals(participantFormattedName, participantName.getText().toString());
        assertEquals(participant.getFormattedDetail(participantActivity), participantDetails.getText().toString());
        assertEquals("",viewById.getText());
    }

    @Test
    public void ShouldBeAbleToPopulateViewWithParticipantWithNotReachable() {
        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.NOT_REACHABLE, date);
        intent.putExtra(Constants.PARTICIPANT, participant);
        ParticipantActivity participantActivity = getParticipantActivity(intent);

        TextView participantName = participantActivity.findViewById(R.id.selected_participant_name);
        TextView participantDetails = participantActivity.findViewById(R.id.selected_participant_details);
        String participantFormattedName = participant.getFormattedName() + " (" + participantActivity.getString(R.string.pid) + " " + participant.getParticipantID() + " )";
        TextView viewById = participantActivity.findViewById(R.id.survey_message);

        assertNotNull(participantName);
        assertNotNull(participantDetails);
        assertEquals(participantFormattedName, participantName.getText().toString());
        assertEquals(participant.getFormattedDetail(participantActivity), participantDetails.getText().toString());
        assertEquals(participantActivity.getString(R.string.interviewee_not_reachable_message),viewById.getText());
    }

    @Test
    public void testOnOptionsItemSelectedShouldOpenExpectedMenuItem() {

        MockedStatic<ParticipantActivityFactory> participantActivityFactory = Mockito.mockStatic(ParticipantActivityFactory.class);
        IMenuHandler handler = Mockito.mock(IMenuHandler.class);

        List<IMenuHandler> handlers = new ArrayList<>();
        handlers.add(handler);

        participantActivityFactory.when(() -> ParticipantActivityFactory.getMenuHandlers(Mockito.any(), Mockito.any())).thenReturn(handlers);
        Mockito.when(handler.shouldOpen(Mockito.any(Integer.class))).thenReturn(true);

        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.REFUSED, date);
        intent.putExtra(Constants.PARTICIPANT, participant);
        ParticipantActivity participantActivity = getParticipantActivity(intent);
        participantActivity.onOptionsItemSelected(Mockito.mock(MenuItem.class));

        Mockito.verify(handler).open();
        participantActivityFactory.close();
    }

    @Test
    public void testOnCreateOptionsMenuShouldVerifyMenuInflate() {
        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.REFUSED, date);
        intent.putExtra(Constants.PARTICIPANT, participant);
        ParticipantActivity participantActivity = Mockito.spy(getParticipantActivity(intent));

        MenuInflater menuInflater = Mockito.mock(MenuInflater.class);
        Mockito.when(participantActivity.getMenuInflater()).thenReturn(menuInflater);

        participantActivity.onCreateOptionsMenu(new RoboMenu());

        Mockito.verify(menuInflater).inflate(Mockito.anyInt(), Mockito.any());
    }

    @Test
    public void testOnActivityResultShouldHandleResult() {

        MockedStatic<ParticipantActivityFactory> participantActivityFactory = Mockito.mockStatic(ParticipantActivityFactory.class);
        IActivityResultHandler handler = Mockito.mock(IActivityResultHandler.class);

        List<IActivityResultHandler> handlers = new ArrayList<>();
        handlers.add(handler);

        participantActivityFactory.when(() -> ParticipantActivityFactory.getResultHandlers(Mockito.any(), Mockito.any())).thenReturn(handlers);
        Mockito.when(handler.canHandleResult(Mockito.any(Integer.class))).thenReturn(true);

        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.REFUSED, date);
        intent.putExtra(Constants.PARTICIPANT, participant);
        ParticipantActivity participantActivity = getParticipantActivity(intent);
        participantActivity.onActivityResult(1, -1, new Intent());

        Mockito.verify(handler).handleResult(Mockito.any(), Mockito.any(Integer.class));
        participantActivityFactory.close();
    }

    @Test
    public void testHandleCustomMenuShouldOpenExpectedMenuItem() {

        MockedStatic<ParticipantActivityFactory> participantActivityFactory = Mockito.mockStatic(ParticipantActivityFactory.class);
        IMenuHandler handler = Mockito.mock(IMenuHandler.class);

        List<IMenuHandler> handlers = new ArrayList<>();
        handlers.add(handler);

        participantActivityFactory.when(() -> ParticipantActivityFactory.getCustomMenuHandler(Mockito.any(), Mockito.any())).thenReturn(handlers);
        Mockito.when(handler.shouldOpen(Mockito.any(Integer.class))).thenReturn(true);

        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.REFUSED, date);
        intent.putExtra(Constants.PARTICIPANT, participant);
        ParticipantActivity participantActivity = getParticipantActivity(intent);
        participantActivity.handleCustomMenu(Mockito.mock(View.class));

        Mockito.verify(handler).open();
        participantActivityFactory.close();
    }

    @Test
    public void testOnPrepareOptionsMenuShouldDisableMenu() {

        MockedStatic<ParticipantActivityFactory> participantActivityFactory = Mockito.mockStatic(ParticipantActivityFactory.class);
        IMenuPreparer handler = Mockito.mock(IMenuPreparer.class);

        List<IMenuPreparer> handlers = new ArrayList<>();
        handlers.add(handler);

        participantActivityFactory.when(() -> ParticipantActivityFactory.getMenuPreparer(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(handlers);
        Mockito.when(handler.shouldDeactivate()).thenReturn(true);

        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.REFUSED, date);
        intent.putExtra(Constants.PARTICIPANT, participant);
        ParticipantActivity participantActivity = getParticipantActivity(intent);
        participantActivity.onPrepareOptionsMenu(Mockito.mock(Menu.class));

        Mockito.verify(handler).deactivate();
        participantActivityFactory.close();
    }

    private ParticipantActivity getParticipantActivity(Intent intent) {
        return Robolectric.buildActivity(ParticipantActivity.class, intent).create().resume().get();
    }
}