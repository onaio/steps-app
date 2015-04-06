package com.onaio.steps.model;

import android.content.ContentValues;

import com.onaio.steps.helper.DatabaseHelper;

import java.io.Serializable;

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
    public static final String FIND_ALL_QUERY = "SELECT * FROM MEMBER WHERE %s=%s and %s=%d ORDER BY Id asc";
    public static final String FIND_ALL_WITH_DELETED_QUERY = "SELECT * FROM MEMBER WHERE %s=%s ORDER BY Id asc";
    public static final String FIND_BY_ID_QUERY = "SELECT * FROM MEMBER WHERE "+ID+" = '%d'";
    public static final int NOT_DELETED_INT = 0;
    public static final int DELETED_INT = 1;
    private final int DELTA = 1;

    private String familySurname;
    private String firstName;
    private String gender;
    private int age;
    private Household household;
    private int id;
    private String memberHouseholdId;
    private Boolean deleted;

    public Member(int id, String familySurname, String firstName, String gender, int age, Household household, String memberHouseholdId, Boolean deleted) {
        this.familySurname = familySurname;
        this.firstName = firstName;
        this.gender = gender;
        this.age = age;
        this.household = household;
        this.id = id;
        this.memberHouseholdId = memberHouseholdId;
        this.deleted = deleted;
    }

    public Member(String familySurname, String firstName, String gender, int age, Household household) {
        this.familySurname = familySurname;
        this.firstName = firstName;
        this.gender = gender;
        this.age = age;
        this.household = household;
        deleted = false;
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

    public String getGender() {
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

    public long save(DatabaseHelper db) {
        int memberNumber = household.numberOfMembers(db) + DELTA;
        String generatedId = household.getName() + "-" + memberNumber;
        ContentValues memberDetails = populateBasicDetails();
        memberDetails.put(MEMBER_HOUSEHOLD_ID, generatedId);
        return db.save(memberDetails, TABLE_NAME);
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
        values.put(GENDER,gender);
        values.put(AGE,age);
        values.put(HOUSEHOLD_ID,household.getId());
        values.put(DELETED,deleted ? DELETED_INT : NOT_DELETED_INT);
        return values;
    }

    @Override
    public String toString() {
        return familySurname + " "+ firstName;
    }
}
