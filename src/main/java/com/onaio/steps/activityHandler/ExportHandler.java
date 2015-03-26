package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
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

public class ExportHandler implements IMenuHandler {

    private List<Household> households;
    private ListActivity activity;
    private DatabaseHelper databaseHelper;

    public ExportHandler(ListActivity activity) {

        this.activity = activity;
        databaseHelper = new DatabaseHelper(activity.getApplicationContext());
        households = new ArrayList<Household>();
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.action_export;
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
                    row.add(household.getStatus().toString());
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

    public ExportHandler withAllHouseholds(){
        households = Household.getAll(databaseHelper);
        return this;
    }

    public ExportHandler withHousehold(Household household){
        households = new ArrayList<Household>();
        households.add(household);
        return this;
    }
}
