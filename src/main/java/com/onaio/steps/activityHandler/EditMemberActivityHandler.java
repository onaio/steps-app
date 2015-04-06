package com.onaio.steps.activityHandler;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.onaio.steps.R;
import com.onaio.steps.activity.EditMemberActivity;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuResultHandler;
import com.onaio.steps.activityHandler.Interface.IPrepare;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Member;

import static android.app.Activity.RESULT_OK;

public class EditMemberActivityHandler implements IMenuHandler, IMenuResultHandler,IPrepare {

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

    public EditMemberActivityHandler withMenu(Menu menu) {
        this.menu = menu;
        return this;
    }

    @Override
    public boolean shouldInactivate() {
        return String.valueOf(member.getId()).equals(member.getHousehold().getSelectedMember());
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
