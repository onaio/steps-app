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

import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml",shadows = {ShadowDatabaseHelper.class})
public class MemberTest {

    private final int DELTA = 1;
    private final int NOT_DELETED_INT = 0;
    private String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
    @Mock
    private DatabaseHelper db;
    @Mock
    private Cursor cursor;
    private String householdId = "1";
    private final String householdName = "Any household";
    private final String memberFamilyName = "Rana";
    private final String memberFirstName = "Manisha";
    private final String memberGender = Constants.FEMALE;
    private final int memberAge = 23;
    private Household household;

    @Before
    public void Setup(){
        db = Mockito.mock(DatabaseHelper.class);
        cursor = Mockito.mock(Cursor.class);
        String phoneNumber = "123456789";
        household = new Household(householdId, householdName, phoneNumber,"", HouseholdStatus.NOT_SELECTED, currentDate);
    }

    @Test
    public void ShouldBeAbleToSaveTheMember(){
        int numberOfMembers = 0;
        Member member = new Member(memberFamilyName, memberFirstName, memberGender, memberAge, household);
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

        Mockito.verify(db).update(Mockito.argThat(updateMemberMatcher(NOT_DELETED_INT)), Mockito.eq(Member.TABLE_NAME), Mockito.eq(Member.ID + " = "+memberId),Mockito.any(String[].class));
    }

    @Test
    public void ShouldBeAbleToDeleteTheMember(){
        int numberOfMembers = 0;
        int memberId = 1;
        Member member = new Member(memberId,memberFamilyName, memberFirstName, memberGender, memberAge, household,householdName+"-1",false);
        stubDb(numberOfMembers);

        member.delete(db);

        Mockito.verify(db).update(Mockito.argThat(updateMemberMatcher(Member.DELETED_INT)), Mockito.eq(Member.TABLE_NAME), Mockito.eq(Member.ID + " = "+memberId),Mockito.any(String[].class));
    }


    private ArgumentMatcher<ContentValues> saveMemberMatcher(final int memberCount) {
        return new ArgumentMatcher<ContentValues>() {
            @Override
            public boolean matches(Object argument) {
                ContentValues contentValues = (ContentValues) argument;
                assertBasicDetails(contentValues, NOT_DELETED_INT);
                assertTrue(contentValues.containsKey(Member.MEMBER_HOUSEHOLD_ID));
                assertTrue(contentValues.getAsString(Member.MEMBER_HOUSEHOLD_ID).equals(String.format("%s-%d", householdName, memberCount + DELTA)));
                return true;
            }
        };
    }

    private ArgumentMatcher<ContentValues> updateMemberMatcher(final int deleted) {
        return new ArgumentMatcher<ContentValues>() {
            @Override
            public boolean matches(Object argument) {
                ContentValues contentValues = (ContentValues) argument;
                assertBasicDetails(contentValues, deleted);
                return true;
            }
        };
    }

    private void assertBasicDetails(ContentValues contentValues, int deleted) {
        assertTrue(contentValues.containsKey(Member.FAMILY_SURNAME));
        assertTrue(contentValues.getAsString(Member.FAMILY_SURNAME).equals(memberFamilyName));
        assertTrue(contentValues.containsKey(Member.FIRST_NAME));
        assertTrue(contentValues.getAsString(Member.FIRST_NAME).equals(memberFirstName));
        assertTrue(contentValues.containsKey(Member.GENDER));
        assertTrue(contentValues.getAsString(Member.GENDER).equals(memberGender));
        assertTrue(contentValues.containsKey(Member.AGE));
        assertTrue(contentValues.getAsInteger(Member.AGE) == memberAge);
        assertTrue(contentValues.containsKey(Member.HOUSEHOLD_ID));
        assertTrue(contentValues.getAsString(Member.HOUSEHOLD_ID).equals(householdId));
        assertTrue(contentValues.containsKey(Member.DELETED));
        assertTrue(contentValues.getAsInteger(Member.DELETED) == deleted);
    }

    private void stubDb(int numberOfMembers) {

        Mockito.stub(cursor.getCount()).toReturn(numberOfMembers);
        Mockito.stub(db.exec(Mockito.anyString())).toReturn(cursor);
    }



}