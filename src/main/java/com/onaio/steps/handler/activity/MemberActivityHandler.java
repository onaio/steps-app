package com.onaio.steps.handler.activity;

import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.activity.MemberActivity;
import com.onaio.steps.handler.Interface.IListItemHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Member;

public class MemberActivityHandler implements IListItemHandler {

    private Member member;
    private Activity activity;

    public MemberActivityHandler(Activity activity, Member member) {
        this.activity = activity;
        this.member = member;
    }

    @Override
    public boolean open() {
        if (member == null) return true;
        Intent intent = new Intent(activity, MemberActivity.class);
        intent.putExtra(Constants.MEMBER, member);
        activity.startActivity(intent);
        return true;
    }
}
