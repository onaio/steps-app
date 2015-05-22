package com.onaio.steps.model.ODKForm;

import android.app.Activity;
import android.content.ContentProviderClient;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

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
        return URI.parse(URI_STRING+"/"+_id);
    }

    @Override
    public String getPath() {
        return instanceFilePath;
    }

    public static List<IForm> findAll(Activity activity, String displayName) throws AppNotInstalledException {
        ContentProviderClient formsContentProvider = activity.getContentResolver().acquireContentProviderClient(ODKSavedForm.URI);
        ArrayList<IForm> forms = new ArrayList<IForm>();
        try {
            if(formsContentProvider==null) throw new AppNotInstalledException();
            Cursor cursor = formsContentProvider.query(ODKSavedForm.URI, null, "displayName = ?", new String[]{displayName}, null);
            if(cursor.moveToFirst()){
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
        return forms;
    }

    public String getStatus() {
        return status;
    }
}
