package com.onaio.steps.utils;

import android.database.Cursor;

import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.Member;

import org.mockito.Mockito;

public class CursorStub {
    private Cursor cursor;

    public CursorStub(Cursor cursor) {
        this.cursor = cursor;
    }

    public void stubCursorForHousehold(String householdName, String phoneNumber, String householdId, HouseholdStatus householdStatus, String currentDate, String selectedMember){
        Mockito.stub(cursor.moveToFirst()).toReturn(true);
        Mockito.stub(cursor.getColumnIndex(Household.NAME)).toReturn(1);
        Mockito.stub(cursor.getColumnIndex(Household.PHONE_NUMBER)).toReturn(2);
        Mockito.stub(cursor.getColumnIndex(Household.ID)).toReturn(3);
        Mockito.stub(cursor.getColumnIndex(Household.SELECTED_MEMBER)).toReturn(4);
        Mockito.stub(cursor.getColumnIndex(Household.STATUS)).toReturn(5);
        Mockito.stub(cursor.getColumnIndex(Household.CREATED_AT)).toReturn(6);

        Mockito.stub(cursor.getString(1)).toReturn(householdName);
        Mockito.stub(cursor.getString(2)).toReturn(phoneNumber);
        Mockito.stub(cursor.getString(3)).toReturn(String.valueOf(householdId));
        Mockito.stub(cursor.getString(4)).toReturn(String.valueOf(selectedMember));
        Mockito.stub(cursor.getString(5)).toReturn(householdStatus.toString());
        Mockito.stub(cursor.getString(6)).toReturn(currentDate);
    }

    public void stubCursorForMember(long id, String memberFamilyName, String memberFirstName, String memberGender, String memberAge, String householdId, int isDeleted, String memberHouseholdId){
        Mockito.stub(cursor.moveToFirst()).toReturn(true);
        Mockito.stub(cursor.getColumnIndex(Member.FAMILY_SURNAME)).toReturn(1);
        Mockito.stub(cursor.getColumnIndex(Member.FIRST_NAME)).toReturn(2);
        Mockito.stub(cursor.getColumnIndex(Member.GENDER)).toReturn(3);
        Mockito.stub(cursor.getColumnIndex(Member.AGE)).toReturn(4);
        Mockito.stub(cursor.getColumnIndex(Member.DELETED)).toReturn(5);
        Mockito.stub(cursor.getColumnIndex(Member.ID)).toReturn(6);
        Mockito.stub(cursor.getColumnIndex(Member.MEMBER_HOUSEHOLD_ID)).toReturn(7);
        Mockito.stub(cursor.getColumnIndex(Member.HOUSEHOLD_ID)).toReturn(8);

        Mockito.stub(cursor.getString(1)).toReturn(memberFamilyName);
        Mockito.stub(cursor.getString(2)).toReturn(memberFirstName);
        Mockito.stub(cursor.getString(3)).toReturn(memberGender);
        Mockito.stub(cursor.getString(4)).toReturn(String.valueOf(memberAge));
        Mockito.stub(cursor.getInt(5)).toReturn(isDeleted);
        Mockito.stub(cursor.getString(6)).toReturn(String.valueOf(id));
        Mockito.stub(cursor.getString(7)).toReturn(memberHouseholdId);
        Mockito.stub(cursor.getString(8)).toReturn(householdId);
    }

//    public void stubCursorForMemberCount(int count){
//        Mockito.stub(cursor.getCount()).toReturn(count);
//    }
}
