package com.onaio.steps.utils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.model.ODKForm.ODKSavedForm;

public class Faker {

    public static void mockQueryInActivityToFindOdkBlankForm(AppCompatActivity activity) throws RemoteException {
        ContentResolver contentResolver = mock(ContentResolver.class);
        ContentProviderClient contentProviderClient = mock(ContentProviderClient.class);
        Cursor cursor = mock(Cursor.class);

        when(activity.getContentResolver()).thenReturn(contentResolver);
        when(contentResolver.acquireContentProviderClient(any(Uri.class))).thenReturn(contentProviderClient);
        when(contentProviderClient.query(any(Uri.class), nullable(String[].class), anyString(), any(String[].class), nullable(String.class))).thenReturn(cursor);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.getColumnIndex("_id")).thenReturn(0);
        when(cursor.getColumnIndex("jrFormId")).thenReturn(1);
        when(cursor.getColumnIndex("displayName")).thenReturn(2);
        when(cursor.getColumnIndex("jrVersion")).thenReturn(3);
        when(cursor.getColumnIndex("formMediaPath")).thenReturn(4);
        when(cursor.getString(0)).thenReturn("id");
        when(cursor.getString(1)).thenReturn("jrFormId");
        when(cursor.getString(2)).thenReturn("displayName");
        when(cursor.getString(3)).thenReturn("jrVersion");
        when(cursor.getString(4)).thenReturn("path");
    }

    public static void mockQueryInActivityToFindOdkSavedForm(AppCompatActivity activity) throws RemoteException {
        ContentResolver contentResolver = mock(ContentResolver.class);
        ContentProviderClient contentProviderClient = mock(ContentProviderClient.class);
        Cursor cursor = mock(Cursor.class);

        when(activity.getContentResolver()).thenReturn(contentResolver);
        when(contentResolver.acquireContentProviderClient(eq(ODKSavedForm.URI))).thenReturn(contentProviderClient);
        when(contentProviderClient.query(eq(ODKSavedForm.URI), eq(null), eq("_id = ?"), any(String[].class), eq(null))).thenReturn(cursor);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.getColumnIndex("_id")).thenReturn(0);
        when(cursor.getColumnIndex("jrFormId")).thenReturn(1);
        when(cursor.getColumnIndex("displayName")).thenReturn(2);
        when(cursor.getColumnIndex("jrVersion")).thenReturn(3);
        when(cursor.getColumnIndex("instanceFilePath")).thenReturn(4);
        when(cursor.getColumnIndex("status")).thenReturn(5);
        when(cursor.getString(0)).thenReturn("id");
        when(cursor.getString(1)).thenReturn("jrFormId");
        when(cursor.getString(2)).thenReturn("displayName");
        when(cursor.getString(3)).thenReturn("jrVersion");
        when(cursor.getString(4)).thenReturn("path");
        when(cursor.getString(5)).thenReturn("complete");
    }
}