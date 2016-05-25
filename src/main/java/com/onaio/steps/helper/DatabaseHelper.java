package com.onaio.steps.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdVisit;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.Participant;
import com.onaio.steps.model.ReElectReason;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "householdManager";
    public static final int DATABASE_VERSION = 1;
    private SQLiteDatabase readableDb;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Household.TABLE_CREATE_QUERY);
        db.execSQL(Member.TABLE_CREATE_QUERY);
        db.execSQL(Participant.TABLE_CREATE_QUERY);
        db.execSQL(ReElectReason.TABLE_CREATE_QUERY);
        db.execSQL(HouseholdVisit.TABLE_CREATE_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Household.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Member.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ ReElectReason.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ Participant.TABLE_NAME);
        onCreate(sqLiteDatabase);
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
