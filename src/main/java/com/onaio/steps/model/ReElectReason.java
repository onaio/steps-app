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

import com.onaio.steps.helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ReElectReason {
    private static String FIND_ALL_QUERY = "SELECT * FROM REASON WHERE %s=%s ORDER BY Id asc";
    private static String FIND_COUNT_QUERY = "SELECT count(*) FROM REASON WHERE %s=%s ORDER BY Id asc";
    public static final String TABLE_NAME = "reason";
    public static final String ID = "Id";
    private static final String REASON = "reason";
    private static final String HOUSEHOLD_ID = "household_id";
    public static final String TABLE_CREATE_QUERY = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s INTEGER, FOREIGN KEY (%s) REFERENCES %s(%s))", TABLE_NAME, ID, REASON, HOUSEHOLD_ID, HOUSEHOLD_ID, Household.TABLE_NAME, Household.ID);

    String id;
    String reason;
    Household household;

    public ReElectReason(String reason, Household household) {
        this.reason = reason;
        this.household = household;
    }

    public ReElectReason(String id, String reason, Household household) {
        this.id = id;
        this.reason = reason;
        this.household = household;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long save(DatabaseHelper db){
        ContentValues values = new ContentValues();
        if(! reason.isEmpty()) {
            values.put(ID, id);
            values.put(REASON, reason);
            values.put(HOUSEHOLD_ID, household.getId());
            return db.save(values, TABLE_NAME);
        }
        return (long)-1;
    }

    public static List<ReElectReason> getAll(DatabaseHelper db, Household household){
        Cursor cursor = db.exec(String.format(FIND_ALL_QUERY,HOUSEHOLD_ID,household.getId()));
        List<ReElectReason> reasons = read(cursor,household);
        db.close();
        return reasons;
    }

    public static int count(DatabaseHelper db, Household household){
        Cursor cursor = db.exec(String.format(FIND_ALL_QUERY, HOUSEHOLD_ID, household.getId()));
        int count = cursor.getCount();
        db.close();
        cursor.close();
        return count;
    }

    private static List<ReElectReason> read(Cursor cursor, Household household) {
        List<ReElectReason> reasons = new ArrayList<ReElectReason>();
        if(cursor.moveToFirst()){
            do{
                String reason = cursor.getString(cursor.getColumnIndex(REASON));
                String id = cursor.getString(cursor.getColumnIndex(ID));
                String household_id = cursor.getString(cursor.getColumnIndex(HOUSEHOLD_ID));
                if (household.getId().equals(household_id))
                    reasons.add(new ReElectReason(id, reason, household));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return reasons;
    }

    @Override
    public String toString() {
        return reason;
    }
}
