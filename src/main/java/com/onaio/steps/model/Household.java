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

import com.onaio.steps.helper.CursorHelper;
import com.onaio.steps.helper.DatabaseHelper;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Household implements Serializable,Comparable<Household> {
    private static String FIND_BY_ID_QUERY = "SELECT * FROM HOUSEHOLD WHERE id = %s";
    public static final String FIND_BY_NAME_QUERY = "SELECT * FROM HOUSEHOLD WHERE %s = '%s'";
    public static final String FIND_BY_STATUS_QUERY = "SELECT * FROM HOUSEHOLD WHERE %s = '%s'";
    public static final String FIND_BY_SERVER_STATUS_NOT_EQUAL_QUERY = "SELECT * FROM HOUSEHOLD WHERE %s != '%s'";
    private static String FIND_ALL_QUERY = "SELECT hh.*, m.family_surname, m.first_name FROM household AS hh LEFT JOIN member AS m ON m.id = hh.selected_member_id ORDER BY hh.Id DESC";
    private static String FIND_ALL_COUNT_QUERY = "SELECT count(*) FROM HOUSEHOLD ORDER BY Id desc";
    private static String FIND_BY_STATUS_COUNT_QUERY = "SELECT count(*) FROM HOUSEHOLD WHERE Status = '%s'";
    public static final String TABLE_NAME = "household";
    public static final String ID = "Id";
    public static final String NAME = "Name";
    public static final String STATUS = "Status";
    public static final String PHONE_NUMBER = "Phone_Number";
    public static final String SELECTED_MEMBER_ID = "selected_member_id";
    public static final String CREATED_AT = "Created_At";
    public static final String COMMENTS = "Comments";
    public static final String SERVER_STATUS = "Server_Status";
    public static final String UNIQUE_DEVICE_ID = "unique_device_id";
    public static final String TABLE_CREATE_QUERY = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)", TABLE_NAME, ID, NAME, PHONE_NUMBER, SELECTED_MEMBER_ID,STATUS, CREATED_AT ,COMMENTS, UNIQUE_DEVICE_ID, SERVER_STATUS);

    String id;
    String name;
    String phoneNumber;
    InterviewStatus status;
    String selectedMemberId;
    String createdAt;
    String comments;
    String uniqueDeviceId;
    Member selectedMember;
    ServerStatus serverStatus;
    
    public Household(String id, String name, String phoneNumber, String selectedMemberId, InterviewStatus status, String createdAt, String uniqueDeviceId, String comments) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.selectedMemberId = selectedMemberId;
        this.status = status;
        this.createdAt=createdAt;
        this.uniqueDeviceId = uniqueDeviceId;
        this.comments=comments;
    }

    public Household(String name, String phoneNumber, InterviewStatus status, String createdAt, String uniqueDeviceId, String comments) {
        this.name= name;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.createdAt=createdAt;
        this.uniqueDeviceId = uniqueDeviceId;
        this.comments = comments;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public InterviewStatus getStatus() {
        return status;
    }

    public void setStatus(InterviewStatus status) {
        this.status = status;
    }

    public void setSelectedMemberId(String selectedMemberId) {
        this.selectedMemberId = selectedMemberId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSelectedMemberId() {
        return selectedMemberId;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Member getSelectedMember(DatabaseHelper db){
        if(selectedMemberId == null)
            return null;
        return findMember(db,Long.parseLong(selectedMemberId));
    }

    public String getUniqueDeviceId() {
        return this.uniqueDeviceId;
    }

    public Member getSelectedMember() {
        return selectedMember;
    }

    public void setSelectedMember(Member member) {
        this.selectedMember = member;
    }

    public ServerStatus getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }

    public long save(DatabaseHelper db){
        ContentValues householdValues = populateWithBasicDetails();
        householdValues.put(CREATED_AT, createdAt);

        long savedId = db.save(householdValues, TABLE_NAME);
        if(savedId != -1)
            id = String.valueOf(savedId);
        return savedId;
    }

    public long update(DatabaseHelper db){
        ContentValues householdValues = populateWithBasicDetails();
        householdValues.put(SELECTED_MEMBER_ID, selectedMemberId);
        return db.update(householdValues, TABLE_NAME,ID +" = ?",new String[]{getId()});
    }

    private ContentValues populateWithBasicDetails() {
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(PHONE_NUMBER,phoneNumber);
        values.put(COMMENTS,comments);
        values.put(STATUS,status.toString());
        values.put(UNIQUE_DEVICE_ID, getUniqueDeviceId());
        values.put(SERVER_STATUS, getServerStatus().toString());
        return values;
    }

    public static Household find_by(DatabaseHelper db, Long id) {
        Cursor cursor = db.exec(String.format(FIND_BY_ID_QUERY, String.valueOf(id)));
        Household household = new CursorHelper().getHouseholds(cursor).get(0);
        db.close();
        return household;
    }

    public static Household find_by(DatabaseHelper db, String name) {
        Cursor cursor = db.exec(String.format(FIND_BY_NAME_QUERY,NAME,name));
        List<Household> households = new CursorHelper().getHouseholds(cursor);
        db.close();
        if(households.isEmpty())
            return null;
        return households.get(0);
    }

    public static List<Household> findByStatus(DatabaseHelper db, InterviewStatus status) {
        Cursor cursor = db.exec(String.format(FIND_BY_STATUS_QUERY, STATUS, status));
        List<Household> households = new CursorHelper().getHouseholds(cursor);
        db.close();
        return  households;
    }

    public static List<Household> findAllExceptServerStatus(DatabaseHelper db, ServerStatus status) {
        Cursor cursor = db.exec(String.format(FIND_BY_SERVER_STATUS_NOT_EQUAL_QUERY, SERVER_STATUS, status));
        List<Household> households = new CursorHelper().getHouseholds(cursor);
        db.close();
        return  households;
    }

    public static List<Household> getAllInOrder(DatabaseHelper db){
        Cursor cursor = db.exec(FIND_ALL_QUERY);
        List<Household> households = new CursorHelper().getHouseholds(cursor);
        db.close();
        Collections.sort(households);
        return households;
    }

    public static int getAllCount(DatabaseHelper db){
        Cursor cursor = db.exec(FIND_ALL_COUNT_QUERY);
        cursor.moveToFirst();
        int householdCounts = cursor.getInt(0);
        db.close();
        return householdCounts;
    }

    public static int getCountByStatus(DatabaseHelper db, InterviewStatus status){
        Cursor cursor = db.exec(String.format(FIND_BY_STATUS_COUNT_QUERY, status));
        cursor.moveToFirst();
        int householdCounts = cursor.getInt(0);
        db.close();
        return householdCounts;
    }

    public List<Member> getAllUnselectedMembers(DatabaseHelper db){
        if(getSelectedMemberId()==null)
            return getAllNonDeletedMembers(db);
        String query = String.format(Member.FIND_ALL_UNSELECTED_QUERY, Member.HOUSEHOLD_ID, this.getId(), Member.DELETED, String.valueOf(Member.NOT_DELETED_INT), Member.ID, getSelectedMemberId());
        return getMembers(db,query);
    }

    public List<Member> getAllNonDeletedMembers(DatabaseHelper db){
        String query = String.format(Member.FIND_ALL_QUERY, Member.HOUSEHOLD_ID, this.getId(), Member.DELETED, String.valueOf(Member.NOT_DELETED_INT), Member.ID, getSelectedMemberId());
        return getMembers(db,query);
    }

    public List<Member> getAllMembersForExport(DatabaseHelper db){
        String query = String.format(Member.FIND_ALL_WITH_DELETED_QUERY, Member.HOUSEHOLD_ID, this.getId());
        return getMembers(db, query);
    }

    public Member findMember(DatabaseHelper db, Long id) {
        String query = String.format(Member.FIND_BY_ID_QUERY, String.valueOf(id));
        return getMembers(db,query).get(0);
    }

    private List<Member> getMembers(DatabaseHelper db, String query) {
        Cursor cursor = db.exec(query);
        List<Member> members = new CursorHelper().getMembers(cursor, this);
        db.close();
        return members;
    }

    public int numberOfNonDeletedMembers(DatabaseHelper db){
        String query = String.format(Member.FIND_ALL_QUERY, Member.HOUSEHOLD_ID, getId(), Member.DELETED, String.valueOf(Member.NOT_DELETED_INT));
        return getCount(db, query);
    }

    public int numberOfNonSelectedMembers(DatabaseHelper db){
        if(getSelectedMemberId()==null)
            return numberOfNonDeletedMembers(db);
        String query = String.format(Member.FIND_ALL_UNSELECTED_QUERY, Member.HOUSEHOLD_ID, this.getId(), Member.DELETED, String.valueOf(Member.NOT_DELETED_INT), Member.ID, getSelectedMemberId());
        return getCount(db, query);
    }

    public int getCount(DatabaseHelper db, String query) {
        Cursor cursor = db.exec(query);
        cursor.moveToFirst();
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int numberOfMembers(DatabaseHelper db){
        Cursor cursor = db.exec(String.format(Member.FIND_ALL_WITH_DELETED_QUERY,Member.HOUSEHOLD_ID,getId()));
        cursor.moveToFirst();
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    @Override
    public int compareTo(Household otherHousehold) {
        return status.getOrderWeight() > otherHousehold.status.getOrderWeight()? 1
                : status.getOrderWeight() < otherHousehold.status.getOrderWeight() ? -1
                : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Household household = (Household) o;

        if (comments != null ? !comments.equals(household.comments) : household.comments != null)
            return false;
        if (!createdAt.equals(household.createdAt)) return false;
        if (!name.equals(household.name)) return false;
        if (phoneNumber != null ? !phoneNumber.equals(household.phoneNumber) : household.phoneNumber != null)
            return false;
        if (selectedMemberId != null ? !selectedMemberId.equals(household.selectedMemberId) : household.selectedMemberId != null)
            return false;
        return status == household.status;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + status.hashCode();
        result = 31 * result + (selectedMemberId != null ? selectedMemberId.hashCode() : 0);
        result = 31 * result + createdAt.hashCode();
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }
}
