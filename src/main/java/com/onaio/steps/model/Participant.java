package com.onaio.steps.model;

import android.content.ContentValues;

import com.onaio.steps.helper.DatabaseHelper;

import java.io.Serializable;


public class Participant  implements  Serializable{

        public static final String TABLE_NAME = "participant";
        public static final String ID = "Id";
        public static final String FIRST_NAME = "first_name";
        public static final String FAMILY_SURNAME = "family_surname";
        public static final String GENDER = "gender";
        public static final String AGE = "age";
        public static final String STATUS = "Status";

        public static final String TABLE_CREATE_QUERY = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY,%s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT)", TABLE_NAME, ID,FAMILY_SURNAME,FIRST_NAME, AGE, GENDER,STATUS);
        public static final String FIND_ALL_QUERY = "SELECT * FROM PARTICIPANT WHERE %s=%s and %s=%d ORDER BY Id asc";
        public static final String FIND_BY_ID_QUERY = "SELECT * FROM PARTICIPANT WHERE "+ID+" = '%d'";

        private String familySurname;
        private String firstName;
        private Gender gender;
        ParticipantStatus status;
        private int age;
        private int id;

        public Participant(int id, String familySurname, String firstName, Gender gender, int age ,ParticipantStatus status) {
            this.familySurname = familySurname;
            this.firstName = firstName;
            this.gender = gender;
            this.age = age;
            this.id = id;
            this.status=status;

        }

        public String getFirstName() {
            return firstName;
        }

        public String getFamilySurname() {
            return familySurname;
        }

        public String getFormattedName(){
            return String.format("%s %s",familySurname,firstName);
        }

        public String getFormattedDetail(){
            return String.format(gender +", "+ age);
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

        public long save(DatabaseHelper db) {
            ContentValues participantDetails = populateBasicDetails();
            long savedId = db.save(participantDetails, TABLE_NAME);
            if(savedId!= -1)
                id = (int) savedId;
            return savedId;
        }

        public long update(DatabaseHelper db){
            ContentValues memberDetails = populateBasicDetails();
            return db.update(memberDetails,TABLE_NAME,ID+" = "+id,null);
        }

        private ContentValues populateBasicDetails() {
            ContentValues values = new ContentValues();
            values.put(FIRST_NAME, firstName);
            values.put(FAMILY_SURNAME, familySurname);
            values.put(GENDER,gender.toString());
            values.put(AGE,age);
            values.put(STATUS, status.toString());
            return values;
        }

        @Override
        public String toString() {
            return familySurname + " "+ firstName;
        }




    }


