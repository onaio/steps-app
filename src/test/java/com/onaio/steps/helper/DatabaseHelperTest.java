package com.onaio.steps.helper;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Participant;
import com.onaio.steps.model.ServerStatus;

import org.junit.Before;
import org.junit.Test;

public class DatabaseHelperTest extends StepsTestRunner {

    private DatabaseHelper dbHelper;
    private static final String TABLE_NAME = "household";

    @Before
    public void setUp() {
        dbHelper = new DatabaseHelper(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void testAddColumnIfNotExistShouldAddColumnIfNotExists() {
        SQLiteDatabase db = mock(SQLiteDatabase.class);
        Cursor cursor = mock(Cursor.class);

        String columnServerStatus = "Server_Status";
        String alterQuery = String.format("ALTER TABLE %s ADD COLUMN %s TEXT default '" + ServerStatus.NOT_SENT + "'", TABLE_NAME, columnServerStatus);

        when(db.rawQuery(anyString(), nullable(String[].class))).thenReturn(cursor);
        when(cursor.moveToFirst()).thenReturn(false);

        dbHelper.addColumnIfNotExist(db, TABLE_NAME, columnServerStatus, alterQuery);

        verify(db, times(1)).execSQL(eq(alterQuery));
    }

    @Test
    public void testIsColumnExistShouldReturnTrueWhenColumnExist() {
        SQLiteDatabase db = mock(SQLiteDatabase.class);
        Cursor cursor = mock(Cursor.class);

        String columnServerStatus = "Server_Status";

        when(db.rawQuery(anyString(), nullable(String[].class))).thenReturn(cursor);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.getString(anyInt())).thenReturn(columnServerStatus);

        boolean isColumnExist = dbHelper.isColumnExist(db, TABLE_NAME, columnServerStatus);

        assertTrue(isColumnExist);
        verify(cursor, times(1)).moveToFirst();
        verify(cursor, times(1)).getString(eq(1));
        verify(cursor, times(1)).close();
        verify(db, times(1)).rawQuery(eq("PRAGMA table_info(" + TABLE_NAME + ")"), eq(null));
    }

    @Test
    public void testDropTableShouldDropGivenTable() {
        SQLiteDatabase db = mock(SQLiteDatabase.class);
        dbHelper.dropTable(db, Household.TABLE_NAME, Participant.TABLE_NAME);
        verify(db, times(1)).execSQL(String.format("DROP TABLE IF EXISTS %s", Household.TABLE_NAME));
        verify(db, times(1)).execSQL(String.format("DROP TABLE IF EXISTS %s", Participant.TABLE_NAME));
    }
}
