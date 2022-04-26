package com.onaio.steps.handler.actions;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.onaio.steps.R;
import com.onaio.steps.activities.ParticipantListActivity;
import com.onaio.steps.dialogs.HouseholdUploadResultDialog;
import com.onaio.steps.handler.activities.HouseholdServerStatusUpdater;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.handler.interfaces.IViewPreparer;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Participant;
import com.onaio.steps.model.UploadResult;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles actions related to the 'Submit Data' button
 *
 * Created by Jason Rogena - jrogena@ona.io on 03/10/2016.
 */

public class SubmitDataHandler implements IMenuHandler,IMenuPreparer, IViewPreparer {
    private final int MENU_ID = R.id.action_submit_data;
    private final AppCompatActivity activity;
    private Menu menu;
    private final ExportHandler exportHandler;
    private final FinalisedFormHandler finalisedFormHandler;
    private List<Participant> participants;
    private final HouseholdServerStatusUpdater householdServerStatusUpdater;
    private final ProgressDialog progressDialog;

    public SubmitDataHandler(AppCompatActivity activity) {
        this.activity = activity;
        exportHandler = new ExportHandler(activity);
        finalisedFormHandler = new FinalisedFormHandler(activity);
        participants  = new ArrayList<>();
        householdServerStatusUpdater = new HouseholdServerStatusUpdater(activity);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(activity.getString(R.string.uploading_household));
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        if (activity instanceof ParticipantListActivity) {
            // Only submitting ODK Data, just launch ODK
            finalisedFormHandler.open();
        } else {
            final Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.dialog_submit_data);
            dialog.setTitle(R.string.action_submit_data);
            Button okButton = (Button) dialog.findViewById(R.id.okButton);
            okButton.setOnClickListener(view -> {
                CheckBox exportHouseholdListCheckBox = (CheckBox) dialog.findViewById(R.id.exportHouseholdListCheckBox);
                final CheckBox submitRecordsCheckBox = (CheckBox) dialog.findViewById(R.id.submitRecordsCheckBox);
                if (exportHouseholdListCheckBox.isChecked() || submitRecordsCheckBox.isChecked()) {
                    if (submitRecordsCheckBox.isChecked() && !exportHouseholdListCheckBox.isChecked()) {
                        finalisedFormHandler.open();
                    } else if (exportHouseholdListCheckBox.isChecked()) {
                        exportHandler.setOnExportListener(new ExportHandler.OnExportListener() {
                            @Override
                            public void onExportCancelled() {

                            }

                            @Override
                            public void onExportStart() {

                            }

                            @Override
                            public void onFileSaved() {
                                if (submitRecordsCheckBox.isChecked()) {
                                    finalisedFormHandler.open();
                                }
                                progressDialog.show();
                            }

                            @Override
                            public void onFileUploaded(List<UploadResult> uploadResults) {
                                progressDialog.dismiss();
                                householdServerStatusUpdater.markAllSent(uploadResults);
                                displayUploadResult(uploadResults);
                            }

                            @Override
                            public void onError(String error) {
                                progressDialog.dismiss();
                                new CustomDialog().notify(activity, CustomDialog.EmptyListener, error, R.string.error_title);
                            }
                        });
                        exportHandler.open();
                    }

                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        return true;
    }

    @Override
    public boolean shouldDeactivate() {
        if (activity instanceof ParticipantListActivity) {
            return participants.size() == 0;
        } else {
            return exportHandler.shouldDeactivate();
        }
    }

    @Override
    public void deactivate() {
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

    public SubmitDataHandler with(List<Household> households){
        exportHandler.with(households);
        return this;
    }

    public SubmitDataHandler withParticipants(List<Participant> participants) {
        this.participants = participants;
        return this;
    }

    @Override
    public boolean shouldBeDisabled() {
        return shouldDeactivate();
    }

    @Override
    public void disable() {
        activity.findViewById(MENU_ID).setBackgroundColor(activity.getResources().getColor(R.color.button_disabled));
        activity.findViewById(MENU_ID).setEnabled(false);
    }

    @Override
    public void enable() {
        activity.findViewById(MENU_ID).setBackground(activity.getResources().getDrawable(R.drawable.button_selector));
        activity.findViewById(MENU_ID).setEnabled(true);
    }

    public void displayUploadResult(List<UploadResult> uploadResults) {

        if (!activity.isFinishing()) {
            HouseholdUploadResultDialog householdUploadResultDialog = new HouseholdUploadResultDialog(uploadResults);
            FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(householdUploadResultDialog, HouseholdUploadResultDialog.class.getSimpleName());
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}
