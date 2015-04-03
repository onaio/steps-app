package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IPrepare;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.Dialog;
import com.onaio.steps.helper.FileBuilder;
import com.onaio.steps.helper.UploadFileTask;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.ReElectReason;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportHandler implements IMenuHandler,IPrepare {

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
        try {
            FileBuilder fileBuilder = new FileBuilder().withHeader(Constants.EXPORT_FIELDS.split(","));
            for(Household household: households) {
                List<ReElectReason> reasons = ReElectReason.getAll(databaseHelper, household);
                List<Member> membersPerHousehold = Member.getAllForExport(databaseHelper, household);
                for(Member member: membersPerHousehold){
                    ArrayList<String> row = new ArrayList<String>();
                    row.add(household.getPhoneNumber());
                    row.add(household.getName());
                    row.add(member.getMemberHouseholdId());
                    row.add(member.getFamilySurname());
                    row.add(member.getFirstName());
                    row.add(String.valueOf(member.getAge()));
                    row.add(member.getGender());
                    row.add(member.getDeletedString());
                    setStatus(household, member, row);
                    row.add(String.valueOf(reasons.size()));
                    row.add(StringUtils.join(reasons.toArray(),','));
                    fileBuilder.withData(row.toArray(new String[row.size()]));
                }
            }
            File file = fileBuilder.buildCSV(activity.getFilesDir() +"/"+ Constants.EXPORT_FILE_NAME);
            new UploadFileTask(activity).execute(file);
            return true;
        } catch (IOException e) {
            new Dialog().notify(activity, Dialog.EmptyListener, R.string.something_went_wrong_try_again, R.string.error_title);
        }
        return false;
    }

    private void setStatus(Household household, Member member, ArrayList<String> row) {
        if(household.getSelectedMember() == null || household.getSelectedMember().equals("") || household.getSelectedMember().equals(String.valueOf(member.getId())))
            row.add(household.getStatus().toString());
        else
            row.add("NA");
    }

    public ExportHandler withAllHouseholds(){
        households = Household.getAll(databaseHelper);
        return this;
    }

    public ExportHandler withHousehold(Household household){
        households = new ArrayList<Household>();
        households.add(household);
        return this;
    }

    @Override
    public boolean shouldInactivate() {
        return Member.numberOfNonDeletedMembers(new DatabaseHelper(activity), households.get(0)) <=0;
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

    public IPrepare withMenu(Menu menu) {
        this.menu = menu;
        return this;
    }


}
