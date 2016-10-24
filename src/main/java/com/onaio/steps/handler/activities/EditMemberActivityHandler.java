/*
 * Copyright 2016. World Health Organization
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onaio.steps.handler.activities;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.onaio.steps.R;
import com.onaio.steps.activities.EditMemberActivity;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
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
        intent.putExtra(Constants.HH_MEMBER, member);
        activity.startActivityForResult(intent, RequestCode.EDIT_MEMBER.getCode());
        return true;
    }

    @Override
    public void handleResult(Intent intent, int resultCode) {
        if (resultCode != RESULT_OK)
            return ;
        activity.finish();
        member = (Member)intent.getSerializableExtra(Constants.HH_MEMBER);
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
    public boolean shouldDeactivate() {
        boolean isSelectedMember = String.valueOf(member.getId()).equals(member.getHousehold().getSelectedMemberId());
        boolean refusedHousehold = member.getHousehold().getStatus().equals(InterviewStatus.REFUSED);
        boolean surveyDone = member.getHousehold().getStatus().equals(InterviewStatus.DONE);
        boolean incompleteRefused = member.getHousehold().getStatus().equals(InterviewStatus.INCOMPLETE_REFUSED);
        return (isSelectedMember || refusedHousehold || surveyDone || incompleteRefused);
    }

    @Override
    public void deactivate() {
        MenuItem menuItem = menu.findItem(MENU_ID);
        menuItem.setEnabled(false);
    }

    @Override
    public void activate() {
        MenuItem menuItem = menu.findItem(MENU_ID);
        menuItem.setEnabled(true);
    }
}
