package com.onaio.steps.handler.activities;


import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.R;
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
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class NewParticipantActivityHandlerTest {

    private DatabaseHelper dbMock;
    private ParticipantListActivity participantListActivity;
    private NewParticipantActivityHandler newParticipantActivityHandler;
    private Participant participantMock;
    private String date;
    private Participant participant;


    @Before
    public  void Setup() {
        dbMock = Mockito.mock(DatabaseHelper.class);
        participantMock = Mockito.mock(Participant.class);
        participantListActivity = Robolectric.setupActivity(ParticipantListActivity.class);
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
        date = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.DONE, date);

        participant.save(new DatabaseHelper(participantListActivity));
        intent.putExtra(Constants.PARTICIPANT,participant);
        ParticipantAdapter participantAdapter = Mockito.mock(ParticipantAdapter.class);
        Mockito.stub(participantAdapter.getViewTypeCount()).toReturn(1);
        participantListActivity.getListView().setAdapter(participantAdapter);
        ShadowActivity stepsActivityShadow = Robolectric.shadowOf(participantListActivity);

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

        ShadowActivity shadowActivity = Robolectric.shadowOf(participantListActivity);
        Intent nextStartedActivity = shadowActivity.getNextStartedActivity();
        assertEquals(NewParticipantActivity.class.getName(),nextStartedActivity.getComponent().getClassName());
    }

}
