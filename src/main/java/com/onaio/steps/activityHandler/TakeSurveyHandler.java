package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IPrepare;
import com.onaio.steps.exception.AppNotInstalledException;
import com.onaio.steps.exception.FormNotPresentException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.Dialog;
import com.onaio.steps.helper.FileBuilder;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.ODKForm;

import java.io.IOException;
import java.util.ArrayList;

public class TakeSurveyHandler implements IMenuHandler, IPrepare {
    private ListActivity activity;
    private Household household;
    private static final int MENU_ID= R.id.action_take_survey;


    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    public TakeSurveyHandler(ListActivity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean open() {
        try {
            ODKForm requiredForm = ODKForm.getWithId(activity, Constants.ODK_FORM_ID);
            saveFile(requiredForm);
            launchODKCollect(requiredForm);
            updateHousehold();
        } catch (FormNotPresentException e) {
            new Dialog().notify(activity,Dialog.EmptyListener,R.string.form_not_present, R.string.error_title);
        } catch (AppNotInstalledException e) {
            new Dialog().notify(activity,Dialog.EmptyListener,R.string.odk_app_not_installed, R.string.error_title);
        } catch (IOException e) {
            new Dialog().notify(activity,Dialog.EmptyListener,R.string.something_went_wrong_try_again, R.string.error_title);
        }

        return true;
    }

    private void updateHousehold() {
        household.setStatus(HouseholdStatus.CLOSED);
        household.update(new DatabaseHelper(activity));
    }

    private void launchODKCollect(ODKForm requiredForm) {
        Intent surveyIntent = new Intent();
        surveyIntent.setComponent(new ComponentName("org.odk.collect.android","org.odk.collect.android.activities.FormEntryActivity"));
        surveyIntent.setAction(Intent.ACTION_EDIT);
        surveyIntent.setData(requiredForm.getUri());
        activity.startActivity(surveyIntent);
    }

    private void saveFile(ODKForm requiredForm) throws IOException {
        FileBuilder fileBuilder = new FileBuilder().withHeader(Constants.ODK_FORM_FIELDS.split(","));
        Member selectedMember = Member.find_by(new DatabaseHelper(activity), Long.parseLong(household.getSelectedMember()), household);
        ArrayList<String> row = new ArrayList<String>();
        row.add(Constants.ODK_HH_ID);
        row.add(selectedMember.getFamilySurname());
        row.add(selectedMember.getFirstName());
        String gender = selectedMember.getGender();
        int genderInt = gender.equals(Constants.MALE)?1:2;
        row.add(String.valueOf(genderInt));
        row.add(String.valueOf(selectedMember.getAge()));
        fileBuilder.withData(row.toArray(new String[row.size()]));
        fileBuilder.buildCSV(requiredForm.getFormMediaPath()+"/"+Constants.ODK_DATA_FILENAME);
    }

    @Override
    public boolean shouldInactivate() {
        boolean selected = household.getStatus() == HouseholdStatus.SELECTED;
        boolean deferred = household.getStatus() == HouseholdStatus.DEFERRED;
        return !(selected || deferred );
    }

    @Override
    public void inactivate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.INVISIBLE);
    }

    @Override
    public void activate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.VISIBLE);
    }
}
