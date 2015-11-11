package com.onaio.steps.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.provider.DocumentFile;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.orchestrators.FlowOrchestrator;
import com.onaio.steps.orchestrators.flows.FlowType;
import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;

import java.io.OutputStream;

import static android.app.PendingIntent.getActivity;

public class SettingsActivity extends Activity {

    private FlowType flowType;
    private FlowOrchestrator flowOrchestrator;
    /**
     * The requestCode with which the storage access framework is triggered for input folder.
     */
    private static final int REQUEST_CODE_STORAGE_ACCESS_INPUT = 4;
    private static final String APP_SETTINGS = "STEPS SETTINGS";
    private static final String SD_CARD_PERM = "WRITE PERMISSIONS";
    private static DocumentFile sdDir;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        flowType = FlowType.valueOf(intent.getStringExtra(Constants.FLOW_TYPE));
        flowOrchestrator = new FlowOrchestrator(this);
        setHeader();
        prepareViewWithData();
        // Request permissions.
        sharedPreferences = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
        String set = sharedPreferences.getString(SD_CARD_PERM, null);
        if (set == null) {
            triggerStorageAccessFramework();
        }
    }

    private void setHeader() {
        setContentView(R.layout.settings);
        TextView header = (TextView) findViewById(R.id.form_header);
        header.setText(R.string.action_settings);
    }

    private void prepareViewWithData() {
        flowOrchestrator.prepareSettingScreen(flowType);
    }

    public void save(View view) {
        try {
                flowOrchestrator.saveSettings(flowType);
                setResult(RESULT_OK, this.getIntent());
                finish();
        } catch (InvalidDataException e) {
            new CustomDialog().notify(this, CustomDialog.EmptyListener, e.getMessage(), R.string.error_title);
        }
    }

    public void cancel(View view) {
        finish();
    }


    public void enableHouseholdFlow(View view) {
        flowType = FlowType.Household;
        setHeader();
        prepareViewWithData();
    }

    public void enableParticipantFlow(View view) {
        flowType = FlowType.Participant;
        setHeader();
        prepareViewWithData();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void triggerStorageAccessFramework() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, REQUEST_CODE_STORAGE_ACCESS_INPUT);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public final void onActivityResult(final int requestCode, final int resultCode, final Intent resultData) {
        if (requestCode == REQUEST_CODE_STORAGE_ACCESS_INPUT) {
            Uri treeUri = null;
            if (resultCode == Activity.RESULT_OK) {
                // Get Uri from Storage Access Framework.
                treeUri = resultData.getData();

                // Persist URI in shared preference so that you can use it later.
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SD_CARD_PERM, "GRANT_WRITE_ACCESS");
                editor.commit();

                // Persist access permissions.
                final int takeFlags = resultData.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                // noinspection ResourceType
                this.getContentResolver().takePersistableUriPermission(treeUri, takeFlags);

                DocumentFile pickedDir = DocumentFile.fromTreeUri(this, treeUri);
                sdDir = pickedDir;
                // Create a new file and write into it
                DocumentFile newFile = pickedDir.createFile("text/plain", "Test File");
                try {
                    OutputStream out = getContentResolver().openOutputStream(newFile.getUri());
                    out.write("A long time ago...".getBytes());
                    out.close();
                } catch (Exception e) {

                }

                Toast.makeText(this, "Granting permissions", Toast.LENGTH_LONG).show();
            }
        }
    }

}
