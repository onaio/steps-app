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
    private ListActivity activity;

    public MemberActivityHandler(ListActivity activity, Member member) {

        this.activity = activity;
        this.member = member;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return true;
    }

    @Override
    public boolean open() {
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
    public void handleResult(Intent data, int resultCode) {
    }
}
