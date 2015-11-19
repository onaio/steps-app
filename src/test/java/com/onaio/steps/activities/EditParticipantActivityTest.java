package com.onaio.steps.activities;


import android.content.Intent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Robolectric.shadowOf;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class EditParticipantActivityTest {


    private Participant participant;
    private String date;
    private EditParticipantActivity editParticipantActivity;

    @Before
    public void setup(){
        date = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.DONE, date);

        Intent intent = new Intent();
        intent.putExtra(Constants.PARTICIPANT, participant);

        editParticipantActivity = Robolectric.buildActivity(EditParticipantActivity.class)
                .withIntent(intent)
                .create()
                .get();
    }

    @Test
    public void ShouldPopulateViewWithMemberForm(){

        TextView header = (TextView)editParticipantActivity.findViewById(R.id.form_header);
        TextView participantId = (TextView)editParticipantActivity.findViewById(R.id.participant_id_value);
        TextView surname = (TextView)editParticipantActivity.findViewById(R.id.member_family_surname);
        TextView firstName = (TextView)editParticipantActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = (RadioGroup)editParticipantActivity.findViewById(R.id.member_gender);
        TextView age = (TextView)editParticipantActivity.findViewById(R.id.member_age);

        assertEquals(R.id.participant_form, shadowOf(editParticipantActivity).getContentView().getId());
        assertNotNull(header);
        assertNotNull(surname);
        assertNotNull(firstName);
        assertNotNull(gender);
        assertNotNull(age);
        assertEquals("Edit Participant", header.getText());
        assertEquals("123-10",participantId.getText().toString());
        assertEquals("family surname", surname.getText().toString());
        assertEquals("firstName", firstName.getText().toString());
        assertEquals(R.id.female_selection, gender.getCheckedRadioButtonId());
        assertEquals("34", age.getText().toString());
    }


    @Test
    public void ShouldNotPassImproperDataToIntent(){
        setValue(Constants.HH_MIN_AGE,"12");
        setValue(Constants.HH_MAX_AGE,"60");
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(viewMock.getId()).toReturn(R.id.member_form);
        TextView participantId = (TextView) editParticipantActivity.findViewById(R.id.participant_id_value);
        TextView surnameView = (TextView) editParticipantActivity.findViewById(R.id.member_family_surname);
        TextView firstNameView = (TextView) editParticipantActivity.findViewById(R.id.member_first_name);
        TextView ageView = (TextView) editParticipantActivity.findViewById(R.id.member_age);
        RadioGroup genderView = (RadioGroup) editParticipantActivity.findViewById(R.id.member_gender);

        participantId.setText("");
        surnameView.setText("");
        firstNameView.setText("");
        ageView.setText("");
        genderView.check(R.id.female_selection);


        editParticipantActivity.save(viewMock);

        Intent intent = editParticipantActivity.getIntent();
        assertEquals(participant, intent.getSerializableExtra(Constants.PARTICIPANT));

        assertFalse(editParticipantActivity.isFinishing());
    }


    @Test
    public void ShouldUpdateMemberAndFinishActivity(){
        setValue(Constants.HH_MIN_AGE,"12");
        setValue(Constants.HH_MAX_AGE,"60");
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(viewMock.getId()).toReturn(R.id.member_form);

        editParticipantActivity.save(viewMock);

        Intent intent = editParticipantActivity.getIntent();
        assertEquals(participant, intent.getSerializableExtra(Constants.PARTICIPANT));
        assertTrue(editParticipantActivity.isFinishing());
    }



    private void setValue(String key, String value) {
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(editParticipantActivity);
        keyValueStore.putString(key, value);
    }

    @Test
    public void ShouldFinishTheActivityOnCancel(){
        editParticipantActivity.cancel(null);

        assertTrue(editParticipantActivity.isFinishing());
    }
}