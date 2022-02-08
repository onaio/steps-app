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

package com.onaio.steps.modelViewWrapper;

import static org.junit.Assert.assertEquals;

import android.widget.RadioGroup;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.NewParticipantActivity;
import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.robolectric.Robolectric;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ParticipantViewWrapperTest extends StepsTestRunner {


    private final int PARTICIPANT_VIEW_ID = R.id.participant_id_value;
    private final int SURNAME_VIEW_ID = R.id.member_family_surname;
    private final int FIRST_NAME_VIEW_ID = R.id.member_first_name;
    private final int GENDER_VIEW_ID = R.id.member_gender;
    private final int AGE_VIEW_ID = R.id.member_age;


    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    public String error_string;
    private ParticipantViewWrapper participantViewWrapper;
    private Participant participant;
    private NewParticipantActivity newParticipantActivity;

    @Before
    public void Setup() {

        newParticipantActivity = Robolectric.setupActivity(NewParticipantActivity.class);
        participantViewWrapper = new ParticipantViewWrapper(newParticipantActivity);
        String date = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH).format(new Date());
        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.DONE, date, null);
        error_string = getStringValue(R.string.invalid) + " %s, " + getStringValue(R.string.fill_correct_message) + " %s";
        setValue(Constants.PA_MIN_AGE, "18");
        setValue(Constants.PA_MAX_AGE, "70");
    }


    @Test
    public void ShouldRaiseExceptionWheParticipantIdIsNull() throws InvalidDataException {
        setSurname("Rana");
        setFirstName("Manisha");
        setGender(R.id.female_selection);
        setAge("23");

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string, "Participant", getStringValue(R.string.participant_id)));

        participantViewWrapper.getFromView();
    }

    @Test
    public void ShouldRaiseExceptionWhenSurnameIsNull() throws InvalidDataException {
        setParticipantId("123-30");
        setSurname("");
        setFirstName("Manisha");
        setGender(R.id.female_selection);
        setAge("23");

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string, "Participant", getStringValue(R.string.member_family_surname_hint)));

        participantViewWrapper.getFromView();
    }

    @Test
    public void ShouldRaiseExceptionWhenFirstNameIsNull() throws InvalidDataException {
        setParticipantId("123-30");
        setSurname("Some Surname");
        setFirstName("");
        setGender(R.id.female_selection);
        setAge("23");

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string, "Participant", getStringValue(R.string.member_first_name_hint)));

        participantViewWrapper.getFromView();
    }

    @Test
    public void ShouldRaiseExceptionWhenGenderIsNull() throws InvalidDataException {
        setParticipantId("123-30");
        setSurname("Some Surname");
        setFirstName("Manisha");
        setAge("23");

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string, "Participant", getStringValue(R.string.member_gender_hint)));

        participantViewWrapper.getFromView();
    }

    @Test
    public void ShouldRaiseExceptionWhenAgeIsNull() throws InvalidDataException {
        setParticipantId("123-30");
        setSurname("some surname");
        setFirstName("Manisha");
        setGender(R.id.female_selection);
        setAge("");

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string, "Participant", getStringValue(R.string.member_age_hint)));

        participantViewWrapper.getFromView();
    }

    @Test
    public void ShouldRaiseExceptionWhenAgeIsInvalid() throws InvalidDataException {
        setParticipantId("123-30");
        setSurname("some surname");
        setFirstName("Manisha");
        setGender(R.id.female_selection);
        setAge("12");

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string, "Participant", getStringValue(R.string.age_not_in_range)));

        participantViewWrapper.getFromView();
    }


    @Test
    public void ShouldRaiseExceptionWhenAllFieldsAreNull() throws InvalidDataException {
        setParticipantId("");
        setSurname("");
        setFirstName("");
        setAge("");

        expectedException.expect(InvalidDataException.class);
        expectedException.expectMessage(String.format(error_string, "Participant", getStringValue(R.string.participant_id), getStringValue(R.string.member_family_surname_hint), getStringValue(R.string.member_first_name_hint), getStringValue(R.string.member_gender_hint), getStringValue(R.string.age_hint)));

        participantViewWrapper.getFromView();
    }

    @Test
    public void ShouldGetValuesFromViewAndReturnMember() throws InvalidDataException {
        setParticipantId("123-100-1");
        setSurname("Bansal");
        setFirstName("Rohit");
        setGender(R.id.male_selection);
        setAge("20");

        Participant participant1 = participantViewWrapper.getFromView();

        assertEquals("123-100-1", participant1.getParticipantID());
        assertEquals("Bansal", participant1.getFamilySurname());
        assertEquals("Rohit", participant1.getFirstName());
        assertEquals("Male", participant1.getGender().toString());
        assertEquals(20, participant1.getAge());
    }


    @Test
    public void ShouldGetValuesFromViewAndUpdateMember() throws InvalidDataException {
        setParticipantId("123-100-1");
        setSurname("Bansal");
        setFirstName("Rohit");
        setGender(R.id.male_selection);
        setAge("20");

        Participant participant1 = participantViewWrapper.updateFromView(participant);

        assertEquals("123-100-1", participant1.getParticipantID());
        assertEquals("Bansal", participant1.getFamilySurname());
        assertEquals("Rohit", participant1.getFirstName());
        assertEquals("Male", participant1.getGender().toString());
        assertEquals(20, participant1.getAge());
    }

    @Test
    public void ShouldBeAbleToPopulateViewFromIntentData() {
        TextView participantId = newParticipantActivity.findViewById(R.id.participant_id_value);
        TextView surname = newParticipantActivity.findViewById(R.id.member_family_surname);
        TextView firstName = newParticipantActivity.findViewById(R.id.member_first_name);
        RadioGroup gender = newParticipantActivity.findViewById(R.id.member_gender);
        TextView age = newParticipantActivity.findViewById(R.id.member_age);

        participantViewWrapper.updateView(participant);

        assertEquals("123-10", participantId.getText().toString());
        assertEquals("family surname", surname.getText().toString());
        assertEquals("firstName", firstName.getText().toString());
        assertEquals(R.id.female_selection, gender.getCheckedRadioButtonId());
        assertEquals("34", age.getText().toString());
    }


    private void setParticipantId(String participantId) {
        ((TextView) newParticipantActivity.findViewById(PARTICIPANT_VIEW_ID)).setText(participantId);
    }

    private void setValue(String key, String value) {
        KeyValueStore keyValueStore = KeyValueStoreFactory.instance(newParticipantActivity);
        keyValueStore.putString(key, value);
    }

    private String getStringValue(int value) {
        return newParticipantActivity.getString(value);
    }

    protected void setSurname(String surname) {
        ((TextView) newParticipantActivity.findViewById(SURNAME_VIEW_ID)).setText(surname);
    }

    protected void setFirstName(String firstName) {
        ((TextView) newParticipantActivity.findViewById(FIRST_NAME_VIEW_ID)).setText(firstName);
    }

    protected void setGender(int genderId) {
        ((RadioGroup) newParticipantActivity.findViewById(GENDER_VIEW_ID)).check(genderId);
    }

    protected void setAge(String age) {
        ((TextView) newParticipantActivity.findViewById(AGE_VIEW_ID)).setText(age);
    }

}