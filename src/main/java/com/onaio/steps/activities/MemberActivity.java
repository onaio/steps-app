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

package com.onaio.steps.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.handler.factories.MemberActivityFactory;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.model.Member;

import java.util.List;

import static com.onaio.steps.helper.Constants.HH_MEMBER;

public class MemberActivity extends Activity {

    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member);
        populateMember();
        styleActionBar();
        setTitle(member.getFirstName());
    }

    private void styleActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setIcon(R.drawable.ic_action_back);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.member_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void populateMember() {
        Intent intent = getIntent();
        member = (Member) intent.getSerializableExtra(HH_MEMBER);
        TextView ageView = (TextView) findViewById(R.id.member_age);
        TextView genderView = (TextView) findViewById(R.id.member_gender);

        ageView.setText(String.valueOf(member.getAge()));

        genderView.setText(String.format(member.getGender().getInternationalizedString(this)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<IMenuHandler> menuHandlers = MemberActivityFactory.getMenuHandlers(this, member);
        for(IMenuHandler menuHandler:menuHandlers)
            if(menuHandler.shouldOpen(item.getItemId()))
                menuHandler.open();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<IActivityResultHandler> menuHandlers = MemberActivityFactory.getMenuResultHandlers(this, member);
        for(IActivityResultHandler menuHandler:menuHandlers)
            if(menuHandler.canHandleResult(requestCode))
                menuHandler.handleResult(data, resultCode);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        List<IMenuPreparer> menuItemHandlers = MemberActivityFactory.getMenuPreparer(this, member,menu);
        for(IMenuPreparer handler:menuItemHandlers)
            if(handler.shouldInactivate())
                handler.inactivate();
        super.onPrepareOptionsMenu(menu);
        return true;
    }


}
