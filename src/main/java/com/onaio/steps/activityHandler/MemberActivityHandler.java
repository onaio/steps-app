package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.activity.MemberActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

public class MemberActivityHandler implements IActivityHandler {

    private Member member;

    @Override
    public boolean shouldOpen(int menu_id) {
        return true;
    }

    @Override
    public boolean open(ListActivity activity) {
        if (member == null) return true;
        Intent intent = new Intent(activity.getBaseContext(), MemberActivity.class);
        intent.putExtra(Constants.MEMBER, member);
        activity.startActivityForResult(intent, Constants.MEMBER_IDENTIFIER);
        return true;
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == Constants.MEMBER_IDENTIFIER;
    }

    @Override
    public IActivityHandler with(Object data) {
        member = ((Member) data);
        return this;
    }

    @Override
    public void handleResult(ListActivity activity, Intent data, int resultCode) {
    }
}
