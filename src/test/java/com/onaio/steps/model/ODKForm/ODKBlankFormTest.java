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

package com.onaio.steps.model.ODKForm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.exceptions.AppNotInstalledException;
import com.onaio.steps.exceptions.FormNotPresentException;
import com.onaio.steps.model.ShadowDatabaseHelper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.annotation.Config;

@Config(shadows = {ShadowDatabaseHelper.class})
public class ODKBlankFormTest extends StepsTestRunner {

    private ODKBlankForm form;

    @Before
    public void Setup(){
        form = new ODKBlankForm("id", "jrFormId", "displayName", "jrVersion", "path");
    }

    @Test
    public void ShouldGetTheFormURIWithId(){
        assertEquals("content://org.odk.collect.android.provider.odk.forms/forms/id",form.getUri().toString());
    }

    @Test
    public void ShouldValidateURIWithoutIdButShouldNotHaveOnlyThat(){
        String uriWithoutID = "content://org.odk.collect.android.provider.odk.forms/forms";

        Assert.assertTrue(form.getUri().toString().contains(uriWithoutID));
        assertNotEquals(form.getUri().toString(), uriWithoutID);
    }

    @Test
    public void testFindShouldReturnOneEmptyForm() throws RemoteException, FormNotPresentException, AppNotInstalledException {
        AppCompatActivity activity = mock(AppCompatActivity.class);
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
        when(cursor.getString(0)).thenReturn(form._id);
        when(cursor.getString(1)).thenReturn(form.jrFormId);
        when(cursor.getString(2)).thenReturn(form.displayName);
        when(cursor.getString(3)).thenReturn(form.jrVersion);
        when(cursor.getString(4)).thenReturn(form.formMediaPath);

        ODKBlankForm odkBlankForm = (ODKBlankForm) ODKBlankForm.find(activity, "");

        assertEquals(form._id, odkBlankForm._id);
        assertEquals(form.jrFormId, odkBlankForm.jrFormId);
        assertEquals(form.displayName, odkBlankForm.displayName);
        assertEquals(form.jrVersion, odkBlankForm.jrVersion);
        assertEquals(form.formMediaPath, odkBlankForm.formMediaPath);
    }
}