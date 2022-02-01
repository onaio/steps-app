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

import static com.onaio.steps.model.InterviewStatus.CANCEL_SELECTION;
import static com.onaio.steps.model.InterviewStatus.DEFERRED;
import static com.onaio.steps.model.InterviewStatus.INCOMPLETE;
import static com.onaio.steps.model.InterviewStatus.NOT_DONE;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.onaio.steps.R;
import com.onaio.steps.adapters.MemberAdapter;
import com.onaio.steps.handler.HouseholdActivityBackButtonPreparer;
import com.onaio.steps.handler.factories.HouseholdActivityFactory;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;
import com.onaio.steps.modelViewWrapper.SelectedMemberViewWrapper;

import java.util.List;

public class HouseholdActivity extends AppCompatActivity {

    private Household household;
    private DatabaseHelper db;
    private RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.household);
        list = findViewById(R.id.list);
        household = (Household) getIntent().getSerializableExtra(Constants.HH_HOUSEHOLD);
        db = new DatabaseHelper(this);
        styleActionBar();
        handleMembers();
        prepareCustomMenu();
        populateMessage();
    }

    @Override
    public void onBackPressed() {
        HouseholdActivityBackButtonPreparer handler = new HouseholdActivityBackButtonPreparer(this, household);
        if(!handler.shouldDeactivate()) {
            super.onBackPressed();
        }
    }

    private void populateMessage() {
        TextView viewById = (TextView) findViewById(R.id.survey_message);
        switch (household.getStatus()){
            case DONE: viewById.setText(R.string.survey_done_message);
                viewById.setTextColor(Color.parseColor(Constants.TEXT_GREEN));
                break;
            case INCOMPLETE_REFUSED: viewById.setText(R.string.survey_partially_completed);
                viewById.setTextColor(Color.RED);
                break;
            case REFUSED: viewById.setText(R.string.survey_refused_message);
                viewById.setTextColor(Color.RED);
                break;
            default: viewById.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareCustomMenu();
        populateMembers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<IMenuHandler> menuHandlers = HouseholdActivityFactory.getMenuHandlers(this, household);
        for(IMenuHandler menuHandler:menuHandlers)
            if(menuHandler.shouldOpen(item.getItemId()))
                menuHandler.open();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.household_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<IActivityResultHandler> menuHandlers = HouseholdActivityFactory.getResultHandlers(this, household);
        for(IActivityResultHandler menuHandler:menuHandlers)
            if(menuHandler.canHandleResult(requestCode))
                menuHandler.handleResult(data, resultCode);
    }

    public void handleCustomMenu(View view) {
        List<IMenuHandler> bottomMenuItem = HouseholdActivityFactory.getCustomMenuHandler(this, household);
        for(IMenuHandler menuItem: bottomMenuItem)
            if(menuItem.shouldOpen(view.getId()))
                menuItem.open();
    }

    private void styleActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setIcon(R.drawable.ic_action_back);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        actionBar.setElevation(0);

        TextView idHeader = findViewById(R.id.household_id_header);
        TextView numberHeader = findViewById(R.id.household_number_header);
        TextView commentView = findViewById(R.id.text_view_comment);

        idHeader.setText(String.format(getString(R.string.household_id_hint)+": %s",household.getName()));
        idHeader.setTextColor(Color.parseColor(Constants.HEADER_GREEN));

        if(!household.getPhoneNumber().isEmpty()) {
            numberHeader.setText(String.format(getString(R.string.phone_number) + " %s", household.getPhoneNumber()));

        }
        if(!household.getComments().equals("")) {
            commentView.setText(household.getComments());
        }

        actionBar.setTitle("");
    }

    private void handleMembers() {
        populateMembers();
    }

    private void prepareCustomMenu() {
        List<IMenuPreparer> customMenus = HouseholdActivityFactory.getCustomMenuPreparer(this, household);
        for(IMenuPreparer customMenu:customMenus)
            if(customMenu.shouldDeactivate())
                customMenu.deactivate();
            else
                customMenu.activate();
    }

    private void populateMembers() {
        MemberAdapter memberAdapter = new MemberAdapter(this, getMembers(), household.getSelectedMemberId(), household, new MemberAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, Member member) {
                HouseholdActivityFactory.getMemberItemHandler(HouseholdActivity.this, member).open();
            }
        });
        list.setAdapter(memberAdapter);
        new SelectedMemberViewWrapper().populate(household,household.getSelectedMember(db), this);
    }

    private List<Member> getMembers() {
        InterviewStatus status = household.getStatus();
        if(status.equals(NOT_DONE) || status.equals(DEFERRED) || status.equals(INCOMPLETE) || status.equals(CANCEL_SELECTION))
            return household.getAllUnselectedMembers(db);
        return household.getAllNonDeletedMembers(db);
    }


}
