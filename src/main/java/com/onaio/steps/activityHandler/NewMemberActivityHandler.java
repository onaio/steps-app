package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activity.NewMemberActivity;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuResultHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.adapter.MemberAdapter;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class NewMemberActivityHandler implements IMenuHandler, IMenuResultHandler {

    private Household household;
    private ListActivity activity;
    private MemberAdapter memberAdapter;
    private DatabaseHelper db;

    public NewMemberActivityHandler(Household household, ListActivity activity, MemberAdapter memberAdapter, DatabaseHelper db) {
        this.household = household;
        this.activity = activity;
        this.memberAdapter = memberAdapter;
        this.db = db;
    }

    public NewMemberActivityHandler(ListActivity activity, Household household) {
        this(household,activity,(MemberAdapter) activity.getListView().getAdapter(),new DatabaseHelper(activity.getApplicationContext()));
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.action_add;
    }

    @Override
    public boolean open() {
        if (household== null) return true;
        Intent intent = new Intent(activity, NewMemberActivity.class);
        intent.putExtra(Constants.HOUSEHOLD,household);
        activity.startActivityForResult(intent, Constants.NEW_MEMBER_IDENTIFIER);
        return true;
    }

    @Override
    public void handleResult(Intent data, int resultCode) {
        if (resultCode != RESULT_OK)
            return ;
        if (memberAdapter == null)
            return;
        List<Member> members = Member.getAll(db, household);
        memberAdapter.reinitialize(members);
        memberAdapter.notifyDataSetChanged();
        activity.invalidateOptionsMenu();
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == Constants.NEW_MEMBER_IDENTIFIER;
    }

}
