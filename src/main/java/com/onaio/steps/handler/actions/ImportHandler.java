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

package com.onaio.steps.handler.actions;

import android.app.ListActivity;
import android.os.Environment;

import com.onaio.steps.R;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.CustomNotification;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.DownloadFileTask;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.helper.Logger;
import com.onaio.steps.helper.NetworkConnectivity;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.ReElectReason;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.onaio.steps.helper.Constants.HH_PHONE_ID;
import static com.onaio.steps.helper.Constants.HH_SURVEY_ID;
import static com.onaio.steps.helper.Constants.IMPORT_URL;

public class ImportHandler implements IMenuHandler {
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
        String deviceId = KeyValueStoreFactory.instance(activity).getString(HH_PHONE_ID);
        String surveyId = KeyValueStoreFactory.instance(activity).getString(HH_SURVEY_ID);
        String filename = Environment.getExternalStorageDirectory()+"/"+Constants.APP_DIR+"/"+Constants.EXPORT_FILE_NAME+"_"+
                deviceId +".csv";
        DownloadFileTask handler = new DownloadFileTask(this, filename);
        handler.execute(KeyValueStoreFactory.instance(activity).getString(IMPORT_URL)+"/"+deviceId+"/"+surveyId);
        return true;
    }

    public void importDataFromDownloadedFile(String filepath) {
        try {
            List<String[]> rows = fileUtil.readFile(filepath);
            for(String[] row : rows){
                //Validate for 10 data
                String phoneNumber = row[0];
                String householdName = row[1];
                String comments = row[2];
                String memberHouseholdId = row[3];
                String surname = row[4];
                String firstName = row[5];
                String age = row[6];
                String gender = row[7];
                String deleted = row[8];
                String surveyStatus = row[9];
                String reasons = row[11];
                Household household = Household.find_by(db, householdName);
                if(household == null){
                    String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH).format(new Date());
                    household = new Household(householdName, phoneNumber, InterviewStatus.NOT_SELECTED, currentDate ,comments);
                    household.save(db);
                }
                //validate for members
                Member member = Member.find_by_household_id(db, household, memberHouseholdId);
                if (member == null && !memberHouseholdId.trim().endsWith(Constants.DUMMY_MEMBER_ID)) {
                    member = new Member(surname, firstName, Gender.valueOf(gender), Integer.parseInt(age), household, Boolean.getBoolean(deleted));
                    member.setMemberHouseholdId(memberHouseholdId);
                    member.save(db);
                    if (!(surveyStatus.equals(Constants.SURVEY_NA) || surveyStatus.equals(InterviewStatus.NOT_SELECTED.toString()))) {
                        household.setStatus(InterviewStatus.valueOf(surveyStatus));
                        household.setSelectedMemberId(String.valueOf(member.getId()));
                        household.update(db);
                    }
                    int reasonCount = ReElectReason.count(db, household);
                    //validate for reasons
                    if (reasonCount != 0) {
                        String[] separateReasons = reasons.split(";");
                        for (String reason : separateReasons) {
                            ReElectReason reElectReason = new ReElectReason(reason, household);
                            reElectReason.save(db);
                        }
                    }
                }
            }
            new CustomNotification().notify(activity, R.string.import_complete, R.string.import_complete_message);
            // Show imported households
            activity.recreate();
        } catch (IOException e) {
            if (NetworkConnectivity.isNetworkAvailable(activity)) {
                new Logger().log(e, "Import failed.");
                new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.error_title, R.string.import_fail_message);
            } else {
                new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.error_title, R.string.fail_no_connectivity);
            }
        }
    }
}
