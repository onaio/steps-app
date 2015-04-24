package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuPreparer;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.FileUtil;
import com.onaio.steps.helper.Logger;
import com.onaio.steps.helper.UploadFileTask;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.ReElectReason;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.onaio.steps.helper.Constants.*;

public class ExportHandler implements IMenuHandler,IMenuPreparer {

    private List<Household> households;
    private ListActivity activity;
    private DatabaseHelper databaseHelper;
    private Menu menu;
    private int MENU_ID = R.id.action_export;

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
                    new UploadFileTask(activity).execute(file);
                } catch (IOException e) {
                    new Logger().log(e,"Not able to write CSV file for export.");
                    new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.error_title, R.string.something_went_wrong_try_again);
                }
            }
        };
        new CustomDialog().confirm(activity, uploadConfirmListener, CustomDialog.EmptyListener, R.string.export_start_message,R.string.action_export);
        return true;
    }

    private File saveFile() throws IOException {
        FileUtil fileUtil = new FileUtil().withHeader(EXPORT_FIELDS.split(","));
        for(Household household: households) {
            List<ReElectReason> reasons = ReElectReason.getAll(databaseHelper, household);
            List<Member> membersPerHousehold = household.getAllMembersForExport(databaseHelper);
            for(Member member: membersPerHousehold){
                ArrayList<String> row = new ArrayList<String>();
                row.add(household.getPhoneNumber());
                row.add(household.getName());
                row.add(member.getMemberHouseholdId());
                row.add(member.getFamilySurname());
                row.add(member.getFirstName());
                row.add(String.valueOf(member.getAge()));
                row.add(member.getGender().toString());
                row.add(member.getDeletedString());
                setStatus(household, member, row);
                row.add(String.valueOf(reasons.size()));
                row.add(StringUtils.join(reasons.toArray(), ';'));
                fileUtil.withData(row.toArray(new String[row.size()]));
            }
        }
        return fileUtil.writeCSV(activity.getFilesDir() + "/" + Constants.EXPORT_FILE_NAME);
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

    public ExportHandler with(Household household){
        households = new ArrayList<Household>();
        households.add(household);
        return this;
    }

    @Override
    public boolean shouldInactivate() {
        return households.get(0).numberOfNonDeletedMembers(new DatabaseHelper(activity)) <=0;
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


}
