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

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.helper.CursorHelper;
import com.onaio.steps.helper.DatabaseHelper;

import java.io.Serializable;
import java.util.List;

public class Member implements Serializable {
    public static final String TABLE_NAME = "member";
    public static final String ID = "Id";
    public static final String MEMBER_HOUSEHOLD_ID = "member_household_id";
    public static final String FIRST_NAME = "first_name";
    public static final String FAMILY_SURNAME = "family_surname";
    public static final String GENDER = "gender";
    public static final String AGE = "age";
    public static final String HOUSEHOLD_ID = "household_id";
    public static final String DELETED = "deleted";
    public static final String TABLE_CREATE_QUERY = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT,%s TEXT, %s INTEGER, %s TEXT, %s INTEGER, %s INTEGER, FOREIGN KEY (%s) REFERENCES %s(%s))", TABLE_NAME, ID, MEMBER_HOUSEHOLD_ID,FAMILY_SURNAME,FIRST_NAME, AGE, GENDER,DELETED, HOUSEHOLD_ID, HOUSEHOLD_ID, Household.TABLE_NAME, Household.ID);
    public static final String FIND_ALL_UNSELECTED_QUERY = "SELECT * FROM MEMBER WHERE %s=%s and %s=%s  and %s!=%s ORDER BY Id asc";
    public static final String FIND_ALL_QUERY = "SELECT * FROM MEMBER WHERE %s=%s and %s=%s ORDER BY Id asc";
    public static final String FIND_ALL_WITH_DELETED_QUERY = "SELECT * FROM MEMBER WHERE %s=%s ORDER BY Id asc";
    public static final String FIND_BY_ID_QUERY = "SELECT * FROM MEMBER WHERE "+ID+" = '%s'";
    public static final String FIND_MEMBER_BY_HOUSEHOLD_ID_QUERY = "SELECT * FROM MEMBER WHERE "+MEMBER_HOUSEHOLD_ID+" = '%s'";
    public static final int NOT_DELETED_INT = 0;
    public static final int DELETED_INT = 1;
    private final int DELTA = 1;

    private String familySurname;
    private String firstName;
    private Gender gender;
    private int age;
    private Household household;
    private int id;
    private String memberHouseholdId;
    private Boolean deleted;

    public Member() {}

    public Member(int id, String familySurname, String firstName, Gender gender, int age, Household household, String memberHouseholdId, Boolean deleted) {
        this.familySurname = familySurname;
        this.firstName = firstName;
        this.gender = gender;
        this.age = age;
        this.household = household;
        this.id = id;
        this.memberHouseholdId = memberHouseholdId;
        this.deleted = deleted;
    }

    public Member(String familySurname, String firstName, Gender gender, int age, Household household, boolean deleted) {
        this.familySurname = familySurname;
        this.firstName = firstName;
        this.gender = gender;
        this.age = age;
        this.household = household;
        this.deleted = deleted;
    }

    public void setMemberHouseholdId(String memberHouseholdId) {
        this.memberHouseholdId = memberHouseholdId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFamilySurname() {
        return familySurname;
    }

    public String getFormattedName(){
        return String.format("%s %s",familySurname,firstName);
    }

    public String getFormattedDetail(AppCompatActivity activity){
        return String.format(gender.getInternationalizedString(activity) +", "+ age);
    }

    public Gender getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public Household getHousehold() {
        return household;
    }

    public String getDeletedString() {
        return deleted? "Yes":"No";
    }

    public String getMemberHouseholdId() {
        return memberHouseholdId;
    }

    public int getId() {
        return id;
    }

    public void setFamilySurname(String familySurname) {
        this.familySurname = familySurname;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public long save(DatabaseHelper db) {
        int memberNumber = household.numberOfMembers(db) + DELTA;
        String generatedId = household.getName() + "-" + memberNumber;
        ContentValues memberDetails = populateBasicDetails();
        memberDetails.put(MEMBER_HOUSEHOLD_ID, generatedId);
        long savedId = db.save(memberDetails, TABLE_NAME);
        if(savedId!= -1)
            id = (int) savedId;
        return savedId;
    }

    public long update(DatabaseHelper db){
        ContentValues memberDetails = populateBasicDetails();
        return db.update(memberDetails,TABLE_NAME,ID+" = "+id,null);
    }

    public void delete(DatabaseHelper db) {
        deleted = true;
        update(db);
    }

    private ContentValues populateBasicDetails() {
        ContentValues values = new ContentValues();
        values.put(FIRST_NAME, firstName);
        values.put(FAMILY_SURNAME, familySurname);
        values.put(GENDER, gender.toString());
        values.put(AGE, age);
        values.put(HOUSEHOLD_ID, household.getId());
        values.put(DELETED, deleted ? DELETED_INT : NOT_DELETED_INT);
        return values;
    }

    @Override
    public String toString() {
        return familySurname + " "+ firstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        if (age != member.age) return false;
        if (id != member.id) return false;
        if (deleted != null ? !deleted.equals(member.deleted) : member.deleted != null)
            return false;
        if (familySurname != null ? !familySurname.equals(member.familySurname) : member.familySurname != null)
            return false;
        if (firstName != null ? !firstName.equals(member.firstName) : member.firstName != null)
            return false;
        if (gender != null ? !gender.equals(member.gender) : member.gender != null) return false;
        if (household != null ? !household.equals(member.household) : member.household != null)
            return false;
        return !(memberHouseholdId != null ? !memberHouseholdId.equals(member.memberHouseholdId) : member.memberHouseholdId != null);

    }

    public static Member find_by_household_id(DatabaseHelper db, Household household, String memberId) {
        Cursor cursor = db.exec(String.format(FIND_MEMBER_BY_HOUSEHOLD_ID_QUERY, memberId));
        List<Member> members = new CursorHelper().getMembers(cursor, household);
        if (members.size() > 0) {
            Member member = members.get(0);
            db.close();
            return member;
        } else {
            return  null;
        }
    }

    @Override
    public int hashCode() {
        int result = familySurname != null ? familySurname.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + age;
        result = 31 * result + (household != null ? household.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (memberHouseholdId != null ? memberHouseholdId.hashCode() : 0);
        return result;
    }
}
