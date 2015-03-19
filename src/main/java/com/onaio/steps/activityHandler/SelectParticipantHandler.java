package com.onaio.steps.activityHandler;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.onaio.steps.R;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.adapter.MemberAdapter;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.Member;

import java.util.Random;

import static com.onaio.steps.model.HouseholdStatus.*;

public class SelectParticipantHandler implements IHandler{

    private ListActivity activity;
    private Household household;

    public SelectParticipantHandler(ListActivity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.action_select_participant;
    }

    @Override
    public boolean open() {
        switch(household.getStatus()){
            case OPEN: selectParticipant();
                break;
            case SELECTED: confirm();
                break;
            default: canNotReElect();
        }
        return true;
    }

    private void canNotReElect() {
        new AlertDialog.Builder(activity)
                .setTitle("Can not re elect")
                .setMessage("You can not re elect the participant with the current status.")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create().show();
    }

    private void confirm() {
        LayoutInflater factory = LayoutInflater.from(activity);
        View confirmation = factory.inflate(R.layout.selection_confirm, null);
        new AlertDialog.Builder(activity)
                .setTitle("Confirm re-election of the participant")
                .setView(confirmation)
                .setPositiveButton(R.string.confirm_ok,new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectParticipant();
                    }
                })
                .setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
    }

    private void selectParticipant() {
        ListView listView = activity.getListView();
        Member selectedMember = getSelectedMember(listView);
        updateHousehold(selectedMember);
        updateView(listView);
        activity.invalidateOptionsMenu();
    }

    private void updateView(ListView listView) {
        MemberAdapter membersAdapter = (MemberAdapter) listView.getAdapter();
        membersAdapter.setSelectedMemberId(household.getSelectedMember());
        membersAdapter.notifyDataSetChanged();
    }

    private void updateHousehold(Member selectedMember) {
        household.setSelectedMember(String.valueOf(selectedMember.getId()));
        household.setStatus(SELECTED);
        household.update(new DatabaseHelper(activity.getApplicationContext()));
    }

    private Member getSelectedMember(ListView listView) {
        int totalMembers = Member.numberOfMembers(new DatabaseHelper(activity.getApplicationContext()), household);
        Random random = new Random();
        int selectedParticipant = random.nextInt(totalMembers);
        return (Member) listView.getItemAtPosition(selectedParticipant);
    }

    @Override
    public void handleResult(Intent data, int resultCode) {

    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return false;
    }
}
