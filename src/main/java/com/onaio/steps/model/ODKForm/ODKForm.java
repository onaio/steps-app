package com.onaio.steps.model.ODKForm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import com.onaio.steps.exception.AppNotInstalledException;
import com.onaio.steps.exception.FormNotPresentException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.Participant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ODKForm {
    private IForm savedForm;
    private IForm blankForm;
    private FileUtil fileUtil;

    public ODKForm(IForm blankForm, IForm savedForm){
        this(blankForm,savedForm,new FileUtil());
    }

    public ODKForm(IForm blankForm,IForm savedForm, FileUtil fileUtil){
        this.blankForm = blankForm;
        this.savedForm = savedForm;
        this.fileUtil = fileUtil;
    }

    private IForm getForm(){
        if(savedForm!= null)
            return savedForm;
        return blankForm;
    }

    public void open(Household household, Activity activity, int requestCode) throws IOException{
        //irrespective of the form type the data has to be saved at entry form path.
        String pathToSaveDataFile = blankForm.getPath();
        saveDataFile(household,activity, new DatabaseHelper(activity), pathToSaveDataFile);
        launchODKCollect(activity, requestCode);
    }

    public static ODKForm create(Activity activity, String formId, String formName) throws FormNotPresentException, AppNotInstalledException {
        List<IForm> savedForms = ODKSavedForm.findAll(activity, formName);
        IForm blankForm = ODKBlankForm.find(activity, formId);
        if(savedForms != null && !savedForms.isEmpty())
            return new ODKForm(blankForm,savedForms.get(0));
        return new ODKForm(blankForm,null);
    }

    private void launchODKCollect(Activity activity, int requestCode) {
        Intent surveyIntent = new Intent();
        surveyIntent.setComponent(new ComponentName(Constants.ODK_COLLECT_PACKAGE,Constants.ODK_COLLECT_FORM_CLASS));
        surveyIntent.setAction(Intent.ACTION_EDIT);
        surveyIntent.setData(getForm().getUri());
        activity.startActivityForResult(surveyIntent, requestCode);
    }

    private void saveDataFile(Household household, Activity activity, DatabaseHelper db, String pathToSaveDataFile) throws IOException {
        Member selectedMember = household.getSelectedMember(db);
        String formId = getValue(Constants.FORM_ID,activity);
        String formNameFormat = formId + "-%s";
        ArrayList<String> row = new ArrayList<String>();
        row.add(Constants.ODK_HH_ID);
        row.add(String.format(formNameFormat, household.getName()));
        row.add(selectedMember.getMemberHouseholdId());
        row.add(selectedMember.getFamilySurname());
        row.add(selectedMember.getFirstName());
        row.add(String.valueOf(selectedMember.getGender().getIntValue()));
        row.add(String.valueOf(selectedMember.getAge()));
        fileUtil.withHeader(Constants.ODK_FORM_FIELDS.split(","))
                .withData(row.toArray(new String[row.size()]))
                .writeCSV(pathToSaveDataFile + "/" + Constants.ODK_DATA_FILENAME);
    }
    private String getValue(String key,Activity activity) {
        return KeyValueStoreFactory.instance(activity).getString(key) ;
    }

    public void open(Participant participant, Activity activity, int code) throws IOException{
        String pathToSaveDataFile = blankForm.getPath();
        saveDataFile(participant,activity, new DatabaseHelper(activity), pathToSaveDataFile);
        launchODKCollect(activity, code);
    }

    private void saveDataFile(Participant participant, Activity activity, DatabaseHelper db, String pathToSaveDataFile) throws IOException{
        String formId = getValue(Constants.FORM_ID,activity);
        String formNameFormat = formId + "-%s";
        ArrayList<String> row = new ArrayList<String>();
        row.add(Constants.ODK_PARTICIPANT_ID);
        row.add(String.format(formNameFormat, participant.getId()));
        row.add(participant.getFamilySurname());
        row.add(participant.getFirstName());
        row.add(String.valueOf(participant.getGender().getIntValue()));
        row.add(String.valueOf(participant.getAge()));
        fileUtil.withHeader(Constants.PARTICIPANT_ODK_FORM_FIELDS.split(","))
                .withData(row.toArray(new String[row.size()]))
                .writeCSV(pathToSaveDataFile + "/" + Constants.ODK_DATA_FILENAME);

    }

}
