package com.onaio.steps.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.onaio.steps.helper.DatabaseHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Household implements Serializable {
    private static String FIND_BY_ID_QUERY = "SELECT * FROM HOUSEHOLD WHERE id = %d";
    public static String FIND_ALL_QUERY = "SELECT * FROM HOUSEHOLD ORDER BY Id desc";
    public static String FIND_ALL_COUNT_QUERY = "SELECT count(*) FROM HOUSEHOLD ORDER BY Id desc";
    public static final String TABLE_NAME = "household";
    public static final String ID = "Id";
    private static final String NAME = "Name";
    private static final String STATUS = "Status";
    private static final String PHONE_NUMBER = "Phone_Number";
    private static final String SELECTED_MEMBER = "selected_member";
    private static final String CREATED_AT = "Created_At";
    public static final String TABLE_CREATE_QUERY = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT)", TABLE_NAME, ID, NAME, PHONE_NUMBER,SELECTED_MEMBER,STATUS, CREATED_AT);

    String id;
    String name;
    String phoneNumber;
    HouseholdStatus status;
    String selectedMember;
    String createdAt;
    
    public Household(String id, String name, String phoneNumber, String selectedMember, HouseholdStatus status, String createdAt) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.selectedMember = selectedMember;
        this.status = status;
        this.createdAt=createdAt;
    }

    public Household(String name, String phoneNumber, HouseholdStatus status, String createdAt) {
        this.name= name;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.createdAt=createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public HouseholdStatus getStatus() {
        return status;
    }

    public void setStatus(HouseholdStatus status) {
        this.status = status;
    }

    public void setSelectedMember(String selectedMember) {
        this.selectedMember = selectedMember;
    }

    public String getSelectedMember() {
        return selectedMember;
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

    public long save(DatabaseHelper db){
        ContentValues householdValues = populateWithBasicDetails();
        householdValues.put(CREATED_AT, createdAt);
        return db.save(householdValues, TABLE_NAME);
    }

    public long update(DatabaseHelper db){
        ContentValues householdValues = populateWithBasicDetails();
        householdValues.put(SELECTED_MEMBER, selectedMember);
        return db.update(householdValues, TABLE_NAME,ID +" = "+getId(),null);
    }

    private ContentValues populateWithBasicDetails() {
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(PHONE_NUMBER,phoneNumber);
        values.put(STATUS,status.toString());
        return values;
    }

    public static Household find_by(DatabaseHelper db, Long id) {
        Cursor cursor = db.exec(String.format(FIND_BY_ID_QUERY,id));
        Household household = read(cursor).get(0);
        db.close();
        return household;
    }

    public static List<Household> getAll(DatabaseHelper db){
        Cursor cursor = db.exec(FIND_ALL_QUERY);
        List<Household> households = read(cursor);
        db.close();
        return households;
    }

    public static int getAllCount(DatabaseHelper db){
        Cursor cursor = db.exec(FIND_ALL_COUNT_QUERY);
        cursor.moveToFirst();
        int householdCounts = cursor.getInt(0);
        db.close();
        return householdCounts;
    }

    private static List<Household> read(Cursor cursor) {
        List<Household> householdNames = new ArrayList<Household>();
        if(cursor.moveToFirst()){
            do{
                String household_name = cursor.getString(cursor.getColumnIndex(NAME));
                String household_number = cursor.getString(cursor.getColumnIndex(PHONE_NUMBER));
                String id = cursor.getString(cursor.getColumnIndex(ID));
                String selectedMember = cursor.getString(cursor.getColumnIndex(SELECTED_MEMBER));
                String status = cursor.getString(cursor.getColumnIndex(STATUS));
                String createdAt = cursor.getString(cursor.getColumnIndex(CREATED_AT));
                householdNames.add(new Household(id,household_name, household_number,selectedMember,HouseholdStatus.valueOf(status),createdAt ));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return householdNames;
    }
}
