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

package com.onaio.steps.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml",shadows = {ShadowDatabaseHelper.class})

public class ParticipantTest {

    private String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH).format(new Date());
    public static final String ID = "Id";

    @Mock
    private DatabaseHelper db;
    private Participant participant;
    private Cursor cursor;


    @Before
    public void Setup(){
        cursor = Mockito.mock(Cursor.class);
        db = Mockito.mock(DatabaseHelper.class);
        participant = new Participant(1, "123-10", "family surname", "firstName", Gender.Female, 34, InterviewStatus.DONE, currentDate);
    }

    @Test
    public void ShouldBeAbleToSaveParticipant(){
        participant.save(db);

        Mockito.verify(db).save(Mockito.argThat(saveMemberMatcher()),Mockito.eq(Participant.TABLE_NAME));

    }

    private ArgumentMatcher<ContentValues> saveMemberMatcher() {
        return new ArgumentMatcher<ContentValues>() {
            @Override
            public boolean matches(Object argument) {
                ContentValues contentValues = (ContentValues) argument;
                assertTrue(contentValues.containsKey(Participant.CREATED_AT));
                assertTrue(contentValues.getAsString(Participant.CREATED_AT).equals(participant.getCreatedAt()));
                assertBasicDetails(contentValues);
                 return true;
            }
        };
    }
    private ArgumentMatcher<ContentValues> updateMemberMatcher() {
        return new ArgumentMatcher<ContentValues>() {
            @Override
            public boolean matches(Object argument) {
                ContentValues contentValues = (ContentValues) argument;
                assertBasicDetails(contentValues);
                return true;
            }
        };
    }

    private void assertBasicDetails(ContentValues contentValues) {
        assertTrue(contentValues.containsKey(Participant.PARTICIPANT_ID));
        assertTrue(contentValues.containsKey(Participant.FAMILY_SURNAME));
        assertTrue(contentValues.containsKey(Participant.FIRST_NAME));
        assertTrue(contentValues.containsKey(Participant.AGE));
        assertTrue(contentValues.containsKey(Participant.GENDER));
        assertTrue(contentValues.containsKey(Participant.STATUS));
        assertTrue(contentValues.getAsString(Participant.FAMILY_SURNAME).equals(participant.getFamilySurname()));
        assertTrue(contentValues.getAsString(Participant.PARTICIPANT_ID).equals(participant.getParticipantID()));
        assertTrue(contentValues.getAsString(Participant.FIRST_NAME).equals(participant.getFirstName()));
        assertTrue(contentValues.getAsString(Participant.GENDER).equals(participant.getGender().toString()));
        assertTrue(contentValues.getAsInteger(Participant.AGE).equals(participant.getAge()));
        assertTrue(contentValues.getAsString(Participant.STATUS).equals(participant.getStatus().toString()));

    }
        @Test
    public void ShouldBeAbleToUpdateParticipant(){
        participant.update(db);
        String id=String.valueOf(participant.getId());
        Mockito.verify(db).update(Mockito.argThat(updateMemberMatcher()),Mockito.eq(Participant.TABLE_NAME),Mockito.eq(ID + " ="+ id),Mockito.any(String[].class));
    }

}