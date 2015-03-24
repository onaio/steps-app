package com.onaio.steps.model;

import android.app.Activity;
import android.content.ContentProviderClient;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import com.onaio.steps.R;
import com.onaio.steps.exception.AppNotInstalledException;
import com.onaio.steps.exception.FormNotPresentException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.Dialog;

import java.util.ArrayList;
import java.util.List;

public class ODKForm {
    public static final String COLLECT_FORMS_AUTHORITY = "org.odk.collect.android.provider.odk.forms";
    private static final String URI_STRING = "content://"
            + COLLECT_FORMS_AUTHORITY + "/forms";
    public static final Uri URI = Uri.parse(URI_STRING);

    String _id;
    String jrFormId;
    String displayName;
    String jrVersion;
    String formMediaPath;

    public Uri getUri() {
        return URI.parse(URI_STRING+"/"+_id);
    }


    public ODKForm(String id, String jrFormId, String displayName, String jrVersion, String formMediaPath) {

        _id = id;
        this.jrFormId = jrFormId;
        this.displayName = displayName;
        this.jrVersion = jrVersion;
        this.formMediaPath = formMediaPath;
    }

    public static ODKForm getFrom(Activity activity, String jrFormId) throws FormNotPresentException, AppNotInstalledException {
        List<ODKForm> forms = getAll(activity);
        for (ODKForm form: forms)
            if(form.jrFormId.equals(jrFormId))
                return form;
        throw new FormNotPresentException();
    }

    public static List<ODKForm> getAll(Activity activity) throws AppNotInstalledException {
        ContentProviderClient formsContentProvider = activity.getContentResolver().acquireContentProviderClient(ODKForm.URI);
        ArrayList<ODKForm> forms = new ArrayList<ODKForm>();
        try {
            Cursor cursor = formsContentProvider.query(ODKForm.URI, null, null, null, null);
            if(cursor.moveToFirst()){
                do{
                    String id = cursor.getString(cursor.getColumnIndex("_id"));
                    String jrFormId = cursor.getString(cursor.getColumnIndex("jrFormId"));
                    String displayName = cursor.getString(cursor.getColumnIndex("displayName"));
                    String jrVersion = cursor.getString(cursor.getColumnIndex("jrVersion"));
                    String formMediaPath = cursor.getString(cursor.getColumnIndex("formMediaPath"));
                    forms.add(new ODKForm(id, jrFormId, displayName, jrVersion, formMediaPath));
                }while (cursor.moveToNext());
            }
        } catch (RemoteException e) {
            throw new AppNotInstalledException();
        }
        return forms;
    }
}
