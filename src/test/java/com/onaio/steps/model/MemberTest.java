package com.onaio.steps.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import junit.framework.Assert;
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
import java.util.List;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml",shadows = {ShadowDatabaseHelper.class})
public class MemberTest {

    private final int DELTA = 1;
    private final int NOT_DELETED_INT = 0;
    private final int DELETED_INT = 1;
    private String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
    @Mock
    private DatabaseHelper db;
    @Mock
    private Cursor cursor;
    private String householdId = "1";
    private final String householdName = "Any household";
    private final String phoneNumber = "123456789";
    private final String memberFamilyName = "Rana";
    private final String memberFirstName = "Manisha";
    private final String memberGender = Constants.FEMALE;
    private final int memberAge = 23;
    private Household household;
    private final int numberOfMembers = 5;
    private static final String ID = "Id";
    private static final String MEMBER_HOUSEHOLD_ID = "member_household_id";
    private static final String FIRST_NAME = "first_name";
    private static final String FAMILY_SURNAME = "family_surname";
    private static final String GENDER = "gender";
    private static final String AGE = "age";
    private static final String HOUSEHOLD_ID = "household_id";
    private static final String DELETED = "deleted";
    private static String FIND_ALL_QUERY = "SELECT * FROM MEMBER WHERE %s=%s and %s=%d ORDER BY Id asc";
    private static String FIND_ALL_WITH_DELETED_QUERY = "SELECT * FROM MEMBER WHERE %s=%s ORDER BY Id asc";

    @Before
    public void Setup(){
        db = Mockito.mock(DatabaseHelper.class);
        cursor = Mockito.mock(Cursor.class);
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

        Mockito.verify(db).update(Mockito.argThat(updateMemberMatcher(NOT_DELETED_INT)), Mockito.eq(Member.TABLE_NAME), Mockito.eq(ID + " = "+memberId),Mockito.any(String[].class));
    }

    @Test
    public void ShouldBeAbleToDeleteTheMember(){
        int numberOfMembers = 0;
        int memberId = 1;
        Member member = new Member(memberId,memberFamilyName, memberFirstName, memberGender, memberAge, household,householdName+"-1",false);
        stubDb(numberOfMembers);

        member.delete(db);

        Mockito.verify(db).update(Mockito.argThat(updateMemberMatcher(DELETED_INT)), Mockito.eq(Member.TABLE_NAME), Mockito.eq(ID + " = "+memberId),Mockito.any(String[].class));
    }

    @Test
    public void ShouldGetNonDeletedNumberOfMembersFromDatabase(){
        int numberOfMembers = 2;
        Household household = new Household(householdId, householdName, phoneNumber,"", HouseholdStatus.NOT_SELECTED, currentDate);
        stubDb(numberOfMembers);

        assertEquals(numberOfMembers, Member.numberOfNonDeletedMembers(db, household));

        Mockito.verify(db).exec(String.format(FIND_ALL_QUERY,HOUSEHOLD_ID,householdId, DELETED, NOT_DELETED_INT));
    }

    @Test
    public void ShouldGetAllNumberOfMembersFromDatabase(){
        Household household = new Household(householdId, householdName, phoneNumber,"", HouseholdStatus.NOT_SELECTED, currentDate);
        stubDb(numberOfMembers);

        assertEquals(numberOfMembers, Member.numberOfMembers(db, household));

        Mockito.verify(db).exec(String.format(FIND_ALL_WITH_DELETED_QUERY,HOUSEHOLD_ID,householdId));
    }

    @Test
    public void ShouldGetAllNonDeletedMember(){
        long memberId = 1L;
        Household household = new Household(householdId, householdName, phoneNumber,"", HouseholdStatus.NOT_SELECTED, currentDate);
        stubDb(numberOfMembers);
        stubCursor(memberId, NOT_DELETED_INT, householdName + "-1");

        List<Member> members = Member.getAll(db, household);

        assertEquals(1,members.size());
        validateMember(members.get(0),false);
        Mockito.verify(db).exec(String.format(FIND_ALL_QUERY,HOUSEHOLD_ID,householdId,DELETED, NOT_DELETED_INT));
    }

    @Test
    public void ShouldGetAllMember(){
        long memberId = 1L;
        Household household = new Household(householdId, householdName, phoneNumber,"", HouseholdStatus.NOT_SELECTED, currentDate);
        stubDb(numberOfMembers);
        stubCursor(memberId, DELETED_INT, householdName + "-1");

        List<Member> allMembers = Member.getAllForExport(db, household);

        assertEquals(1,allMembers.size());
        validateMember(allMembers.get(0),true);
        Mockito.verify(db).exec(String.format(FIND_ALL_WITH_DELETED_QUERY,HOUSEHOLD_ID,householdId));
    }

