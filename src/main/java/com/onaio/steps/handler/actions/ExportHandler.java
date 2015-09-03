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
import static com.onaio.steps.helper.Constants.PHONE_ID;
import static com.onaio.steps.helper.Constants.SURVEY_ID;
import static com.onaio.steps.helper.Constants.SURVEY_NA;

public class ExportHandler implements IMenuHandler,IMenuPreparer {

    private List<Household> households;
    private ListActivity activity;
    private DatabaseHelper databaseHelper;
    private Menu menu;
    private int MENU_ID = R.id.action_export;

    public static final String APP_DIR = "STEPS";

    public ExportHandler(ListActivity activity) {
        this.activity = activity;
        databaseHelper = new DatabaseHelper(activity.getApplicationContext());
        households = new ArrayList<Household>();
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
                try {
                    File file = saveFile();
                    if (NetworkConnectivity.isNetworkAvailable(activity)) {
                        new UploadFileTask(activity).execute(file);
                    } else {
                        new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.error_title, R.string.fail_no_connectivity);
                    }
                } catch (IOException e) {
                    new Logger().log(e,"Not able to write CSV file for export.");
                    new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.error_title, R.string.something_went_wrong_try_again);
                }
            }
        };
        new CustomDialog().confirm(activity, uploadConfirmListener, CustomDialog.EmptyListener, R.string.export_start_message, R.string.action_export);
        return true;
    }

    public File saveFile() throws IOException {
        String deviceId = getDeviceId();

        FileUtil fileUtil = new FileUtil().withHeader(EXPORT_FIELDS);
        for(Household household: households) {
            List<ReElectReason> reasons = ReElectReason.getAll(databaseHelper, household);
            List<Member> membersPerHousehold = household.getAllMembersForExport(databaseHelper);
            for(Member member: membersPerHousehold){
                ArrayList<String> row = new ArrayList<String>();
                row.add(household.getPhoneNumber());
                row.add(household.getName());
                row.add(household.getComments());
                row.add(member.getMemberHouseholdId());
                row.add(member.getFamilySurname());
                row.add(member.getFirstName());
                row.add(String.valueOf(member.getAge()));
                row.add(member.getGender().toString());
                row.add(member.getDeletedString());
                setStatus(household, member, row);
                row.add(String.valueOf(reasons.size()));
                row.add(StringUtils.join(reasons.toArray(), ';'));
                row.add(deviceId);
                row.add(KeyValueStoreFactory.instance(activity).getString(SURVEY_ID));
                fileUtil.withData(row.toArray(new String[row.size()]));
            }
        }
        //Write the csv to external storage for the user to access.
        new SaveToSDCardHandler(activity).saveToExternalStorage(fileUtil);

        return fileUtil.writeCSV(activity.getFilesDir() + "/" + Constants.EXPORT_FILE_NAME + "_" + deviceId + ".csv");
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
        return KeyValueStoreFactory.instance(activity).getString(PHONE_ID);
    }
}
