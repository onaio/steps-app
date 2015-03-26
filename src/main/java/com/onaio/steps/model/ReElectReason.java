package com.onaio.steps.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.onaio.steps.helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ReElectReason {
    private static String FIND_ALL_QUERY = "SELECT * FROM REASON WHERE %s=%s ORDER BY Id asc";
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
        values.put(ID, id);
        values.put(REASON,reason);
        values.put(HOUSEHOLD_ID,household.getId());
        return db.save(values, TABLE_NAME);
    }

    public static List<ReElectReason> getAll(DatabaseHelper db, Household household){
        Cursor cursor = db.exec(String.format(FIND_ALL_QUERY,HOUSEHOLD_ID,household.getId()));
        List<ReElectReason> reasons = read(cursor,household);
        db.close();
        return reasons;
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
