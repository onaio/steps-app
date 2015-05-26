package com.onaio.steps.handler.activities;

import android.content.Intent;

import com.onaio.steps.activities.ParticipantActivity;
import com.onaio.steps.activities.ParticipantListActivity;
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
import org.robolectric.shadows.ShadowActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ParticipantActivityHandlerTest {

    private ParticipantListActivity participantListActivity;
    private Participant participant;
    private ParticipantActivityHandler participantActivityHandler;
    private String date;

    @Before
    public void setup(){
        date = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
        participantListActivity = Robolectric.setupActivity(ParticipantListActivity.class);
        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.DONE, date);
        participantActivityHandler = new ParticipantActivityHandler(participantListActivity, participant);
    }

    @Test
    public void ShouldOpenParticipantActivity(){
        participantActivityHandler.open();

        ShadowActivity shadowActivity = Robolectric.shadowOf(participantListActivity);
        Intent nextStartedActivity = shadowActivity.getNextStartedActivity();
        assertEquals(ParticipantActivity.class.getName(),nextStartedActivity.getComponent().getClassName());
        assertEquals(participant,nextStartedActivity.getSerializableExtra(Constants.PARTICIPANT));
    }
}