package com.onaio.steps.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "householdManager";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Household.TABLE_CREATE_QUERY);
        db.execSQL(Member.TABLE_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Household.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Member.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long save(ContentValues values, String tableName){
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(tableName, null, values);
    }

    public Cursor exec(String query){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(query,null);
    }
}
