package com.onaio.steps.activityHandler;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IResultHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.ReElectReason;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ImportHandler implements IMenuHandler, IResultHandler {
    private final DatabaseHelper db;
    private ListActivity activity;
    private static final int MENU_ID= R.id.action_import;
    private final FileUtil fileUtil;

    public ImportHandler(ListActivity activity) {
        this(activity, new DatabaseHelper(activity),new FileUtil());
    }

    ImportHandler(ListActivity activity, DatabaseHelper db, FileUtil fileUtil) {
        this.activity = activity;
        this.db = db;
        this.fileUtil = fileUtil;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        activity.startActivityForResult(intent, Constants.IMPORT_IDENTIFIER);
        return true;
    }

    @Override
    public void handleResult(Intent data, int resultCode) {
        if(resultCode!= Activity.RESULT_OK)
            return;
        try {
            List<String[]> rows = fileUtil.readFile(data.getData().getPath());
            for(String[] row:rows){
                //Validate for 10 data
                String phoneNumber = row[0];
                String householdName = row[1];
                String memberHouseholdId = row[2];
                String surname = row[3];
                String firstName = row[4];
                String age = row[5];
                String gender = row[6];
                String deleted = row[7];
                String surveyStatus = row[8];
                String reasonsCount = row[9];
                String reasons = row[10];
                Household household = Household.find_by(db, householdName);
                if(household == null){
                    String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
                    household = new Household(householdName, phoneNumber, HouseholdStatus.NOT_SELECTED, currentDate);
                    household.save(db);
                }
                //validate for members
                Member member = new Member(surname, firstName, gender, Integer.parseInt(age), household, Boolean.getBoolean(deleted));
                member.setMemberHouseholdId(memberHouseholdId);
                member.save(db);
                if(!(surveyStatus.equals(Constants.SURVEY_NA) || surveyStatus.equals(HouseholdStatus.NOT_SELECTED.toString()))) {
                    household.setStatus(HouseholdStatus.valueOf(surveyStatus));
                    household.setSelectedMemberId(String.valueOf(member.getId()));
                    household.update(db);
                }
                int reasonCount = ReElectReason.count(db, household);
                //validate for reasons
                if(reasonCount != 0){
                    String[] separateReasons = reasons.split(";");
                    for(String reason:separateReasons){
                        ReElectReason reElectReason = new ReElectReason(reason, household);
                        reElectReason.save(db);
                    }
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == Constants.IMPORT_IDENTIFIER;
    }
}