package com.onaio.steps.activity;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Factory.HouseholdActivityFactory;
import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuPreparer;
import com.onaio.steps.adapter.MemberAdapter;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.Member;
import com.onaio.steps.modelViewWrapper.SelectedMemberViewWrapper;

import java.util.List;

import static com.onaio.steps.model.HouseholdStatus.*;

public class HouseholdActivity extends ListActivity {

    private Household household;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.household);
        household = (Household) getIntent().getSerializableExtra(Constants.HOUSEHOLD);
        db = new DatabaseHelper(this);
        styleActionBar();
        handleMembers();
        prepareCustomMenu();
        populateMessage();
    }

    private void populateMessage() {
        TextView viewById = (TextView) findViewById(R.id.survey_message);
        switch (household.getStatus()){
            case DONE: viewById.setText(R.string.survey_done_message);
                viewById.setTextColor(Color.parseColor(Constants.TEXT_GREEN));
                break;
            case REFUSED: viewById.setText(R.string.survey_refused_message);
                viewById.setTextColor(Color.RED);
                break;
            default: viewById.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        prepareCustomMenu();
        populateMembers();
        super.onResume();
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        List<IMenuPreparer> menuPreparers = HouseholdActivityFactory.getMenuPreparer(this, household, menu);
        for (IMenuPreparer menuPreparer: menuPreparers)
            if(menuPreparer.shouldInactivate())
                menuPreparer.inactivate();
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.household_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<IActivityResultHandler> menuHandlers = HouseholdActivityFactory.getMenuResultHandlers(this, household);
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
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setIcon(R.drawable.ic_action_back);
        TextView idHeader = (TextView) findViewById(R.id.household_id_header);
        TextView numberHeader = (TextView) findViewById(R.id.household_number_header);
        idHeader.setText(String.format("ID-%s",household.getName()));
        idHeader.setTextColor(Color.parseColor(Constants.HEADER_GREEN));
        numberHeader.setText(String.format("Phone Number: %s", household.getPhoneNumber()));
        actionBar.setTitle("");
    }

    private void handleMembers() {
        populateMembers();
        bindMembers();
    }

    private void bindMembers() {
        AdapterView.OnItemClickListener memberItemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Member member = household.findMember(db, id);
                HouseholdActivityFactory.getMemberItemHandler(HouseholdActivity.this, member).open();
            }
        };
        getListView().setOnItemClickListener(memberItemListener);
    }

    private void prepareCustomMenu() {
        List<IMenuPreparer> customMenus = HouseholdActivityFactory.getCustomMenuPreparer(this, household);
        for(IMenuPreparer customMenu:customMenus)
            if(customMenu.shouldInactivate())
                customMenu.inactivate();
            else
                customMenu.activate();
    }

    private void populateMembers() {
        MemberAdapter memberAdapter = new MemberAdapter(this, getMembers(),household.getSelectedMemberId());
        getListView().setAdapter(memberAdapter);
        new SelectedMemberViewWrapper().populate(household,household.getSelectedMember(db), this);
    }

    private List<Member> getMembers() {
        HouseholdStatus status = household.getStatus();
        if(status.equals(NOT_DONE) || status.equals(DEFERRED))
            return household.getAllUnselectedMembers(db);
        return household.getAllNonDeletedMembers(db);
    }
}
