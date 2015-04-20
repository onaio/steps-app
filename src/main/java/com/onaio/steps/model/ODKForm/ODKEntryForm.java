package com.onaio.steps.model.ODKForm;

import android.app.Activity;
import android.content.ContentProviderClient;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import com.onaio.steps.exception.AppNotInstalledException;
import com.onaio.steps.exception.FormNotPresentException;

import java.util.ArrayList;
import java.util.List;

public class ODKEntryForm extends ODKForm {
    public static final String COLLECT_FORMS_AUTHORITY = "org.odk.collect.android.provider.odk.forms";
    private static final String URI_STRING = "content://"
            + COLLECT_FORMS_AUTHORITY + "/forms";
    public static final Uri URI = Uri.parse(URI_STRING);

    String formMediaPath;

    @Override
    public Uri getUri() {
        return URI.parse(URI_STRING+"/"+_id);
    }

    @Override
    public String getPath() {
        return formMediaPath;
    }

    public ODKEntryForm(String id, String jrFormId, String displayName, String jrVersion, String formMediaPath) {
        super(jrVersion, displayName, jrFormId, id);

        this.formMediaPath = formMediaPath;
    }

    public static ODKForm getWithId(Activity activity, String jrFormId) throws FormNotPresentException, AppNotInstalledException {
        List<ODKForm> forms = get(activity, jrFormId);
        if(forms.size() <= 0)
            throw new FormNotPresentException();
        return forms.get(0);
    }

    public static List<ODKForm> get(Activity activity, String odkFormId) throws AppNotInstalledException {
        ContentProviderClient formsContentProvider = activity.getContentResolver().acquireContentProviderClient(ODKEntryForm.URI);
        ArrayList<ODKForm> forms = new ArrayList<ODKForm>();
        try {
            if(formsContentProvider==null) throw new AppNotInstalledException();
            Cursor cursor = formsContentProvider.query(ODKEntryForm.URI, null, "jrFormId = ?", new String[]{odkFormId}, null);
            if(cursor.moveToFirst()){
                do{
                    String id = cursor.getString(cursor.getColumnIndex("_id"));
                    String jrFormId = cursor.getString(cursor.getColumnIndex("jrFormId"));
                    String displayName = cursor.getString(cursor.getColumnIndex("displayName"));
                    String jrVersion = cursor.getString(cursor.getColumnIndex("jrVersion"));
                    String formMediaPath = cursor.getString(cursor.getColumnIndex("formMediaPath"));
                    forms.add(new ODKEntryForm(id, jrFormId, displayName, jrVersion, formMediaPath));
                }while (cursor.moveToNext());
            }
        } catch (RemoteException e) {
            throw new AppNotInstalledException();
        }
        return forms;
    }
}
