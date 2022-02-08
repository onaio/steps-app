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
        List<Member> members = new ArrayList<Member>();
        if(cursor.moveToFirst()){
            do{
                String familySurname = cursor.getString(cursor.getColumnIndex(Member.FAMILY_SURNAME));
                String firstName = cursor.getString(cursor.getColumnIndex(Member.FIRST_NAME));
                String gender = cursor.getString(cursor.getColumnIndex(Member.GENDER));
                String age = cursor.getString(cursor.getColumnIndex(Member.AGE));
                String id = cursor.getString(cursor.getColumnIndex(Member.ID));
                String generatedId = cursor.getString(cursor.getColumnIndex(Member.MEMBER_HOUSEHOLD_ID));
                int deletedInteger = cursor.getInt(cursor.getColumnIndex(Member.DELETED));
                boolean deleted = deletedInteger != Member.NOT_DELETED_INT;
                if(household.getId().equals(cursor.getString(cursor.getColumnIndex(Member.HOUSEHOLD_ID))))
                    members.add(new Member(Integer.parseInt(id), familySurname,firstName, Gender.valueOf(gender), Integer.parseInt(age), household,generatedId, deleted));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return members;
    }

    public List<Household> getHouseholds(Cursor cursor){
        List<Household> householdNames = new ArrayList<Household>();
        if(cursor.moveToFirst()){
            do{
                String household_name = cursor.getString(cursor.getColumnIndex(Household.NAME));
                String household_number = cursor.getString(cursor.getColumnIndex(Household.PHONE_NUMBER));
                String id = cursor.getString(cursor.getColumnIndex(Household.ID));
                String selectedMemberId = cursor.getString(cursor.getColumnIndex(Household.SELECTED_MEMBER_ID));
                String status = cursor.getString(cursor.getColumnIndex(Household.STATUS));
                String createdAt = cursor.getString(cursor.getColumnIndex(Household.CREATED_AT));
                String comments = cursor.getString(cursor.getColumnIndex(Household.COMMENTS));
                String uniqueDeviceId = cursor.getString(cursor.getColumnIndex(Household.UNIQUE_DEVICE_ID));
                String serverStatus = cursor.getString(cursor.getColumnIndex(Household.SERVER_STATUS));
                String odkFormId = cursor.getString(cursor.getColumnIndex(Household.ODK_FORM_ID));

                String memberFamilySurname = cursor.getColumnIndex(Member.FAMILY_SURNAME) != -1 ? cursor.getString(cursor.getColumnIndex(Member.FAMILY_SURNAME)) : null;
                String memberFirstName = cursor.getColumnIndex(Member.FIRST_NAME) != -1 ? cursor.getString(cursor.getColumnIndex(Member.FIRST_NAME)) : null;

                Household hh = new Household(id,household_name, household_number,selectedMemberId, InterviewStatus.valueOf(status),createdAt, uniqueDeviceId, comments, odkFormId);
                hh.setServerStatus(ServerStatus.valueOf(serverStatus));

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
        ArrayList<Participant> participants = new ArrayList<Participant>();
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(Participant.ID));
                String participantId = cursor.getString(cursor.getColumnIndex(Participant.PARTICIPANT_ID));
                String firstName = cursor.getString(cursor.getColumnIndex(Participant.FIRST_NAME));
                String familySurname = cursor.getString(cursor.getColumnIndex(Participant.FAMILY_SURNAME));
                String gender = cursor.getString(cursor.getColumnIndex(Participant.GENDER));
                String age = cursor.getString(cursor.getColumnIndex(Participant.AGE));
                String status = cursor.getString(cursor.getColumnIndex(Participant.STATUS));
                String createdAt = cursor.getString(cursor.getColumnIndex(Participant.CREATED_AT));
                String odkFormId = cursor.getString(cursor.getColumnIndex(Participant.ODK_FORM_ID));
                participants.add(new Participant(Integer.parseInt(id), participantId,familySurname, firstName, Gender.valueOf(gender), Integer.parseInt(age), InterviewStatus.valueOf(status),createdAt, odkFormId));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return participants;
    }
}
