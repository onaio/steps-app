package com.onaio.steps.helper;

import android.database.Cursor;

import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.Member;

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
                boolean deleted = deletedInteger == Member.NOT_DELETED_INT ? false : true;
                if(household.getId().equals(cursor.getString(cursor.getColumnIndex(Member.HOUSEHOLD_ID))))
                    members.add(new Member(Integer.parseInt(id), familySurname,firstName, gender, Integer.parseInt(age), household,generatedId, deleted));
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
                String selectedMember = cursor.getString(cursor.getColumnIndex(Household.SELECTED_MEMBER_ID));
                String status = cursor.getString(cursor.getColumnIndex(Household.STATUS));
                String createdAt = cursor.getString(cursor.getColumnIndex(Household.CREATED_AT));
                householdNames.add(new Household(id,household_name, household_number,selectedMember, HouseholdStatus.valueOf(status),createdAt ));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return householdNames;

    }
}
