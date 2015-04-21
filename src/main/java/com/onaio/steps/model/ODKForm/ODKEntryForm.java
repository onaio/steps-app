package com.onaio.steps.model.ODKForm;

import android.app.Activity;
import android.content.ContentProviderClient;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import com.onaio.steps.exception.AppNotInstalledException;
import com.onaio.steps.exception.FormNotPresentException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ODKEntryForm extends ODKForm {
    public static final String COLLECT_FORMS_AUTHORITY = "org.odk.collect.android.provider.odk.forms";
    private static final String URI_STRING = "content://"
            + COLLECT_FORMS_AUTHORITY + "/forms";
    public static final Uri URI = Uri.parse(URI_STRING);
    private FileUtil fileUtil;

    public ODKEntryForm(String id, String jrFormId, String displayName, String jrVersion, String formMediaPath) {
        this(id,jrFormId,displayName,jrVersion,formMediaPath,new FileUtil());
    }

    public ODKEntryForm(String id, String jrFormId, String displayName, String jrVersion, String formMediaPath,FileUtil fileUtil){
        super(jrVersion, displayName, jrFormId, id);

        this.formMediaPath = formMediaPath;
        this.fileUtil = fileUtil;
    }

    String formMediaPath;

    @Override
    public Uri getUri() {
        return URI.parse(URI_STRING+"/"+_id);
    }

    @Override
    public void open(Household household, Activity activity) throws IOException {
        saveFile(household, new DatabaseHelper(activity));
        launchODKCollect(activity);
    }

    private void saveFile(Household household, DatabaseHelper db) throws IOException {
        Member selectedMember = household.getSelectedMember(db);
        ArrayList<String> row = new ArrayList<String>();
        row.add(Constants.ODK_HH_ID);
        row.add(String.format(Constants.ODK_FORM_NAME_FORMAT, household.getName()));
        row.add(selectedMember.getMemberHouseholdId());
        row.add(selectedMember.getFamilySurname());
        row.add(selectedMember.getFirstName());
        String gender = selectedMember.getGender();
        int genderInt = gender.equals(Constants.MALE)?1:2;
        row.add(String.valueOf(genderInt));
        row.add(String.valueOf(selectedMember.getAge()));
        fileUtil.withHeader(Constants.ODK_FORM_FIELDS.split(","))
                .withData(row.toArray(new String[row.size()]))
                .writeCSV(formMediaPath + "/" + Constants.ODK_DATA_FILENAME);
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
