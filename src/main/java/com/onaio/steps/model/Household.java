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
    public static final String TABLE_NAME = "household";
    public static final String ID = "Id";
    private static final String NAME = "Name";
    private static final String PHONE_NUMBER = "Phone_Number";
    private static final String SELECTED_MEMBER = "selected_member";
    public static final String TABLE_CREATE_QUERY = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s INTEGER)", TABLE_NAME, ID, NAME, PHONE_NUMBER,SELECTED_MEMBER);

    String id;
    String name;
    String phoneNumber;

    public void setSelectedMember(String selectedMember) {
        this.selectedMember = selectedMember;
    }

    String selectedMember;

    public String getSelectedMember() {
        return selectedMember;
    }

    public Household(String id, String name, String phoneNumber, String selectedMember) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.selectedMember = selectedMember;
    }

    public Household(String name, String phoneNumber) {
        this.name= name;
        this.phoneNumber = phoneNumber;
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
        ContentValues values = new ContentValues();
        values.put(NAME, getName());
        values.put(PHONE_NUMBER,getPhoneNumber());
        return db.save(values, TABLE_NAME);
    }

    public long update(DatabaseHelper db){
        ContentValues values = new ContentValues();
        values.put(NAME, getName());
        values.put(PHONE_NUMBER,getPhoneNumber());
        values.put(SELECTED_MEMBER,getSelectedMember());
        return db.update(values, TABLE_NAME,ID +" = "+getId(),null);
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

    private static List<Household> read(Cursor cursor) {
        List<Household> householdNames = new ArrayList<Household>();
        if(cursor.moveToFirst()){
            do{
                String household_name = cursor.getString(cursor.getColumnIndex(NAME));
                String household_number = cursor.getString(cursor.getColumnIndex(PHONE_NUMBER));
                String id = cursor.getString(cursor.getColumnIndex(ID));
                String selectedMember = cursor.getString(cursor.getColumnIndex(SELECTED_MEMBER));
                householdNames.add(new Household(id,household_name, household_number,selectedMember));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return householdNames;
    }
}
