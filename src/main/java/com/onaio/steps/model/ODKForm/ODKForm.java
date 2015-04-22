package com.onaio.steps.model.ODKForm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

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

    public void open(Household household, Activity activity) throws IOException{
        //irrespective of the form type the data has to be saved at entry form path.
        String pathToSaveDataFile = blankForm.getPath();
        saveDataFile(household, new DatabaseHelper(activity), pathToSaveDataFile);
        launchODKCollect(activity);
    }

    public static ODKForm create(Activity activity, String formId, String formName) throws FormNotPresentException, AppNotInstalledException {
        List<IForm> savedForms = ODKSavedForm.findAll(activity, formName);
        IForm blankForm = ODKBlankForm.find(activity, formId);
        if(savedForms != null && !savedForms.isEmpty())
            return new ODKForm(blankForm,savedForms.get(0));
        return new ODKForm(blankForm,null);
    }

    private void launchODKCollect(Activity activity) {
        Intent surveyIntent = new Intent();
        surveyIntent.setComponent(new ComponentName(Constants.ODK_COLLECT_PACKAGE,Constants.ODK_COLLECT_FORM_CLASS));
        surveyIntent.setAction(Intent.ACTION_EDIT);
        surveyIntent.setData(getForm().getUri());
        activity.startActivityForResult(surveyIntent, Constants.SURVEY_IDENTIFIER);
    }

    private void saveDataFile(Household household, DatabaseHelper db, String pathToSaveDataFile) throws IOException {
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
                .writeCSV(pathToSaveDataFile + "/" + Constants.ODK_DATA_FILENAME);
    }

}
