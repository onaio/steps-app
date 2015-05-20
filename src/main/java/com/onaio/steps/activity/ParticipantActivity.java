package com.onaio.steps.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Factory.ParticipantActivityFactory;
import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuPreparer;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Participant;

import java.util.List;

public class ParticipantActivity extends Activity{

    private DatabaseHelper db;
;   private Participant participant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_participant);
        participant = (Participant) getIntent().getSerializableExtra(Constants.PARTICIPANT);
        db = new DatabaseHelper(this);
        styleActionBar();
        prepareCustomMenu();
        populateMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareCustomMenu();
        populateParticipant();
    }

    private void populateParticipant() {

        TextView participantName = (TextView) findViewById(R.id.selected_participant_name);
        TextView participantDetails = (TextView) findViewById(R.id.selected_participant_details);
        participantName.setText(participant.getFormattedName());
        participantDetails.setText(participant.getFormattedDetail());
    }

    private void populateMessage() {
        TextView viewById = (TextView) findViewById(R.id.survey_message);
        switch (participant.getStatus()){
            case DONE: viewById.setText(R.string.interview_done_message);
                viewById.setTextColor(Color.parseColor(Constants.TEXT_GREEN));
                break;
            case REFUSED: viewById.setText(R.string.interview_refused_message);
                viewById.setTextColor(Color.RED);
                break;
            default: viewById.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<IMenuHandler> menuHandlers = ParticipantActivityFactory.getMenuHandlers(this, participant);
        for(IMenuHandler menuHandler:menuHandlers)
            if(menuHandler.shouldOpen(item.getItemId()))
                menuHandler.open();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.participant_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<IActivityResultHandler> menuHandlers = ParticipantActivityFactory.getResultHandlers(this, participant);
        for(IActivityResultHandler menuHandler:menuHandlers)
            if(menuHandler.canHandleResult(requestCode))
                menuHandler.handleResult(data, resultCode);
    }

    private void styleActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setIcon(R.drawable.ic_action_back);
        actionBar.setTitle(this.getString(R.string.pid)+" "+participant.getId());
    }

    public void handleCustomMenu(View view) {
        List<IMenuHandler> bottomMenuItem = ParticipantActivityFactory.getCustomMenuHandler(this, participant);
        for(IMenuHandler menuItem: bottomMenuItem)
            if(menuItem.shouldOpen(view.getId()))
                menuItem.open();
    }

    private void prepareCustomMenu() {
        List<IMenuPreparer> customMenus = ParticipantActivityFactory.getCustomMenuPreparer(this, participant);
        for(IMenuPreparer customMenu:customMenus)
            if(customMenu.shouldInactivate())
                customMenu.inactivate();
            else
                customMenu.activate();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        List<IMenuPreparer> menuItemHandlers =ParticipantActivityFactory.getMenuPreparer(this, participant, menu);
        for(IMenuPreparer handler:menuItemHandlers)
            if(handler.shouldInactivate())
                handler.inactivate();
        super.onPrepareOptionsMenu(menu);
        return true;
    }
}
