package com.onaio.steps.handler.actions;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;

import com.onaio.steps.R;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;

public class DeleteMemberHandler implements IMenuHandler,IMenuPreparer {
    private final CustomDialog dialog;
    private Activity activity;
    private Member member;
    private static final int MENU_ID= R.id.action_member_delete;
    private Menu menu;

    public DeleteMemberHandler(Activity activity, Member member) {
        this(activity, member, new CustomDialog());
    }

    DeleteMemberHandler(Activity activity, Member member, CustomDialog dialog) {
        this.activity = activity;
        this.member = member;
        this.dialog = dialog;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == MENU_ID;
    }

    @Override
    public boolean open() {
        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                member.delete(new DatabaseHelper(activity));
                new BackHomeHandler(activity).open();
            }
        };
        dialog.confirm(activity, confirmListener, CustomDialog.EmptyListener, R.string.member_delete_confirm, R.string.confirm_ok);
        return true;
    }

    public DeleteMemberHandler withMenu(Menu menu) {
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
