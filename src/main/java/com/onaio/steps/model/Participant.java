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


public class Participant implements Serializable {

    public static final String TABLE_NAME = "participant";
    public static final String ID = "Id";
    public static final String PARTICIPANT_ID = "Participant_Id";
    public static final String FIRST_NAME = "first_name";
    public static final String FAMILY_SURNAME = "family_surname";
    public static final String GENDER = "gender";
    public static final String AGE = "age";
    public static final String STATUS = "Status";
    public static final String CREATED_AT="Created_At";


    public static final String TABLE_CREATE_QUERY = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY,%s TEXT,%s Text, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)", TABLE_NAME, ID,PARTICIPANT_ID, FAMILY_SURNAME, FIRST_NAME, AGE, GENDER, STATUS, CREATED_AT);
    public static final String FIND_ALL_QUERY = "SELECT * FROM PARTICIPANT ORDER BY Id asc";
    public static final String FIND_BY_ID_QUERY = "SELECT * FROM PARTICIPANT WHERE " + ID + " = '%s'";
    public static final String FIND_BY_STATUS_QUERY = "SELECT * FROM PARTICIPANT WHERE " + STATUS + " = '%s'";

    private String familySurname;
    private String firstName;
    private String participantID;
    private Gender gender;
    InterviewStatus status;
    private int age;
    private int id;
    private String createdAt;

    public Participant(int id,String participantId, String familySurname, String firstName, Gender gender, int age, InterviewStatus status, String createdAt) {
        this.id = id;
        this.familySurname = familySurname;
        this.firstName = firstName;
        this.participantID=participantId;
        this.gender = gender;
        this.age = age;
        this.status = status;
        this.createdAt = createdAt;
    }


    public Participant(String participantId, String familySurname, String firstName, Gender gender, int age, InterviewStatus status, String createdAt) {
        this.familySurname = familySurname;
        this.firstName = firstName;
        this.participantID=participantId;
        this.gender = gender;
        this.age = age;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getParticipantID() {
        return participantID;
    }

    public void setParticipantID(String participantID) {
        this.participantID = participantID;
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

    public String getFormattedDetail(AppCompatActivity activity) {
        return String.format(gender.getInternationalizedString(activity) +", "+ age);
    }


    public Gender getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public int getId() {
        return id;
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

    public long save(DatabaseHelper db) {
        ContentValues participantDetails = populateBasicDetails();
        participantDetails.put(CREATED_AT,createdAt);
        long savedId = db.save(participantDetails, TABLE_NAME);
        if(savedId!= -1)
            id = (int)savedId;
        return  savedId;
    }

    public long update(DatabaseHelper db) {
        ContentValues participantDetails = populateBasicDetails();
        return db.update(participantDetails, TABLE_NAME, ID + " ="+ id,null);
    }

    private ContentValues populateBasicDetails() {
        ContentValues values = new ContentValues();
        values.put(PARTICIPANT_ID, participantID);
        values.put(FIRST_NAME, firstName);
        values.put(FAMILY_SURNAME, familySurname);
        values.put(AGE, age);
        values.put(GENDER, gender.toString());
        values.put(STATUS ,status.toString());
        return values;
    }

    @Override
    public String toString() {
        return familySurname + " " + firstName;
    }


    public static List<Participant> getAllParticipants(DatabaseHelper db) {
        Cursor cursor = db.exec(Participant.FIND_ALL_QUERY);
        List<Participant> participants = new CursorHelper().getParticipants(cursor);
        db.close();
        return participants;
    }

    public static Participant find_by(DatabaseHelper db, long id) {
        Cursor cursor = db.exec(String.format(Participant.FIND_BY_ID_QUERY, String.valueOf(id)));
        Participant participant = new CursorHelper().getParticipants(cursor).get(0);
        db.close();
        return participant;
    }

    public static List<Participant> findByStatus(DatabaseHelper db, InterviewStatus status) {
        Cursor cursor = db.exec(String.format(FIND_BY_STATUS_QUERY, status));
        List<Participant> participants = new CursorHelper().getParticipants(cursor);
        db.close();
        return participants;
    }

    // for testing purpose
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participant that = (Participant) o;

        if (age != that.age) return false;
        if (!createdAt.equals(that.createdAt)) return false;
        if (!familySurname.equals(that.familySurname)) return false;
        if (!firstName.equals(that.firstName)) return false;
        if (gender != that.gender) return false;
        if (!participantID.equals(that.participantID)) return false;
        return status == that.status;

    }

    @Override
    public int hashCode() {
        int result = familySurname.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + participantID.hashCode();
        result = 31 * result + gender.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + age;
        result = 31 * result + createdAt.hashCode();
        return result;
    }
}


