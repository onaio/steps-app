package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;
import android.widget.ListView;

import com.onaio.steps.R;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.adapter.MemberAdapter;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

import java.util.Random;

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
        ListView listView = activity.getListView();
        Member selectedMember = getSelectedMember(listView);
        updateHousehold(selectedMember);
        updateView(listView);
        return true;
    }

    private void updateView(ListView listView) {
        MemberAdapter membersAdapter = (MemberAdapter) listView.getAdapter();
        membersAdapter.setSelectedMemberId(household.getSelectedMember());
        membersAdapter.notifyDataSetChanged();
    }

    private void updateHousehold(Member selectedMember) {
        household.setSelectedMember(String.valueOf(selectedMember.getId()));
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
