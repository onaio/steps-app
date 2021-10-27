package com.onaio.steps.handler.actions;

import android.app.Dialog;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.onaio.steps.R;
import com.onaio.steps.activities.ParticipantListActivity;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.handler.interfaces.IViewPreparer;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Participant;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles actions related to the 'Submit Data' button
 *
 * Created by Jason Rogena - jrogena@ona.io on 03/10/2016.
 */

public class SubmitDataHandler implements IMenuHandler,IMenuPreparer, IViewPreparer {
    private final int MENU_ID = R.id.action_submit_data;
    private final ListActivity activity;
    private Menu menu;
    private ExportHandler exportHandler;
    private FinalisedFormHandler finalisedFormHandler;
    private List<Household> households;
    private List<Participant> participants;

    public SubmitDataHandler(ListActivity activity) {
        this.activity = activity;
        exportHandler = new ExportHandler(activity);
        finalisedFormHandler = new FinalisedFormHandler(activity);
        participants  = new ArrayList<>();
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
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                                }

                                @Override
                                public void onFileUploaded() {
                                    new CustomDialog().notify(activity, CustomDialog.EmptyListener, R.string.export_complete, R.string.export_complete_message);
                                }

                                @Override
                                public void onFileFailedToUpload(String error) {
                                    new CustomDialog().notify(activity, CustomDialog.EmptyListener, error, R.string.error_title);
                                }
                            });
                            exportHandler.open();
                        }

                        dialog.dismiss();
                    }
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
        this.households = households;
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
}
