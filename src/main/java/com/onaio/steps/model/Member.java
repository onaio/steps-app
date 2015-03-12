package com.onaio.steps.model;

import android.content.ContentValues;

import com.onaio.steps.helper.DatabaseHelper;

public class Member {
    public static final String TABLE_NAME = "member";
    private static final String ID = "Id";
    private static final String NAME = "Name";
    private static final String GENDER = "gender";
    private static final String AGE = "age";
    private static final String HOUSEHOLD_ID = "household_id";
    public static final String TABLE_CREATE_QUERY = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s INTEGER, %s TEXT, %s INTEGER, FOREIGN KEY (%s) REFERENCES %s(%s))", TABLE_NAME, ID, NAME, AGE, GENDER, HOUSEHOLD_ID, HOUSEHOLD_ID, Household.TABLE_NAME, Household.ID);

    private String name;
    private String gender;
    private int age;
    private Household household;

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
        return db.save(values, TABLE_NAME);
    }
}
