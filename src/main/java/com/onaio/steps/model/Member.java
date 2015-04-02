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
    private static final String MEMBER_HOUSEHOLD_ID = "member_household_id";
    private static final String FIRST_NAME = "first_name";
    private static final String FAMILY_SURNAME = "family_surname";
    private static final String GENDER = "gender";
    private static final String AGE = "age";
    private static final String HOUSEHOLD_ID = "household_id";
    private static final String DELETED = "deleted";
    public static final String TABLE_CREATE_QUERY = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT,%s TEXT, %s INTEGER, %s TEXT, %s INTEGER, %s INTEGER, FOREIGN KEY (%s) REFERENCES %s(%s))", TABLE_NAME, ID, MEMBER_HOUSEHOLD_ID,FAMILY_SURNAME,FIRST_NAME, AGE, GENDER,DELETED, HOUSEHOLD_ID, HOUSEHOLD_ID, Household.TABLE_NAME, Household.ID);
    private static String FIND_ALL_QUERY = "SELECT * FROM MEMBER WHERE %s=%s and %s=%d ORDER BY Id asc";
    private static String FIND_ALL_WITH_DELETED_QUERY = "SELECT * FROM MEMBER WHERE %s=%s ORDER BY Id asc";
    private static String FIND_BY_ID_QUERY = "SELECT * FROM MEMBER WHERE "+ID+" = '%d'";
    private static int NOT_DELETED_INT = 0;

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
        int memberNumber = Member.numberOfMembers(db, household) + 1;
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
        values.put(DELETED,deleted ? 1 : 0);
        return values;
    }

    public static int numberOfNonDeletedMembers(DatabaseHelper db, Household household){
        Cursor cursor = db.exec(String.format(FIND_ALL_QUERY,HOUSEHOLD_ID,household.getId(),DELETED, NOT_DELETED_INT));
        cursor.moveToFirst();
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public static int numberOfMembers(DatabaseHelper db, Household household){
        Cursor cursor = db.exec(String.format(FIND_ALL_WITH_DELETED_QUERY,HOUSEHOLD_ID,household.getId()));
        cursor.moveToFirst();
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public static List<Member> getAll(DatabaseHelper db, Household household){
        Cursor cursor = db.exec(String.format(FIND_ALL_QUERY,HOUSEHOLD_ID,household.getId(),DELETED,NOT_DELETED_INT));
        List<Member> members = read(cursor, household);
        db.close();
        return members;
    }

    public static List<Member> getAllForExport(DatabaseHelper db, Household household){
        Cursor cursor = db.exec(String.format(FIND_ALL_WITH_DELETED_QUERY,HOUSEHOLD_ID,household.getId()));
        List<Member> members = read(cursor, household);
        db.close();
        return members;
    }

    private static List<Member> read(Cursor cursor, Household household) {
        List<Member> members = new ArrayList<Member>();
        if(cursor.moveToFirst()){
            do{
                String familySurname = cursor.getString(cursor.getColumnIndex(FAMILY_SURNAME));
                String firstName = cursor.getString(cursor.getColumnIndex(FIRST_NAME));
                String gender = cursor.getString(cursor.getColumnIndex(GENDER));
                String age = cursor.getString(cursor.getColumnIndex(AGE));
                String id = cursor.getString(cursor.getColumnIndex(ID));
                String generatedId = cursor.getString(cursor.getColumnIndex(MEMBER_HOUSEHOLD_ID));
                int deletedInteger = cursor.getInt(cursor.getColumnIndex(DELETED));
                boolean deleted = deletedInteger == NOT_DELETED_INT ? false : true;
                if(household.getId().equals(cursor.getString(cursor.getColumnIndex(HOUSEHOLD_ID))))
                    members.add(new Member(Integer.parseInt(id), familySurname,firstName, gender, Integer.parseInt(age), household,generatedId, deleted));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return members;
    }

    public static Member find_by(DatabaseHelper db, Long id, Household household) {
        Cursor cursor = db.exec(String.format(FIND_BY_ID_QUERY, id));
        Member member = read(cursor, household).get(0);
        db.close();
        return member;
    }

    @Override
    public String toString() {
        return familySurname + " "+ firstName;
    }
}
