package com.onaio.steps.handler.activity;

import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activity.NewParticipantActivity;
import com.onaio.steps.activity.ParticipantActivity;
import com.onaio.steps.handler.Interface.IActivityResultHandler;
import com.onaio.steps.handler.Interface.IMenuHandler;
import com.onaio.steps.adapter.ParticipantAdapter;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Participant;
import com.onaio.steps.model.RequestCode;

import static android.app.Activity.RESULT_OK;

public class NewParticipantActivityHandler implements IActivityResultHandler, IMenuHandler {

    private ListActivity activity;

    public NewParticipantActivityHandler(ListActivity activity) {
        this.activity = activity;
    }


    @Override
    public void handleResult(Intent data, int resultCode) {

        if (resultCode == RESULT_OK) {
            ParticipantAdapter participantAdapter = (ParticipantAdapter) activity.getListView().getAdapter();
            if (participantAdapter == null)
                return;
            participantAdapter.reinitialize(Participant.getAllParticipants(new DatabaseHelper(activity.getApplicationContext())));
            participantAdapter.notifyDataSetChanged();

            Intent participantActivityIntent = new Intent(activity, ParticipantActivity.class);
            participantActivityIntent.putExtra(Constants.PARTICIPANT, data.getSerializableExtra(Constants.PARTICIPANT));
            activity.startActivity(participantActivityIntent);
        }

    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == RequestCode.NEW_PARTICIPANT.getCode();
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.action_add_household;
    }

    @Override
    public boolean open() {
        Intent intent = new Intent(activity.getBaseContext(), NewParticipantActivity.class);
        activity.startActivityForResult(intent,RequestCode.NEW_PARTICIPANT.getCode());
        return true;
    }
}
