package com.onaio.steps.activities;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Robolectric.shadowOf;

@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class NewParticipantActivityTest {

    private Participant participant;
    private String date;
    private NewParticipantActivity newParticipantActivity;

    @Before
    public void setup() {
        Intent intent = new Intent();
        date = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.DONE, date);
        intent.putExtra(Constants.PARTICIPANT, participant);
        newParticipantActivity = Robolectric.buildActivity(NewParticipantActivity.class).withIntent(intent)
                .create()
                .get();
    }

    @Test
    public void ShouldPopulateViewOnCreate() {
        assertEquals(R.id.participant_form, shadowOf(newParticipantActivity).getContentView().getId());
        TextView header = (TextView) newParticipantActivity.findViewById(R.id.form_header);
        TextView surname = (TextView) newParticipantActivity.findViewById(R.id.member_family_surname);
        TextView firstName = (TextView) newParticipantActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = (RadioGroup) newParticipantActivity.findViewById(R.id.member_gender);
        TextView age = (TextView) newParticipantActivity.findViewById(R.id.member_age);
        Button doneButton = (Button) newParticipantActivity.findViewById(R.id.ic_done);

        assertEquals(newParticipantActivity.getString(R.string.add), doneButton.getText());
        Assert.assertNotNull(header);
        Assert.assertNotNull(surname);
        Assert.assertNotNull(firstName);
        Assert.assertNotNull(gender);
        Assert.assertNotNull(age);
        Assert.assertTrue(header.getText().equals("Add New Participant"));
    }

    @Test
    public void ShouldFinishActivityAfterSavingParticipantData() {
        setValue(Constants.MIN_AGE, "12");
        setValue(Constants.MAX_AGE, "50");
        TextView participantId = (TextView) newParticipantActivity.findViewById(R.id.participant_id_value);
        TextView surname = (TextView) newParticipantActivity.findViewById(R.id.member_family_surname);
        TextView firstName = (TextView) newParticipantActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = (RadioGroup) newParticipantActivity.findViewById(R.id.member_gender);
        TextView age = (TextView) newParticipantActivity.findViewById(R.id.member_age);
        surname.setText("family surname");
        firstName.setText("first Name");
        gender.check(R.id.male_selection);
        age.setText("34");
        participantId.setText("123-10");
        View view = Mockito.mock(View.class);
        Mockito.stub(view.getId()).toReturn(R.id.member_form);

        newParticipantActivity.save(view);

        Intent intent = newParticipantActivity.getIntent();

        assertEquals(participant, intent.getSerializableExtra(Constants.PARTICIPANT));
        assertTrue(newParticipantActivity.isFinishing());
    }

    @Test
    public void ShouldNotCreateParticipantWithInsufficientDataAndShouldNotFinishActivity() {
        setValue(Constants.MIN_AGE, "12");
        setValue(Constants.MAX_AGE, "50");
        TextView surname = (TextView) newParticipantActivity.findViewById(R.id.member_family_surname);
        TextView participantId = (TextView) newParticipantActivity.findViewById(R.id.participant_id_value);
        TextView firstName = (TextView) newParticipantActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = (RadioGroup) newParticipantActivity.findViewById(R.id.member_gender);
        TextView age = (TextView) newParticipantActivity.findViewById(R.id.member_age);
        surname.setText("");
        firstName.setText("");
        gender.check(R.id.male_selection);
        age.setText("");
        participantId.setText("");
        View view = Mockito.mock(View.class);
        Mockito.stub(view.getId()).toReturn(R.id.member_form);

        newParticipantActivity.save(view);

        Intent intent = newParticipantActivity.getIntent();

        assertEquals(participant, intent.getSerializableExtra((Constants.PARTICIPANT)));
        assertFalse(newParticipantActivity.isFinishing());
    }

    private void setValue(String key, String value) {
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(newParticipantActivity);
        keyValueStore.putString(key, value);
    }

    @Test
    public void ShouldFinishActivityWhenCanceled() {
        newParticipantActivity.cancel(null);

        assertTrue(newParticipantActivity.isFinishing());
    }

}