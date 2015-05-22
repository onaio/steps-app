package com.onaio.steps.handler.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.onaio.steps.R;
import com.onaio.steps.activity.EditMemberActivity;
import com.onaio.steps.handler.Interface.IActivityResultHandler;
import com.onaio.steps.handler.Interface.IMenuHandler;
import com.onaio.steps.handler.Interface.IMenuPreparer;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.RequestCode;

import static android.app.Activity.RESULT_OK;

public class EditMemberActivityHandler implements IMenuHandler, IActivityResultHandler,IMenuPreparer {

    private static final int MENU_ID = R.id.action_edit;
    private Member member;
    private Activity activity;
    private Menu menu;

    public EditMemberActivityHandler(Activity activity, Member member) {
        this.activity = activity;
        this.member = member;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        if (member == null) return true;
        Intent intent = new Intent(activity, EditMemberActivity.class);
        intent.putExtra(Constants.MEMBER, member);
        activity.startActivityForResult(intent, RequestCode.EDIT_MEMBER.getCode());
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
        return requestCode == RequestCode.EDIT_MEMBER.getCode();
    }

    public EditMemberActivityHandler withMenu(Menu menu) {
        this.menu = menu;
        return this;
    }

    @Override
    public boolean shouldInactivate() {
        boolean isSelectedMember = String.valueOf(member.getId()).equals(member.getHousehold().getSelectedMemberId());
        boolean refusedHousehold = member.getHousehold().getStatus().equals(InterviewStatus.REFUSED);
        boolean surveyDone = member.getHousehold().getStatus().equals(InterviewStatus.DONE);
        return (isSelectedMember || refusedHousehold || surveyDone);
    }

    @Override
    public void inactivate() {
        MenuItem menuItem = menu.findItem(MENU_ID);
        menuItem.setEnabled(false);
    }

    @Override
    public void activate() {
        MenuItem menuItem = menu.findItem(MENU_ID);
        menuItem.setEnabled(true);
    }
}
