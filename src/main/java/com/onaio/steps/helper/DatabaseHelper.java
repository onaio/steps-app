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

package com.onaio.steps.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.onaio.steps.BuildConfig;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.Participant;
import com.onaio.steps.model.ReElectReason;
import com.onaio.steps.model.ServerStatus;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "householdManager";
    public static final int DATABASE_VERSION = 2;
    private SQLiteDatabase readableDb;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 1) {
            purgeTables(db);
            onCreate(db);

        } else if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + Household.TABLE_NAME + " ADD COLUMN " + Household.SERVER_STATUS + " TEXT default '" + ServerStatus.NOT_SENT + "'");
            db.execSQL("ALTER TABLE " + Household.TABLE_NAME + " ADD COLUMN " + Household.ODK_FORM_ID + " TEXT default null");
            db.execSQL("ALTER TABLE " + Household.TABLE_NAME + " ADD COLUMN " + Household.ODK_JR_FORM_ID + " TEXT default '" + BuildConfig.JR_FORM_ID + "'");
            db.execSQL("ALTER TABLE " + Household.TABLE_NAME + " ADD COLUMN " + Household.ODK_JR_FORM_TITLE + " TEXT default '" + BuildConfig.JR_FORM_TITLE + "'");
            db.execSQL("ALTER TABLE " + Participant.TABLE_NAME + " ADD COLUMN " + Participant.ODK_FORM_ID + " TEXT default null");
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    private void createTables(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Household.TABLE_CREATE_QUERY);
        sqLiteDatabase.execSQL(Member.TABLE_CREATE_QUERY);
        sqLiteDatabase.execSQL(Participant.TABLE_CREATE_QUERY);
        sqLiteDatabase.execSQL(ReElectReason.TABLE_CREATE_QUERY);
    }

    private void purgeTables(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Household.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Member.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ ReElectReason.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ Participant.TABLE_NAME);
    }

    /**
     * This method erases all the data in the database but leaves the schemas intact
     */
    public void truncate() {
        SQLiteDatabase db = getWritableDatabase();
        purgeTables(db);
        createTables(db);
    }

    public long save(ContentValues values, String tableName){
        SQLiteDatabase db = getWritableDatabase();
        long insert = db.insert(tableName, null, values);
        db.close();
        return insert;
    }

    public long update(ContentValues values, String tableName,String whereCondition, String[] args){
        SQLiteDatabase db = getWritableDatabase();
        int update = db.update(tableName, values, whereCondition, args);
        db.close();
        return update;
    }

    public Cursor exec(String query){
        readableDb = getReadableDatabase();
        return readableDb.rawQuery(query, null);
    }

    public void close(){
        if(readableDb!=null && readableDb.isOpen())
            readableDb.close();
    }
}
