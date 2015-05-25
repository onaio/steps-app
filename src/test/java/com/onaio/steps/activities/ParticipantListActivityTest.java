package com.onaio.steps.activities;

import android.widget.Button;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.Assert.assertEquals;

@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ParticipantListActivityTest {

    private ParticipantListActivity participantListActivity;
    private String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
    private Participant participant;

    @Before
    public void setUp(){
        participant = new Participant("123-10", "surname", "firstName",Gender.Female, 24, InterviewStatus.NOT_DONE, currentDate);
        participant.save(new DatabaseHelper(participantListActivity));
        participantListActivity = Robolectric.buildActivity(ParticipantListActivity.class).create().get();
    }

    @Test
    public void ShouldBeAbleToSetLayoutAndPopulateParticipants(){
        Button participantHeader = (Button) participantListActivity.findViewById(R.id.action_add_new_item);

        assertEquals(R.id.main_layout, Robolectric.shadowOf(participantListActivity).getContentView().getId());
        assertEquals(participantListActivity.getString(R.string.action_add_participant),participantHeader.getText());
        assertEquals(participantListActivity.getString(R.string.participant_header),participantListActivity.getTitle());
        assertEquals(participant, participantListActivity.getListView().getAdapter().getItem(0));
    }
}