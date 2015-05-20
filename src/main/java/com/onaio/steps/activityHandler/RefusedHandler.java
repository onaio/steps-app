package com.onaio.steps.activityHandler;

import android.app.Activity;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuPreparer;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Participant;

public class RefusedHandler implements IMenuHandler,IMenuPreparer {

    private final CustomDialog dialog;
    private Household household;
    private ListActivity activity;
    private int MENU_ID = R.id.action_refused;

    public RefusedHandler(ListActivity activity, Household household) {
        this(activity,household, new CustomDialog());
    }

    //Constructor to be used for Testing
    RefusedHandler(ListActivity activity, Household household, CustomDialog dialog) {
        this.activity = activity;
        this.household = household;
        this.dialog=dialog;
    }

    public RefusedHandler(Activity activity, Participant participant) {
        this.dialog =new CustomDialog();
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
        household.setStatus(InterviewStatus.REFUSED);
        household.update(new DatabaseHelper(activity.getApplicationContext()));
        new BackHomeHandler(activity).open();
    }

    private void confirm() {
        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                refuse();
            }
        };
        dialog.confirm(activity, confirmListener, CustomDialog.EmptyListener, R.string.survey_refusal_message, R.string.survey_refusal_title);
    }

    @Override
    public boolean shouldInactivate() {
        boolean memberSelected = household.getStatus() == InterviewStatus.NOT_DONE;
        boolean memberDeferred = household.getStatus() == InterviewStatus.DEFERRED;
        return !(memberSelected || memberDeferred);
    }

    @Override
    public void inactivate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.GONE);
    }

    @Override
    public void activate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.VISIBLE);
    }
}
