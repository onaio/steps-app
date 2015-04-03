package com.onaio.steps.activityHandler;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IPrepare;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

public class RefusedHandler implements IMenuHandler,IPrepare {

    private  final AlertDialog.Builder alertDialog;
    private Household household;
    private ListActivity activity;
    private int MENU_ID = R.id.action_refused;

    public RefusedHandler(ListActivity activity, Household household) {
        this(activity,household, new AlertDialog.Builder(activity.getApplicationContext()));
       ;
    }

    //Constructor to be used for Testing
    public RefusedHandler(ListActivity activity, Household household, AlertDialog.Builder alertDialog) {
        this.activity = activity;
        this.household = household;
        this.alertDialog=alertDialog;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id==MENU_ID;
    }

    @Override
    public boolean open() {
        confirm();
        return true;
    }

    private void refuse() {
        household.setStatus(HouseholdStatus.REFUSED);
        household.update(new DatabaseHelper(activity.getApplicationContext()));
        StepsActivityHandler handler = new StepsActivityHandler(activity);
        handler.open();
    }

    private void confirm() {
        alertDialog
                .setTitle(activity.getString(R.string.survey_refusal_title))
                .setMessage(activity.getString(R.string.survey_refusal_message))
                .setPositiveButton(R.string.confirm_ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        refuse();
                    }
                })
                .setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
    }

    @Override
    public boolean shouldInactivate() {
        boolean memberSelected = household.getStatus() == HouseholdStatus.NOT_DONE;
        boolean memberDeferred = household.getStatus() == HouseholdStatus.DEFERRED;
        return !(memberSelected || memberDeferred);
    }

    @Override
    public void inactivate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.INVISIBLE);
    }

    @Override
    public void activate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.VISIBLE);
    }
}