    @Test
    public void ShouldFindTheMemberById(){
        long memberId = 1L;
        Household household = new Household(householdId, householdName, phoneNumber,"", HouseholdStatus.NOT_SELECTED, currentDate);
        stubDb(numberOfMembers);
        stubCursor(memberId, NOT_DELETED_INT,householdName+"-1");
        String FIND_BY_ID_QUERY = "SELECT * FROM MEMBER WHERE " + ID + " = '%d'";

        Member member = Member.find_by(db, memberId, household);

        validateMember(member,false);
        Mockito.verify(db).exec(String.format(FIND_BY_ID_QUERY, memberId));
    }

    private void validateMember(Member member,boolean isDeleted){
        assertEquals(memberFamilyName,member.getFamilySurname());
        assertEquals(memberFirstName,member.getFirstName());
        assertEquals(memberAge,member.getAge());
        assertEquals(memberGender,member.getGender());
        String isDeletedString = isDeleted?"Yes":"No";
        assertEquals(isDeletedString,member.getDeletedString());
    }

    private ArgumentMatcher<ContentValues> saveMemberMatcher(final int memberCount) {
        return new ArgumentMatcher<ContentValues>() {
            @Override
            public boolean matches(Object argument) {
                ContentValues contentValues = (ContentValues) argument;
                assertBasicDetails(contentValues, NOT_DELETED_INT);
                assertTrue(contentValues.containsKey(MEMBER_HOUSEHOLD_ID));
                assertTrue(contentValues.getAsString(MEMBER_HOUSEHOLD_ID).equals(String.format("%s-%d", householdName, memberCount + DELTA)));
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
        assertTrue(contentValues.containsKey(FAMILY_SURNAME));
        assertTrue(contentValues.getAsString(FAMILY_SURNAME).equals(memberFamilyName));
        assertTrue(contentValues.containsKey(FIRST_NAME));
        assertTrue(contentValues.getAsString(FIRST_NAME).equals(memberFirstName));
        assertTrue(contentValues.containsKey(GENDER));
        assertTrue(contentValues.getAsString(GENDER).equals(memberGender));
        assertTrue(contentValues.containsKey(AGE));
        assertTrue(contentValues.getAsInteger(AGE) == memberAge);
        assertTrue(contentValues.containsKey(HOUSEHOLD_ID));
        assertTrue(contentValues.getAsString(HOUSEHOLD_ID).equals(householdId));
        assertTrue(contentValues.containsKey(DELETED));
        assertTrue(contentValues.getAsInteger(DELETED) == deleted);
    }

    private void stubDb(int numberOfMembers) {

        Mockito.stub(cursor.getCount()).toReturn(numberOfMembers);
        Mockito.stub(db.exec(Mockito.anyString())).toReturn(cursor);
    }

    private void stubCursor(long id,int isDeleted, String memberHouseholdId){
        Mockito.stub(cursor.moveToFirst()).toReturn(true);
        Mockito.stub(cursor.getColumnIndex(FAMILY_SURNAME)).toReturn(1);
        Mockito.stub(cursor.getColumnIndex(FIRST_NAME)).toReturn(2);
        Mockito.stub(cursor.getColumnIndex(GENDER)).toReturn(3);
        Mockito.stub(cursor.getColumnIndex(AGE)).toReturn(4);
        Mockito.stub(cursor.getColumnIndex(DELETED)).toReturn(5);
        Mockito.stub(cursor.getColumnIndex(ID)).toReturn(6);
        Mockito.stub(cursor.getColumnIndex(MEMBER_HOUSEHOLD_ID)).toReturn(7);
        Mockito.stub(cursor.getColumnIndex(HOUSEHOLD_ID)).toReturn(8);

        Mockito.stub(cursor.getString(1)).toReturn(memberFamilyName);
        Mockito.stub(cursor.getString(2)).toReturn(memberFirstName);
        Mockito.stub(cursor.getString(3)).toReturn(memberGender);
        Mockito.stub(cursor.getString(4)).toReturn(String.valueOf(memberAge));
        Mockito.stub(cursor.getInt(5)).toReturn(isDeleted);
        Mockito.stub(cursor.getString(6)).toReturn(String.valueOf(id));
        Mockito.stub(cursor.getString(7)).toReturn(memberHouseholdId);
        Mockito.stub(cursor.getString(8)).toReturn(householdId);
    }

}