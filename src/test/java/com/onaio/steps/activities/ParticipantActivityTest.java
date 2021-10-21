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


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ParticipantActivityTest {

    private String date;
    private Participant participant;
    private ParticipantActivity participantActivity;
    private ActivityController<ParticipantActivity> participantActivityController;
    private Intent intent;

    @Before
    public void setup() {
        intent = new Intent();
        date = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH).format(new Date());
        participantActivityController = Robolectric.buildActivity(ParticipantActivity.class);

    }

    @Test
    public void ShouldBeAbleToStyleActionBarAndCustomizeOptionStrings() {
        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.DONE, date);
        intent.putExtra(Constants.PARTICIPANT, participant);
        participantActivity=participantActivityController.withIntent(intent).create().get();

        Button cancelButton = (Button) participantActivity.findViewById(R.id.action_cancel_participant);
        Button takeSurveyButton = (Button) participantActivity.findViewById(R.id.action_take_survey);
        Button deferredButton = (Button) participantActivity.findViewById(R.id.action_deferred);

        assertEquals(participant.getFormattedName(), participantActivity.getActionBar().getTitle());
        assertEquals("ENTER DATA NOW", takeSurveyButton.getText().toString());
        assertEquals("ENTER DATA LATER", deferredButton.getText().toString());
        assertEquals(View.GONE, cancelButton.getVisibility());
    }

    @Test
    public void ShouldBeAbleToPopulateViewWithParticipantWithDoneStatus() {
        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.DONE, date);
        intent.putExtra(Constants.PARTICIPANT, participant);
        participantActivity=participantActivityController.withIntent(intent).create().get();

        TextView participantName = (TextView) participantActivity.findViewById(R.id.selected_participant_name);
        TextView participantDetails = (TextView) participantActivity.findViewById(R.id.selected_participant_details);
        String participantFormattedName = participant.getFormattedName() + " (" + participantActivity.getString(R.string.pid) + " " + participant.getParticipantID() + " )";
        TextView viewById = (TextView) participantActivity.findViewById(R.id.survey_message);

        assertNotNull(participantName);
        assertNotNull(participantDetails);
        assertEquals(participantFormattedName, participantName.getText().toString());
        assertEquals(participant.getFormattedDetail(participantActivity), participantDetails.getText().toString());
        assertEquals("Interview is complete!",viewById.getText());
    }

    @Test
    public void ShouldBeAbleToPopulateViewWithParticipantWithRefused() {
        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.REFUSED, date);
        intent.putExtra(Constants.PARTICIPANT, participant);
        participantActivity=participantActivityController.withIntent(intent).create().get();

        TextView participantName = (TextView) participantActivity.findViewById(R.id.selected_participant_name);
        TextView participantDetails = (TextView) participantActivity.findViewById(R.id.selected_participant_details);
        String participantFormattedName = participant.getFormattedName() + " (" + participantActivity.getString(R.string.pid) + " " + participant.getParticipantID() + " )";
        TextView viewById = (TextView) participantActivity.findViewById(R.id.survey_message);

        assertNotNull(participantName);
        assertNotNull(participantDetails);
        assertEquals(participantFormattedName, participantName.getText().toString());
        assertEquals(participant.getFormattedDetail(participantActivity), participantDetails.getText().toString());
        assertEquals("Interview is refused!",viewById.getText());
    }

    @Test
    public void ShouldBeAbleToPopulateViewWithParticipantWithIncomplete() {
        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.INCOMPLETE, date);
        intent.putExtra(Constants.PARTICIPANT, participant);
        participantActivity=participantActivityController.withIntent(intent).create().get();

        TextView participantName = (TextView) participantActivity.findViewById(R.id.selected_participant_name);
        TextView participantDetails = (TextView) participantActivity.findViewById(R.id.selected_participant_details);
        String participantFormattedName = participant.getFormattedName() + " (" + participantActivity.getString(R.string.pid) + " " + participant.getParticipantID() + " )";
        TextView viewById = (TextView) participantActivity.findViewById(R.id.survey_message);

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
        participantActivity=participantActivityController.withIntent(intent).create().get();

        TextView participantName = (TextView) participantActivity.findViewById(R.id.selected_participant_name);
        TextView participantDetails = (TextView) participantActivity.findViewById(R.id.selected_participant_details);
        String participantFormattedName = participant.getFormattedName() + " (" + participantActivity.getString(R.string.pid) + " " + participant.getParticipantID() + " )";
        TextView viewById = (TextView) participantActivity.findViewById(R.id.survey_message);

        assertNotNull(participantName);
        assertNotNull(participantDetails);
        assertEquals(participantFormattedName, participantName.getText().toString());
        assertEquals(participant.getFormattedDetail(participantActivity), participantDetails.getText().toString());
        assertEquals(participantActivity.getString(R.string.interview_not_reachable_message),viewById.getText());
    }
}