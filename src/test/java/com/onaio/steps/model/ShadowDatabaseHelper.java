package com.onaio.steps.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;

@Implements(SQLiteOpenHelper.class)
public class ShadowDatabaseHelper {
    @RealObject
    private SQLiteOpenHelper realHelper;
    private static SQLiteDatabase database;

    private static Context previousContext;
    private String name;

    public void __constructor__(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        this.name = name;
        if (previousContext == null) {
            previousContext = context;
        } else {
            if(previousContext == context) {
                return;
            } else {
                previousContext = context;
            }
        }
        if (database != null) {
            database.close();
        }
        database = null;
    }

    @Implementation
    public synchronized void close() {
        if(previousContext != null)
            return;
        if (database != null) {
            database.close();
        }
        database = null;
    }

    @Implementation
    public synchronized SQLiteDatabase getReadableDatabase() {
        if (database == null) {
            database = SQLiteDatabase.create(null);
            realHelper.onCreate(database);
        }

        realHelper.onOpen(database);
        return database;
    }

    @Implementation
    public synchronized SQLiteDatabase getWritableDatabase() {
        if (database == null) {
            database = SQLiteDatabase.create(null);
            realHelper.onCreate(database);
        }

        realHelper.onOpen(database);
        return database;
    }

    @Implementation
    public String getDatabaseName() {
        return name;
    }
}