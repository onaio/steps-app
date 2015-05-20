package com.onaio.steps.activityHandler;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuPreparer;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

public class DeferredHandler implements IMenuHandler,IMenuPreparer {

    private final CustomDialog dialog;
    private Activity activity;
    private Household household;
    private Participant participant;
    private int MENU_ID = R.id.action_deferred;

    public DeferredHandler(Activity activity, Household household) {
        this(activity,household,new CustomDialog());
    }

    DeferredHandler(Activity activity, Household household, CustomDialog dialog) {
        this.activity = activity;
        this.household = household;
        this.dialog = dialog;
    }

    public DeferredHandler(Activity activity, Participant participant) {
        this.activity = activity;
        this.participant =participant;
        this.dialog= new CustomDialog();
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {

        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new BackHomeHandler(activity).open();
            }
        };
        if(household!=null) {
            household.setStatus(InterviewStatus.DEFERRED);
            household.update(new DatabaseHelper(activity.getApplicationContext()));
            dialog.notify(activity, confirmListener, R.string.survey_deferred_title, R.string.survey_deferred_message);
        }else {
            participant.setStatus(InterviewStatus.DEFERRED);
            participant.update(new DatabaseHelper(activity.getApplicationContext()));
            dialog.notify(activity, confirmListener, R.string.survey_deferred_title, R.string.participant_survey_deferred_message);
        }
        return true;
    }

    @Override
    public boolean shouldInactivate() {
        if(household!=null){
        boolean memberSelected = household.getStatus() == InterviewStatus.NOT_DONE;
        return !(memberSelected); }
        else {
            return participant.getStatus() == InterviewStatus.NOT_SELECTED;
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
