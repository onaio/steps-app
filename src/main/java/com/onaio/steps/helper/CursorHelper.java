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

package com.onaio.steps.helper;

import android.annotation.SuppressLint;
import android.database.Cursor;

import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.Participant;
import com.onaio.steps.model.ServerStatus;

import java.util.ArrayList;
import java.util.List;


public class CursorHelper {
    public List<Member> getMembers(Cursor cursor,Household household){
        List<Member> members = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                String familySurname = getString(cursor, Member.FAMILY_SURNAME);
                String firstName = getString(cursor, Member.FIRST_NAME);
                String gender = getString(cursor, Member.GENDER);
                String age = getString(cursor, Member.AGE);
                String id = getString(cursor, Member.ID);
                String generatedId = getString(cursor, Member.MEMBER_HOUSEHOLD_ID);
                int deletedInteger = getInt(cursor, Member.DELETED);
                boolean deleted = deletedInteger != Member.NOT_DELETED_INT;
                if(household.getId().equals(getString(cursor, Member.HOUSEHOLD_ID)))
                    members.add(new Member(Integer.parseInt(id), familySurname,firstName, Gender.valueOf(gender), Integer.parseInt(age), household,generatedId, deleted));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return members;
    }

    public List<Household> getHouseholds(Cursor cursor){
        List<Household> householdNames = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                String household_name = getString(cursor, Household.NAME);
                String household_number = getString(cursor, Household.PHONE_NUMBER);
                String id = getString(cursor, Household.ID);
                String selectedMemberId = getString(cursor, Household.SELECTED_MEMBER_ID);
                String status = getString(cursor, Household.STATUS);
                String createdAt = getString(cursor, Household.CREATED_AT);
                String comments = getString(cursor, Household.COMMENTS);
                String uniqueDeviceId = getString(cursor, Household.UNIQUE_DEVICE_ID);
                String serverStatus = getString(cursor, Household.SERVER_STATUS);
                String odkFormId = getString(cursor, Household.ODK_FORM_ID);
                String odkJrFormId = getString(cursor, Household.ODK_JR_FORM_ID);
                String odkJrFormTitle = getString(cursor, Household.ODK_JR_FORM_TITLE);

                String memberFamilySurname = cursor.getColumnIndex(Member.FAMILY_SURNAME) != -1 ? getString(cursor, Member.FAMILY_SURNAME) : null;
                String memberFirstName = cursor.getColumnIndex(Member.FIRST_NAME) != -1 ? getString(cursor, Member.FIRST_NAME) : null;

                Household hh = new Household(id,household_name, household_number,selectedMemberId, InterviewStatus.valueOf(status),createdAt, uniqueDeviceId, comments, odkFormId);
                hh.setServerStatus(ServerStatus.valueOf(serverStatus));
                hh.setOdkJrFormId(odkJrFormId);
                hh.setOdkJrFormTitle(odkJrFormTitle);

                if (memberFamilySurname != null && memberFirstName != null) {
                    Member member = new Member();
                    member.setFamilySurname(memberFamilySurname);
                    member.setFirstName(memberFirstName);
                    hh.setSelectedMember(member);
                }

                householdNames.add(hh);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return householdNames;

    }

    public ArrayList<Participant> getParticipants(Cursor cursor) {
        ArrayList<Participant> participants = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String id = getString(cursor, Participant.ID);
                String participantId = getString(cursor, Participant.PARTICIPANT_ID);
                String firstName = getString(cursor, Participant.FIRST_NAME);
                String familySurname = getString(cursor, Participant.FAMILY_SURNAME);
                String gender = getString(cursor, Participant.GENDER);
                String age = getString(cursor, Participant.AGE);
                String status = getString(cursor, Participant.STATUS);
                String createdAt = getString(cursor, Participant.CREATED_AT);
                String odkFormId = getString(cursor, Participant.ODK_FORM_ID);
                participants.add(new Participant(Integer.parseInt(id), participantId,familySurname, firstName, Gender.valueOf(gender), Integer.parseInt(age), InterviewStatus.valueOf(status),createdAt, odkFormId));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return participants;
    }

    @SuppressLint("Range")
    public String getString(Cursor cursor, String column) {
        return cursor.getString(cursor.getColumnIndex(column));
    }

    @SuppressLint("Range")
    public int getInt(Cursor cursor, String column) {
        return cursor.getInt(cursor.getColumnIndex(column));
    }
}
