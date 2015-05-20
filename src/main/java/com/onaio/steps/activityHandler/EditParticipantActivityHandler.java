package com.onaio.steps.activityHandler;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.onaio.steps.R;
import com.onaio.steps.activity.EditParticipantActivity;
import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuPreparer;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;
import com.onaio.steps.model.RequestCode;

import static android.app.Activity.RESULT_OK;


public class EditParticipantActivityHandler implements IMenuHandler, IActivityResultHandler, IMenuPreparer {

    private final int MENU_ID = R.id.action_edit;
    private  Activity activity;
    private Participant participant;
    private Menu menu;

    public EditParticipantActivityHandler(Activity activity, Participant participant) {
        this.activity=activity;
        this.participant=participant;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        if(participant==null) return true;
        Intent intent = new Intent(activity, EditParticipantActivity.class);
        intent.putExtra(Constants.PARTICIPANT,participant);
        activity.startActivityForResult(intent,RequestCode.EDIT_PARTICIPANT.getCode());
        return true;
    }

    @Override
    public void handleResult(Intent intent, int resultCode) {
        if (resultCode != RESULT_OK)
            return ;
        activity.finish();
        participant = (Participant)intent.getSerializableExtra(Constants.PARTICIPANT);
        new ParticipantActivityHandler(activity, this.participant).open();
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == RequestCode.EDIT_PARTICIPANT.getCode();
    }

    @Override
    public boolean shouldInactivate() {
        boolean doneStatus = participant.getStatus() == InterviewStatus.DONE;
        boolean refusedStatus = participant.getStatus() == InterviewStatus.REFUSED;
        boolean incompleteStatus = participant.getStatus() == InterviewStatus.INCOMPLETE;
        return doneStatus || refusedStatus || incompleteStatus ;

    }

    @Override
    public void inactivate() {
        MenuItem menuItem = menu.findItem(MENU_ID);
        menuItem.setEnabled(false);
    }

    @Override
    public void activate() {
        MenuItem menuItem = menu.findItem(MENU_ID);
        menuItem.setEnabled(true);
    }

    public IMenuPreparer withMenu(Menu menu) { this.menu =menu; return this;    }
}
