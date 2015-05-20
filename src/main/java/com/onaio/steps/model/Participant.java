package com.onaio.steps.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.onaio.steps.helper.CursorHelper;
import com.onaio.steps.helper.DatabaseHelper;

import java.io.Serializable;
import java.util.ArrayList;


public class Participant implements Serializable {

    public static final String TABLE_NAME = "participant";
    public static final String ID = "Id";
    public static final String FIRST_NAME = "first_name";
    public static final String FAMILY_SURNAME = "family_surname";
    public static final String GENDER = "gender";
    public static final String AGE = "age";
    public static final String STATUS = "Status";
    public static final String CREATED_AT="Created_At";

    public static final String TABLE_CREATE_QUERY = String.format("CREATE TABLE %s(%s TEXT PRIMARY KEY,%s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)", TABLE_NAME, ID, FAMILY_SURNAME, FIRST_NAME, AGE, GENDER, STATUS, CREATED_AT);
    public static final String FIND_ALL_QUERY = "SELECT * FROM PARTICIPANT ORDER BY Id asc";
    public static final String FIND_BY_ID_QUERY = "SELECT * FROM PARTICIPANT WHERE " + ID + " = '%d'";

    private String familySurname;
    private String firstName;
    private Gender gender;
    ParticipantStatus status;
    private int age;
    private String id;
    private String createdAt;

    public Participant(String id, String familySurname, String firstName, Gender gender, int age, ParticipantStatus status, String createdAt) {
        this.familySurname = familySurname;
        this.firstName = firstName;
        this.gender = gender;
        this.age = age;
        this.id = id;
        this.status = status;
        this.createdAt = createdAt;

    }

    public String getFirstName() {
        return firstName;
    }

    public String getFamilySurname() {
        return familySurname;
    }

    public String getFormattedName() {
        return String.format("%s %s", familySurname, firstName);
    }

    public String getFormattedDetail() {
        return String.format(gender + ", " + age);
    }


    public Gender getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getId() {
        return id;
    }

    public ParticipantStatus getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setStatus(ParticipantStatus status){
        this.status=status;
    }

    public long save(DatabaseHelper db) {
        ContentValues participantDetails = populateBasicDetails();
        participantDetails.put(CREATED_AT,createdAt);
        long savedId = db.save(participantDetails, TABLE_NAME);
        if (savedId != -1)
            id = String.valueOf(savedId);
        return savedId;
    }

    public long update(DatabaseHelper db) {
        ContentValues participantDetails = populateBasicDetails();
        return db.update(participantDetails, TABLE_NAME, ID + " =?", new String[]{getId()});
    }

    private ContentValues populateBasicDetails() {
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(FIRST_NAME, firstName);
        values.put(FAMILY_SURNAME, familySurname);
        values.put(GENDER, gender.toString());
        values.put(AGE, age);
        values.put(STATUS, status.toString());
        return values;
    }

    @Override
    public String toString() {
        return familySurname + " " + firstName;
    }


    public static ArrayList<Participant> getAllParticipants(DatabaseHelper db) {
        Cursor cursor = db.exec(Participant.FIND_ALL_QUERY);
        ArrayList<Participant> participants = new CursorHelper().getParticipants(cursor);
        db.close();
        return participants;
    }

    public static Participant find_by(DatabaseHelper db, long id) {
        Cursor cursor = db.exec(String.format(Participant.FIND_BY_ID_QUERY, id));
        Participant participant = new CursorHelper().getParticipants(cursor).get(0);
        db.close();
        return participant;
    }



}


