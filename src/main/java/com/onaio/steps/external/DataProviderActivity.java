package com.onaio.steps.external;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
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

    @VisibleForTesting
    public void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.data_populate_message));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @VisibleForTesting
    public void readDataFile(Intent returnedIntent) {

        try {
            List<String[]> data = fileUtil.readFile(getFilesDir().getAbsolutePath() + "/" + Constants.ODK_DATA_FILENAME);
            extractData(data, returnedIntent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            finishing();
        }
    }

    @VisibleForTesting
    public void extractData(List<String[]> data, Intent returnedIntent) {
        if (!data.isEmpty()) {
            String[] row = data.get(0);
            for (DataKeys dataKey : DataKeys.values()) {
                returnedIntent.putExtra(dataKey.getKey(), row[dataKey.getIndex()]);
            }
            setResult(Activity.RESULT_OK, returnedIntent);
        }
    }

    @VisibleForTesting
    public void finishing() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        finish();
    }

    @VisibleForTesting
    public Intent returnedIntent() {
        return new Intent();
    }
}
