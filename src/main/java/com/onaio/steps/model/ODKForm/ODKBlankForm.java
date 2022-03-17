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

import android.annotation.SuppressLint;
import android.content.ContentProviderClient;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.exceptions.AppNotInstalledException;
import com.onaio.steps.exceptions.FormNotPresentException;

import java.util.ArrayList;
import java.util.List;

public class ODKBlankForm implements IForm{
    public static final String COLLECT_FORMS_AUTHORITY = "org.odk.collect.android.provider.odk.forms";
    private static final String URI_STRING = "content://"
            + COLLECT_FORMS_AUTHORITY + "/forms";
    public static final Uri URI = Uri.parse(URI_STRING);

    String formMediaPath;
    String _id;
    String jrFormId;
    String displayName;
    String jrVersion;

    public ODKBlankForm(String id, String jrFormId, String displayName, String jrVersion, String formMediaPath){
        _id = id;
        this.jrFormId = jrFormId;
        this.displayName = displayName;
        this.jrVersion = jrVersion;
        this.formMediaPath = formMediaPath;
    }

    public static IForm find(AppCompatActivity activity, String jrFormId) throws FormNotPresentException, AppNotInstalledException {
        List<ODKBlankForm> forms = get(activity, jrFormId);
        if(forms.size() <= 0)
            throw new FormNotPresentException();
        return forms.get(0);
    }

    public Uri getUri() {
        return Uri.parse(URI_STRING + "/" + _id);
    }

    @SuppressLint("Range")
    public static List<ODKBlankForm> get(AppCompatActivity activity, @Nullable String odkFormId) throws AppNotInstalledException {
        ContentProviderClient formsContentProvider = activity.getContentResolver().acquireContentProviderClient(ODKBlankForm.URI);
        List<ODKBlankForm> forms = new ArrayList<>();
        try {
            if(formsContentProvider==null) throw new AppNotInstalledException();

            String selection = odkFormId != null ? "jrFormId = ?" : null;
            String[] selectionArgs = odkFormId != null ? new String[]{odkFormId} : null;

            Cursor cursor = formsContentProvider.query(ODKBlankForm.URI, null, selection, selectionArgs, null);
            if(cursor != null && cursor.moveToFirst()){
                do{
                    String id = cursor.getString(cursor.getColumnIndex("_id"));
                    String jrFormId = cursor.getString(cursor.getColumnIndex("jrFormId"));
                    String displayName = cursor.getString(cursor.getColumnIndex("displayName"));
                    String jrVersion = cursor.getString(cursor.getColumnIndex("jrVersion"));
                    String formMediaPath = cursor.getString(cursor.getColumnIndex("formMediaPath"));
                    forms.add(new ODKBlankForm(id, jrFormId, displayName, jrVersion, formMediaPath));
                }while (cursor.moveToNext());
            }
        } catch (RemoteException e) {
            throw new AppNotInstalledException();
        }
        return forms;
    }

    public String getJrFormId() {
        return jrFormId;
    }

    @NonNull
    @Override
    public String toString() {
        return displayName;
    }
}
