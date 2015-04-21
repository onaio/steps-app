package com.onaio.steps.model.ODKForm;

import android.app.Activity;
import android.content.ContentProviderClient;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import com.onaio.steps.exception.AppNotInstalledException;
import com.onaio.steps.exception.FormNotPresentException;
import com.onaio.steps.model.Household;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ODKSavedForm extends ODKForm{
    public static final String COLLECT_FORMS_AUTHORITY = "org.odk.collect.android.provider.odk.instances";
    private static final String URI_STRING = "content://"
            + COLLECT_FORMS_AUTHORITY + "/instances";
    public static final Uri URI = Uri.parse(URI_STRING);

    String instanceFilePath;
    private String status;

    public Uri getUri() {
        return URI.parse(URI_STRING+"/"+_id);
    }

    public ODKSavedForm(String id, String jrFormId, String displayName, String jrVersion, String instanceFilePath, String status) {
        super(jrVersion,displayName,jrFormId,id);
        this.instanceFilePath = instanceFilePath;
        this.status = status;
    }

    @Override
    public void open(Household household, Activity activity) throws IOException {
        launchODKCollect(activity);
    }

    public static List<ODKSavedForm> getWithName(Activity activity, String displayName) throws AppNotInstalledException {
        List<ODKSavedForm> forms = get(activity, displayName);
        return forms;
    }

    public static List<ODKSavedForm> get(Activity activity, String displayName) throws AppNotInstalledException {
        ContentProviderClient formsContentProvider = activity.getContentResolver().acquireContentProviderClient(ODKSavedForm.URI);
        ArrayList<ODKSavedForm> forms = new ArrayList<ODKSavedForm>();
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
