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
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;

import com.onaio.steps.R;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.helper.Logger;
import com.onaio.steps.helper.NetworkConnectivity;
import com.onaio.steps.helper.UploadFileTask;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.ReElectReason;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.onaio.steps.helper.Constants.EXPORT_FIELDS;
import static com.onaio.steps.helper.Constants.HH_PHONE_ID;
import static com.onaio.steps.helper.Constants.HH_SURVEY_ID;
import static com.onaio.steps.helper.Constants.SURVEY_NA;

public class ExportHandler implements IMenuHandler,IMenuPreparer {

    private List<Household> households;
    private ListActivity activity;
    private DatabaseHelper databaseHelper;
    private Menu menu;
    private int MENU_ID = R.id.action_export;
    private static final String EMPTY_COLUMN = "";
    private OnExportListener onExportListener;

    public ExportHandler(ListActivity activity) {
        this.activity = activity;
        databaseHelper = new DatabaseHelper(activity.getApplicationContext());
        households = new ArrayList<Household>();
    }

    public void setOnExportListener(OnExportListener onExportListener) {
        this.onExportListener = onExportListener;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        DialogInterface.OnClickListener uploadConfirmListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(onExportListener != null)  onExportListener.onExportStart();
                try {
                    File file = saveFile();
                    if(onExportListener != null) onExportListener.onFileSaved();
                    if (NetworkConnectivity.isNetworkAvailable(activity)) {
                        new UploadFileTask(activity, onExportListener).execute(file);

                    } else {
                        new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.error_title, R.string.fail_no_connectivity);
                    }
                } catch (IOException e) {
                    new Logger().log(e,"Not able to write CSV file for export.");
                    new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.error_title, R.string.something_went_wrong_try_again);
                }
            }
        };
        DialogInterface.OnClickListener uploadCancelledListener = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(onExportListener != null) onExportListener.onExportCancelled();
            }
        };
        new CustomDialog().confirm(activity, uploadConfirmListener, uploadCancelledListener, R.string.export_start_message, R.string.action_export);
        return true;
    }

    public File saveFile() throws IOException {
        String deviceId = getDeviceId();

        FileUtil fileUtil = new FileUtil().withHeader(EXPORT_FIELDS);
        List<Household> emptyHouseholds = new ArrayList<>();
        for(Household household: households) {
            List<ReElectReason> reasons = ReElectReason.getAll(databaseHelper, household);
            List<Member> membersPerHousehold = household.getAllMembersForExport(databaseHelper);
            for(Member member: membersPerHousehold) {
                ArrayList<String> row = new ArrayList<>();
                row.add(household.getPhoneNumber());
                row.add(household.getName());
                row.add(replaceCommas(household.getComments()));
                row.add(member.getMemberHouseholdId());
                row.add(member.getFamilySurname());
                row.add(member.getFirstName());
                row.add(String.valueOf(member.getAge()));
                row.add(member.getGender().toString());
                row.add(member.getDeletedString());
                setStatus(household, member, row);
                row.add(String.valueOf(reasons.size()));
                row.add(replaceCommas(StringUtils.join(reasons.toArray(), ';')));
                row.add(deviceId);
                row.add(KeyValueStoreFactory.instance(activity).getString(HH_SURVEY_ID));
                row.add(String.valueOf(household.numberOfNonDeletedMembers(databaseHelper)));
                fileUtil.withData(row.toArray(new String[row.size()]));
            }
            if (membersPerHousehold.size() == 0) {
                emptyHouseholds.add(household);
            }
        }
        // Add households with no members
        for (Household household : emptyHouseholds) {
            List<ReElectReason> reasons = ReElectReason.getAll(databaseHelper, household);
            ArrayList<String> row = new ArrayList<>();
            row.add(household.getPhoneNumber());
            row.add(household.getName());
            row.add(replaceCommas(household.getComments()));
            row.add(household.getName() + Constants.DUMMY_MEMBER_ID);
            row.add(EMPTY_COLUMN);
            row.add(EMPTY_COLUMN);
            row.add(EMPTY_COLUMN);
            row.add(EMPTY_COLUMN);
            row.add(EMPTY_COLUMN);
            row.add(SURVEY_NA);
            row.add(String.valueOf(reasons.size()));
            row.add(replaceCommas(StringUtils.join(reasons.toArray(), ';')));
            row.add(deviceId);
            row.add(KeyValueStoreFactory.instance(activity).getString(HH_SURVEY_ID));
            row.add(String.valueOf(household.numberOfNonDeletedMembers(databaseHelper)));
            fileUtil.withData(row.toArray(new String[row.size()]));
        }
        //Write the csv to external storage for the user to access.
        new SaveToSDCardHandler(activity).saveToExternalStorage(fileUtil);

        return fileUtil.writeCSV(activity.getFilesDir() + "/" + Constants.EXPORT_FILE_NAME + "_" + deviceId + ".csv");
    }

    /**
     * Remove commas within a column to prevent split.
     * @param column
     * @return
     */
    private String replaceCommas(String column) {
        return column.replace("\"", "").replace(",", ";");
    }


    private void setStatus(Household household, Member member, ArrayList<String> row) {
        if(household.getSelectedMemberId() == null || household.getSelectedMemberId().equals("") || household.getSelectedMemberId().equals(String.valueOf(member.getId())))
            row.add(household.getStatus().toString());
        else {
            row.add(SURVEY_NA);
        }
    }

    public ExportHandler with(List<Household> households){
        this.households = households;
        return this;
    }

    @Override
    public boolean shouldInactivate() {
           return households.isEmpty();
    }

    @Override
    public void inactivate() {
        MenuItem item = menu.findItem(MENU_ID);
        item.setEnabled(false);
    }

    @Override
    public void activate() {
        MenuItem item = menu.findItem(MENU_ID);
        item.setEnabled(true);
    }

    public IMenuPreparer withMenu(Menu menu) {
        this.menu = menu;
        return this;
    }

    public String getDeviceId() {
        return KeyValueStoreFactory.instance(activity).getString(HH_PHONE_ID);
    }

    public interface OnExportListener {
        void onExportCancelled();
        void onExportStart();
        void onFileSaved();
        void onFileUploaded();
    }
}
