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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import android.content.ContentValues;
import android.database.Cursor;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Config(shadows = {ShadowDatabaseHelper.class})
public class MemberTest extends StepsTestRunner {

    private final int DELTA = 1;
    private final int NOT_DELETED_INT = 0;
    private final String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH).format(new Date());
    @Mock
    private DatabaseHelper db;
    @Mock
    private Cursor cursor;
    private String householdId = "1";
    private final String householdName = "Any household";
    private final String memberFamilyName = "Rana";
    private final String memberFirstName = "Manisha";
    private final Gender memberGender = Gender.Female;
    private final int memberAge = 23;
    private Household household;
    private String comments="Dummy comments";

    @Before
    public void Setup(){
        db = Mockito.mock(DatabaseHelper.class);
        cursor = Mockito.mock(Cursor.class);
        String phoneNumber = "123456789";
        household = new Household(householdId, householdName, phoneNumber,"", InterviewStatus.SELECTION_NOT_DONE, currentDate, "uniqueDevId", comments);
    }

    @Test
    public void ShouldBeAbleToSaveTheMember(){
        int numberOfMembers = 0;
        Member member = new Member(memberFamilyName, memberFirstName, memberGender, memberAge, household, false);
        stubDb(numberOfMembers);

        member.save(db);

        Mockito.verify(db).save(Mockito.argThat(saveMemberMatcher(numberOfMembers)),Mockito.eq(Member.TABLE_NAME));
    }

    @Test
    public void ShouldBeAbleToUpdateTheMember(){
        int numberOfMembers = 0;
        int memberId = 1;
        Member member = new Member(memberId,memberFamilyName, memberFirstName, memberGender, memberAge, household,householdName+"-1",false);
        stubDb(numberOfMembers);

        member.update(db);

        Mockito.verify(db).update(Mockito.argThat(updateMemberMatcher(NOT_DELETED_INT)), Mockito.eq(Member.TABLE_NAME), Mockito.eq(Member.ID + " = "+memberId),Mockito.nullable(String[].class));
    }

    @Test
    public void ShouldBeAbleToDeleteTheMember(){
        int numberOfMembers = 0;
        int memberId = 1;
        Member member = new Member(memberId,memberFamilyName, memberFirstName, memberGender, memberAge, household,householdName+"-1",false);
        stubDb(numberOfMembers);

        member.delete(db);

        Mockito.verify(db).update(Mockito.argThat(updateMemberMatcher(Member.DELETED_INT)), Mockito.eq(Member.TABLE_NAME), Mockito.eq(Member.ID + " = "+memberId),Mockito.nullable(String[].class));
    }


    private ArgumentMatcher<ContentValues> saveMemberMatcher(final int memberCount) {
        return contentValues -> {
            assertBasicDetails(contentValues, NOT_DELETED_INT);
            assertTrue(contentValues.containsKey(Member.MEMBER_HOUSEHOLD_ID));
            assertTrue(contentValues.getAsString(Member.MEMBER_HOUSEHOLD_ID).equals(String.format("%s-%d", householdName, memberCount + DELTA)));
            return true;
        };
    }

    private ArgumentMatcher<ContentValues> updateMemberMatcher(final int deleted) {
        return contentValues -> {
            assertBasicDetails(contentValues, deleted);
            return true;
        };
    }

    private void assertBasicDetails(ContentValues contentValues, int deleted) {
        assertTrue(contentValues.containsKey(Member.FAMILY_SURNAME));
        assertEquals(contentValues.getAsString(Member.FAMILY_SURNAME), memberFamilyName);
        assertTrue(contentValues.containsKey(Member.FIRST_NAME));
        assertEquals(contentValues.getAsString(Member.FIRST_NAME), memberFirstName);
        assertTrue(contentValues.containsKey(Member.GENDER));
        assertEquals(contentValues.getAsString(Member.GENDER), memberGender.toString());
        assertTrue(contentValues.containsKey(Member.AGE));
        assertEquals((int) contentValues.getAsInteger(Member.AGE), memberAge);
        assertTrue(contentValues.containsKey(Member.HOUSEHOLD_ID));
        assertEquals(contentValues.getAsString(Member.HOUSEHOLD_ID), householdId);
        assertTrue(contentValues.containsKey(Member.DELETED));
        assertEquals((int) contentValues.getAsInteger(Member.DELETED), deleted);
    }

    private void stubDb(int numberOfMembers) {
        Mockito.when(cursor.getCount()).thenReturn(numberOfMembers);
        Mockito.when(db.exec(Mockito.anyString())).thenReturn(cursor);
    }
}