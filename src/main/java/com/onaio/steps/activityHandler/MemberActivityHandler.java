package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.activity.MemberActivity;
import com.onaio.steps.activityHandler.Interface.IItemHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Member;

public class MemberActivityHandler implements IItemHandler {

    private Member member;
    private ListActivity activity;

    public MemberActivityHandler(ListActivity activity, Member member) {

        this.activity = activity;
        this.member = member;
    }

    @Override
    public boolean open() {
        if (member == null) return true;
        Intent intent = new Intent(activity.getBaseContext(), MemberActivity.class);
        intent.putExtra(Constants.MEMBER, member);
        activity.startActivityForResult(intent, Constants.MEMBER_IDENTIFIER);
        return true;
    }
}
