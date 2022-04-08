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

import android.content.ContentProviderClient;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.exceptions.AppNotInstalledException;

import java.util.ArrayList;
import java.util.List;

public class ODKSavedForm implements IForm{
    public static final String COLLECT_FORMS_AUTHORITY = "org.odk.collect.android.provider.odk.instances";
    private static final String URI_STRING = "content://"
            + COLLECT_FORMS_AUTHORITY + "/instances";
    public static final Uri URI = Uri.parse(URI_STRING);

    String instanceFilePath;
    String status;
    String _id;
    String jrFormId;
    String displayName;
    String jrVersion;

    protected ODKSavedForm(String id, String jrFormId, String displayName, String jrVersion, String instanceFilePath, String status) {
        _id = id;
        this.jrFormId = jrFormId;
        this.displayName = displayName;
        this.jrVersion = jrVersion;
        this.instanceFilePath = instanceFilePath;
        this.status = status;
    }

    public Uri getUri() {
        return Uri.parse(URI_STRING + "/" + _id);
    }

    public static <T extends IForm> List<T> findAll(AppCompatActivity activity, String odkFormId) throws AppNotInstalledException {
        ContentProviderClient formsContentProvider = activity.getContentResolver().acquireContentProviderClient(ODKSavedForm.URI);
        List<ODKSavedForm> forms = new ArrayList<>();
        if (odkFormId == null) return (List<T>) forms;
        try {
            if(formsContentProvider==null) throw new AppNotInstalledException();
            Cursor cursor = formsContentProvider.query(ODKSavedForm.URI, null, "_id = ?", new String[]{odkFormId}, null);
            if(cursor != null && cursor.moveToFirst()){
                do{
                    String id = cursor.getString(cursor.getColumnIndex("_id"));
                    String jrFormId = cursor.getString(cursor.getColumnIndex("jrFormId"));
                    String readDisplayName = cursor.getString(cursor.getColumnIndex("displayName"));
                    String jrVersion = cursor.getString(cursor.getColumnIndex("jrVersion"));
                    String instanceFilePath = cursor.getString(cursor.getColumnIndex("instanceFilePath"));
                    String status = cursor.getString(cursor.getColumnIndex("status"));
                    forms.add(new ODKSavedForm(id, jrFormId, readDisplayName, jrVersion, instanceFilePath,status));
                }while (cursor.moveToNext());
            }
        } catch (RemoteException e) {
            throw new AppNotInstalledException();
        }
        return (List<T>) forms;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return _id;
    }

    public String getJrFormId() {
        return jrFormId;
    }

    public String getDisplayName() {
        return displayName;
    }
}
