package com.onaio.steps.handler.action;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.handler.Interface.IMenuHandler;
import com.onaio.steps.handler.Interface.IMenuPreparer;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

public class RefusedHandler implements IMenuHandler,IMenuPreparer {

    private final CustomDialog dialog;
    private Household household;
    private Activity activity;
    private Participant participant;
    private int MENU_ID = R.id.action_refused;

    public RefusedHandler(Activity activity, Household household) {
        this(activity,household, new CustomDialog());
    }

    //Constructor to be used for Testing
    RefusedHandler(Activity activity, Household household, CustomDialog dialog) {
        this.activity = activity;
        this.household = household;
        this.dialog=dialog;
    }

    public RefusedHandler(Activity activity, Participant participant) {
        this.activity=activity;
        this.dialog =new CustomDialog();
        this.participant =participant;
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
        if(household!=null){
        household.setStatus(InterviewStatus.REFUSED);
        household.update(new DatabaseHelper(activity.getApplicationContext()));
        }
        else {
            participant.setStatus(InterviewStatus.REFUSED);
            participant.update(new DatabaseHelper(activity.getApplicationContext()));
        }
        new BackHomeHandler(activity).open();
    }

    private void confirm() {
        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                refuse();
            }
        };
        if(household!=null)
        dialog.confirm(activity, confirmListener, CustomDialog.EmptyListener, R.string.survey_refusal_message, R.string.survey_refusal_title);
        else
        dialog.confirm(activity, confirmListener, CustomDialog.EmptyListener, R.string.participant_survey_refusal_message, R.string.survey_refusal_title);
    }

    @Override
    public boolean shouldInactivate() {
        if(household!=null) {
            boolean memberSelected = household.getStatus() == InterviewStatus.NOT_DONE;
            boolean memberDeferred = household.getStatus() == InterviewStatus.DEFERRED;

            return !(memberSelected || memberDeferred);
        }else
        {
            boolean deferrredStatus = participant.getStatus() == InterviewStatus.DEFERRED;
            boolean notSelectedStatus = participant.getStatus() == InterviewStatus.NOT_SELECTED;
            return  !(deferrredStatus || notSelectedStatus);
        }
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
