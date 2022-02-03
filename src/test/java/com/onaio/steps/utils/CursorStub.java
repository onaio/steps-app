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

package com.onaio.steps.utils;

import android.database.Cursor;

import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

import org.mockito.Mockito;


public class CursorStub {
    private final Cursor cursor;

    public CursorStub(Cursor cursor) {
        this.cursor = cursor;
    }

    public void stubCursorForHousehold(Household household) {
        Mockito.when(cursor.moveToFirst()).thenReturn(true);
        Mockito.when(cursor.getColumnIndex(Household.NAME)).thenReturn(1);
        Mockito.when(cursor.getColumnIndex(Household.PHONE_NUMBER)).thenReturn(2);
        Mockito.when(cursor.getColumnIndex(Household.ID)).thenReturn(3);
        Mockito.when(cursor.getColumnIndex(Household.SELECTED_MEMBER_ID)).thenReturn(4);
        Mockito.when(cursor.getColumnIndex(Household.STATUS)).thenReturn(5);
        Mockito.when(cursor.getColumnIndex(Household.CREATED_AT)).thenReturn(6);
        Mockito.when(cursor.getColumnIndex(Household.SERVER_STATUS)).thenReturn(7);

        Mockito.when(cursor.getString(1)).thenReturn(household.getName());
        Mockito.when(cursor.getString(2)).thenReturn(household.getPhoneNumber());
        Mockito.when(cursor.getString(3)).thenReturn(household.getId());
        Mockito.when(cursor.getString(4)).thenReturn(household.getSelectedMemberId());
        Mockito.when(cursor.getString(5)).thenReturn(household.getStatus().toString());
        Mockito.when(cursor.getString(6)).thenReturn(household.getCreatedAt());
        Mockito.when(cursor.getString(7)).thenReturn(household.getServerStatus().toString());
    }

    public void stubCursorForMember(long id, String memberFamilyName, String memberFirstName, Gender memberGender, String memberAge, String householdId, int isDeleted, String memberHouseholdId) {
        Mockito.when(cursor.moveToFirst()).thenReturn(true);
        Mockito.when(cursor.getColumnIndex(Member.FAMILY_SURNAME)).thenReturn(1);
        Mockito.when(cursor.getColumnIndex(Member.FIRST_NAME)).thenReturn(2);
        Mockito.when(cursor.getColumnIndex(Member.GENDER)).thenReturn(3);
        Mockito.when(cursor.getColumnIndex(Member.AGE)).thenReturn(4);
        Mockito.when(cursor.getColumnIndex(Member.DELETED)).thenReturn(5);
        Mockito.when(cursor.getColumnIndex(Member.ID)).thenReturn(6);
        Mockito.when(cursor.getColumnIndex(Member.MEMBER_HOUSEHOLD_ID)).thenReturn(7);
        Mockito.when(cursor.getColumnIndex(Member.HOUSEHOLD_ID)).thenReturn(8);

        Mockito.when(cursor.getString(1)).thenReturn(memberFamilyName);
        Mockito.when(cursor.getString(2)).thenReturn(memberFirstName);
        Mockito.when(cursor.getString(3)).thenReturn(memberGender.toString());
        Mockito.when(cursor.getString(4)).thenReturn(String.valueOf(memberAge));
        Mockito.when(cursor.getInt(5)).thenReturn(isDeleted);
        Mockito.when(cursor.getString(6)).thenReturn(String.valueOf(id));
        Mockito.when(cursor.getString(7)).thenReturn(memberHouseholdId);
        Mockito.when(cursor.getString(8)).thenReturn(householdId);
    }
}