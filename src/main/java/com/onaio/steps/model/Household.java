package com.onaio.steps.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.onaio.steps.helper.DatabaseHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Household implements Serializable {
    private static String FIND_BY_NAME_QUERY = "SELECT * FROM HOUSEHOLD WHERE name = '%s' LIMIT 1";
    private static String FIND_ALL_QUERY = "SELECT * FROM HOUSEHOLD ORDER BY Id desc";
    public static final String TABLE_NAME = "household";
    public static final String ID = "Id";
    private static final String NAME = "Name";
    private static final String PHONE_NUMBER = "Phone_Number";
    public static final String TABLE_CREATE_QUERY = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s INTEGER)", TABLE_NAME, ID, NAME, PHONE_NUMBER);

    String id;
    String name;
    long phoneNumber;

    public Household(String id, String name, long phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Household(String name, long phoneNumber) {
        this.name= name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long save(DatabaseHelper db){
        ContentValues values = new ContentValues();
        values.put(NAME, getName());
        values.put(PHONE_NUMBER,getPhoneNumber());
        return db.save(values, TABLE_NAME);
    }

    public static Household find_by(DatabaseHelper db, String name) {
        Cursor cursor = db.exec(String.format(FIND_BY_NAME_QUERY,name));
        return read(cursor).get(0);
    }

    public static List<Household> all(DatabaseHelper db){
        Cursor cursor = db.exec(FIND_ALL_QUERY);
        return read(cursor);
    }

    private static List<Household> read(Cursor cursor) {
        List<Household> householdNames = new ArrayList<Household>();
        if(cursor.moveToFirst()){
            do{
                String household_name = cursor.getString(cursor.getColumnIndex(NAME));
                String household_number = cursor.getString(cursor.getColumnIndex(PHONE_NUMBER));
                String id = cursor.getString(cursor.getColumnIndex(ID));
                householdNames.add(new Household(id,household_name, Integer.parseInt(household_number)));
            }while (cursor.moveToNext());
        }
        return householdNames;
    }
}
