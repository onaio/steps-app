package com.onaio.steps.external;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.FileUtil;

import java.io.IOException;
import java.util.List;


public class DataProviderActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private FileUtil fileUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize progress dialog
        initProgressDialog();

        // initialize file utility class for reading csv file
        fileUtil = new FileUtil();

        // try to start reading data file
        readDataFile(returnedIntent());
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait... data is collecting");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void readDataFile(Intent returnedIntent) {

        try {
            List<String[]> data = fileUtil.readFile(getFilesDir().getAbsolutePath() + "/" + Constants.ODK_DATA_FILENAME);
            extractData(data, returnedIntent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            finishing();
        }
    }

    private void extractData(List<String[]> data, Intent returnedIntent) {
        if (!data.isEmpty()) {
            String[] row = data.get(0);
            for (DataKeys dataKey : DataKeys.values()) {
                returnedIntent.putExtra(dataKey.getKey(), row[dataKey.getIndex()]);
            }
            setResult(Activity.RESULT_OK, returnedIntent);
        }
    }

    private void finishing() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        finish();
    }

    private Intent returnedIntent() {
        return new Intent();
    }
}
