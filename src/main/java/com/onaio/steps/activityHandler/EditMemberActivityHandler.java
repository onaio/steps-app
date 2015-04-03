package com.onaio.steps.activityHandler;

import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activity.EditMemberActivity;
import com.onaio.steps.activity.NewMemberActivity;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuResultHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Member;

import java.io.Serializable;

import static android.app.Activity.RESULT_OK;

public class EditMemberActivityHandler implements IMenuHandler, IMenuResultHandler {

    private Member member;
    private Activity activity;

    public EditMemberActivityHandler(Activity activity, Member member) {
        this.activity = activity;
        this.member = member;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.action_edit;
    }

    @Override
    public boolean open() {
        if (member == null) return true;
        Intent intent = new Intent(activity.getBaseContext(), EditMemberActivity.class);
        intent.putExtra(Constants.MEMBER, member);
        activity.startActivityForResult(intent, Constants.EDIT_MEMBER_IDENTIFIER);
        return true;
    }

    @Override
    public void handleResult(Intent intent, int resultCode) {
        if (resultCode != RESULT_OK)
            return ;
        activity.finish();
        member = (Member)intent.getSerializableExtra(Constants.MEMBER);
        new MemberActivityHandler(activity, this.member).open();
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == Constants.EDIT_MEMBER_IDENTIFIER;
    }

}
