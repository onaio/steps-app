package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;
import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.UploadFileTask;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportHandler implements IHandler {

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
            File file = new File(activity.getFilesDir() + Constants.EXPORT_FILE_NAME);
            CSVWriter writer = new CSVWriter(new FileWriter(file), '\t');
            String[] exportFields = Constants.EXPORT_FIELDS.split(",");
            writer.writeNext(exportFields);
            for(Household household: households) {
                List<Member> membersPerHousehold = Member.getAll(databaseHelper, household);
                for(Member member: membersPerHousehold){
                    ArrayList<String> row = new ArrayList<String>();
                    row.add(household.getName());
                    row.add(household.getPhoneNumber());
                    row.add(member.getMemberHouseholdId());
                    row.add(member.getName());
                    row.add(String.valueOf(member.getAge()));
                    row.add(member.getGender());
                    writer.writeNext(row.toArray(new String[exportFields.length]));
                }
            }
            writer.close();

            new UploadFileTask(activity).execute(file);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
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

    @Override
    public boolean canHandleResult(int requestCode) {
        return false;
    }

    @Override
    public void handleResult(Intent data, int resultCode) {
    }

}
