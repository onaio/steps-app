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