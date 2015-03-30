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
import java.util.List;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml",shadows = {ShadowDatabaseHelper.class})
public class HouseholdTest {

    private final HouseholdStatus householdStatus = HouseholdStatus.NOT_SELECTED;
    private String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
    @Mock
    private DatabaseHelper db;
    @Mock
    private Cursor cursor;
    private final String householdName = "Any household";
    private final String phoneNumber = "123456789";
    public static final String ID = "Id";
    private static final String NAME = "Name";
    private static final String STATUS = "Status";
    private static final String PHONE_NUMBER = "Phone_Number";
    private static final String SELECTED_MEMBER = "selected_member";
    private static final String CREATED_AT = "Created_At";
    private final long householdId = 1;

    @Before
    public void Setup(){
        db = Mockito.mock(DatabaseHelper.class);
        cursor = Mockito.mock(Cursor.class);
    }

    @Test
    public void ShouldBeAbleToSaveTheHousehold(){
        Household household = new Household(householdName, phoneNumber, householdStatus, currentDate);
        stubDb();

        household.save(db);

        Mockito.verify(db).save(Mockito.argThat(saveHouseholdMatcher(currentDate)),Mockito.eq(Household.TABLE_NAME));
    }

    @Test
    public void ShouldBeAbleToUpdateTheMember(){
        String selectedMember = "3";
        Household household = new Household(String.valueOf(householdId),householdName, phoneNumber, selectedMember,householdStatus, currentDate);
        stubDb();

        household.update(db);

        Mockito.verify(db).update(Mockito.argThat(updateHouseholdMatcher(selectedMember)), Mockito.eq(Household.TABLE_NAME), Mockito.eq(ID + " = "+householdId),Mockito.any(String[].class));
    }

    @Test
    public void ShouldGetAllNumberOfHouseholdFromDatabase(){
        stubDb();
        int householdCount = 5;
        Mockito.stub(cursor.getInt(0)).toReturn(householdCount);
        String FIND_ALL_COUNT_QUERY = "SELECT count(*) FROM HOUSEHOLD ORDER BY Id desc";

        assertEquals(5, Household.getAllCount(db));

        Mockito.verify(db).exec(String.format(FIND_ALL_COUNT_QUERY));
    }

    @Test
    public void ShouldGetAllHouseholds(){
        stubDb();
        String FIND_ALL_QUERY = "SELECT * FROM HOUSEHOLD ORDER BY Id desc";
        stubCursor("");

        List<Household> households = Household.getAll(db);

        assertEquals(1,households.size());
        validateHousehold(households.get(0));
        Mockito.verify(db).exec(String.format(FIND_ALL_QUERY));
    }

    @Test
    public void ShouldFindTheHouseholdById(){
        stubDb();
        stubCursor("");
        String FIND_BY_ID_QUERY = "SELECT * FROM HOUSEHOLD WHERE id = %d";

        Household household = Household.find_by(db, householdId);

        validateHousehold(household);
        Mockito.verify(db).exec(String.format(FIND_BY_ID_QUERY, householdId));
    }

    private void validateHousehold(Household household) {
        assertEquals(householdName,household.getName());
        assertEquals(householdStatus,household.getStatus());
        assertEquals(phoneNumber,household.getPhoneNumber());
        assertEquals(currentDate,household.getCreatedAt());
    }

    private ArgumentMatcher<ContentValues> saveHouseholdMatcher(final String created_at) {
        return new ArgumentMatcher<ContentValues>() {
            @Override
            public boolean matches(Object argument) {
                ContentValues contentValues = (ContentValues) argument;
                assertBasicDetails(contentValues);
                assertTrue(contentValues.containsKey(CREATED_AT));
                assertTrue(contentValues.getAsString(CREATED_AT).equals(created_at));
                return true;
            }
        };
    }

    private ArgumentMatcher<ContentValues> updateHouseholdMatcher(final String selectedMember) {
        return new ArgumentMatcher<ContentValues>() {
            @Override
            public boolean matches(Object argument) {
                ContentValues contentValues = (ContentValues) argument;
                assertBasicDetails(contentValues);
                assertTrue(contentValues.containsKey(SELECTED_MEMBER));
                assertTrue(contentValues.getAsString(SELECTED_MEMBER).equals(selectedMember));
                return true;
            }
        };
    }

    private void assertBasicDetails(ContentValues contentValues) {
        assertTrue(contentValues.containsKey(NAME));
        assertTrue(contentValues.getAsString(NAME).equals(householdName));
        assertTrue(contentValues.containsKey(PHONE_NUMBER));
        assertTrue(contentValues.getAsString(PHONE_NUMBER).equals(phoneNumber));
        assertTrue(contentValues.containsKey(STATUS));
        assertTrue(contentValues.getAsString(STATUS).equals(householdStatus.toString()));

    }

    private void stubDb() {
        Mockito.stub(db.exec(Mockito.anyString())).toReturn(cursor);
    }

    private void stubCursor(String selectedMember){
        Mockito.stub(cursor.moveToFirst()).toReturn(true);
        Mockito.stub(cursor.getColumnIndex(NAME)).toReturn(1);
        Mockito.stub(cursor.getColumnIndex(PHONE_NUMBER)).toReturn(2);
        Mockito.stub(cursor.getColumnIndex(ID)).toReturn(3);
        Mockito.stub(cursor.getColumnIndex(SELECTED_MEMBER)).toReturn(4);
        Mockito.stub(cursor.getColumnIndex(STATUS)).toReturn(5);
        Mockito.stub(cursor.getColumnIndex(CREATED_AT)).toReturn(6);

        Mockito.stub(cursor.getString(1)).toReturn(householdName);
        Mockito.stub(cursor.getString(2)).toReturn(phoneNumber);
        Mockito.stub(cursor.getString(3)).toReturn(String.valueOf(householdId));
        Mockito.stub(cursor.getString(4)).toReturn(String.valueOf(selectedMember));
        Mockito.stub(cursor.getString(5)).toReturn(householdStatus.toString());
        Mockito.stub(cursor.getString(6)).toReturn(currentDate);
    }

}