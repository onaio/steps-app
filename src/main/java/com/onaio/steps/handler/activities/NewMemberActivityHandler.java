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

import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.onaio.steps.R;
import com.onaio.steps.activities.NewMemberActivity;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.adapters.MemberAdapter;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
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
        intent.putExtra(Constants.HH_HOUSEHOLD,household);
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
        if (household.getStatus() == InterviewStatus.EMPTY_HOUSEHOLD) {
            household.setStatus(InterviewStatus.SELECTION_NOT_DONE);
        }
        household.update(db);
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == RequestCode.NEW_MEMBER.getCode();
    }

    @Override
    public boolean shouldDeactivate() {
        return !(household.getStatus().equals(InterviewStatus.SELECTION_NOT_DONE))
                && !(household.getStatus().equals(InterviewStatus.EMPTY_HOUSEHOLD))
                && !(household.getStatus().equals(InterviewStatus.CANCEL_SELECTION));
    }

    public void deactivate() {
        Button button = (Button) activity.findViewById(R.id.action_add_member);
        button.setVisibility(View.GONE);
    }

    @Override
    public void activate() {
        Button button = (Button) activity.findViewById(R.id.action_add_member);
        button.setVisibility(View.VISIBLE);
    }

}
