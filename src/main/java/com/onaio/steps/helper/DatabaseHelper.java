package com.onaio.steps.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.onaio.steps.model.Household;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "householdManager";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_HOUSEHOLD = "household";
    public static final String HOUSEHOLD_ID = "Id";
    public static final String HOUSEHOLD_NAME = "Name";
    public static final String HOUSEHOLD_PHONE_NUMBER = "Phone_Number";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    private final String HOUSEHOLD_CREATE_QUERY = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s INTEGER)",TABLE_HOUSEHOLD,HOUSEHOLD_ID,HOUSEHOLD_NAME,HOUSEHOLD_PHONE_NUMBER);
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HOUSEHOLD_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_HOUSEHOLD);

        onCreate(sqLiteDatabase);
    }

    public void createHousehold(Household household){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HOUSEHOLD_NAME,household.getName());
        values.put(HOUSEHOLD_PHONE_NUMBER,household.getPhoneNumber());

        long household_id = db.insert(TABLE_HOUSEHOLD, null, values);
    }

    public List<Household> getHouseholds() {
        SQLiteDatabase db = getReadableDatabase();
        String houseHOldQuery = "SELECT * FROM HOUSEHOLD";

        Cursor cursor = db.rawQuery(houseHOldQuery, null);
        List<Household> householdNames = new ArrayList<Household>();
        if(cursor.moveToFirst()){
            do{
                String household_name = cursor.getString(cursor.getColumnIndex(HOUSEHOLD_NAME));
                String household_number = cursor.getString(cursor.getColumnIndex(HOUSEHOLD_PHONE_NUMBER));
                householdNames.add(new Household(household_name,Integer.parseInt(household_number)));
            }while (cursor.moveToNext());
        }
        return householdNames;
    }
}
