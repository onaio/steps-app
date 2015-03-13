package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import com.onaio.steps.R;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.UploadFileTask;
import com.onaio.steps.model.Household;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportActivityHandler implements IActivityHandler {

    public static final String EXPORT_FILE_NAME = "/households.csv";

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.action_export;
    }

    @Override
    public boolean open(ListActivity activity) {
        try {
            File file = new File(activity.getFilesDir() + EXPORT_FILE_NAME);
            Cursor cursor = new DatabaseHelper(activity.getApplicationContext()).exec(Household.FIND_ALL_QUERY);
            CSVWriter writer = new CSVWriter(new FileWriter(file), '\t');
            writer.writeNext(cursor.getColumnNames());
            while (cursor.moveToNext()) {
                List<String> columns = new ArrayList<String>();
                for (int count = 0; count < cursor.getColumnCount(); count++) {
                    columns.add(cursor.getString(count));
                }
                writer.writeNext(columns.toArray(new String[columns.size()]));
            }
            writer.close();

            new UploadFileTask(activity).execute(file);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return false;
    }

    @Override
    public IActivityHandler with(Object data) {
        return this;
    }

    @Override
    public void handleResult(ListActivity activity, Intent data, int resultCode) {
    }

}
