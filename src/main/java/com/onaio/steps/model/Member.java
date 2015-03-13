package com.onaio.steps.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.onaio.steps.helper.DatabaseHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Member implements Serializable {
    public static final String TABLE_NAME = "member";
    private static final String ID = "Id";
    private static final String NAME = "Name";
    private static final String GENDER = "gender";
    private static final String AGE = "age";
    private static final String HOUSEHOLD_ID = "household_id";
    public static final String TABLE_CREATE_QUERY = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s INTEGER, %s TEXT, %s INTEGER, FOREIGN KEY (%s) REFERENCES %s(%s))", TABLE_NAME, ID, NAME, AGE, GENDER, HOUSEHOLD_ID, HOUSEHOLD_ID, Household.TABLE_NAME, Household.ID);
    private static String FIND_ALL_QUERY = "SELECT * FROM MEMBER WHERE %s=%s ORDER BY Id desc";
    private static String FIND_BY_NAME_QUERY = "SELECT * FROM MEMBER WHERE name = '%s' LIMIT 1";

    private String name;
    private String gender;
    private int age;
    private Household household;
    private int id;

    public Member(int id, String name, String gender, int age, Household household) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.household = household;
        this.id = id;
    }

    public Member(String name, String gender, int age, Household household) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.household = household;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public Household getHousehold() {
        return household;
    }

    public long save(DatabaseHelper db) {
        ContentValues values = new ContentValues();
        values.put(NAME, getName());
        values.put(GENDER,getGender());
        values.put(AGE,getAge());
        values.put(HOUSEHOLD_ID,household.getId());
        return db.save(values, TABLE_NAME);
    }

    public static List<Member> getAll(DatabaseHelper db, Household household){
        Cursor cursor = db.exec(String.format(FIND_ALL_QUERY,HOUSEHOLD_ID,household.getId()));
        return read(cursor,household);
    }

    private static List<Member> read(Cursor cursor, Household household) {
        List<Member> members = new ArrayList<Member>();
        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(cursor.getColumnIndex(NAME));
                String gender = cursor.getString(cursor.getColumnIndex(GENDER));
                String age = cursor.getString(cursor.getColumnIndex(AGE));
                String id = cursor.getString(cursor.getColumnIndex(ID));
                if(household.getId().equals(cursor.getString(cursor.getColumnIndex(HOUSEHOLD_ID))))
                    members.add(new Member(Integer.parseInt(id), name, gender, Integer.parseInt(age), household));
            }while (cursor.moveToNext());
        }
        return members;
    }

    public static Member find_by(DatabaseHelper db, String name, Household household) {
        Cursor cursor = db.exec(String.format(FIND_BY_NAME_QUERY,name));
        return read(cursor,household).get(0);
    }
}
