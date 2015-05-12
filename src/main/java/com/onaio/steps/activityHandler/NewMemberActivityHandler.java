package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.onaio.steps.R;
import com.onaio.steps.activity.NewMemberActivity;
import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuPreparer;
import com.onaio.steps.adapter.MemberAdapter;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.RequestCode;

import static android.app.Activity.RESULT_OK;

public class NewMemberActivityHandler implements IMenuHandler, IActivityResultHandler,IMenuPreparer {

    private Household household;
    private ListActivity activity;
    private MemberAdapter memberAdapter;
    private DatabaseHelper db;

    NewMemberActivityHandler(Household household, ListActivity activity, MemberAdapter memberAdapter, DatabaseHelper db) {
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
        return menu_id == R.id.action_add_member;
    }

    @Override
    public boolean open() {
        if (household== null) return true;
        Intent intent = new Intent(activity, NewMemberActivity.class);
        intent.putExtra(Constants.HOUSEHOLD,household);
        activity.startActivityForResult(intent, RequestCode.NEW_MEMBER.getCode());
        return true;
    }

    @Override
    public void handleResult(Intent data, int resultCode) {
        if (resultCode != RESULT_OK)
            return ;
        if (memberAdapter == null)
            return;
        updateHousehold();
    }

    private void updateHousehold() {
        household.setSelectedMemberId(null);
        household.update(db);
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == RequestCode.NEW_MEMBER.getCode();
    }

    @Override
    public boolean shouldInactivate() {
        return !(household.getStatus().equals(HouseholdStatus.NOT_SELECTED));
    }

    public void inactivate() {
        Button button = (Button) activity.findViewById(R.id.action_add_member);
        button.setVisibility(View.GONE);
    }

    @Override
    public void activate() {
        Button button = (Button) activity.findViewById(R.id.action_add_member);
        button.setVisibility(View.VISIBLE);
    }

}
