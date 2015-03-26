package com.onaio.steps.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Factory.HouseholdActivityFactory;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuResultHandler;
import com.onaio.steps.activityHandler.Interface.IPrepare;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.adapter.MemberAdapter;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

import java.util.List;

public class HouseholdActivity extends ListActivity {

    private Household household;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.household);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        household = (Household)intent.getSerializableExtra(Constants.HOUSEHOLD);
        populatePhoneNumber();
        handleMembers();
        prepareBottomMenuItems();
        setTitle(household.getName());
    }

    private void handleMembers() {
        populateMembers();
        bindMemberItems();
    }

    private void prepareBottomMenuItems() {
        List<IPrepare> bottomMenus = HouseholdActivityFactory.getBottomMenuPreparer(this, household);
        for(IPrepare menu:bottomMenus)
            if(menu.shouldInactivate())
                menu.inactivate();


    }

    private void bindMemberItems() {
        ListView members = getListView();
        members.setOnItemClickListener(memberItemListener);
    }

    private AdapterView.OnItemClickListener memberItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Member member = Member.find_by(db, id, household);
            HouseholdActivityFactory.getMemberItemHandler(HouseholdActivity.this, member).open();
        }
    };

    private void populatePhoneNumber() {
        TextView phoneNumber = (TextView) findViewById(R.id.household_number);
        phoneNumber.setText(String.valueOf(household.getPhoneNumber()));
    }

    private void populateMembers() {
        db = new DatabaseHelper(getApplicationContext());
        MemberAdapter memberAdapter = new MemberAdapter(this, Member.getAll(db, household), household.getSelectedMember());
        getListView().setAdapter(memberAdapter);
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
        IPrepare menuItem = HouseholdActivityFactory.getMenuPreparer(this, household, menu);
        if(menuItem.shouldInactivate())
            menuItem.inactivate();
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
        List<IMenuResultHandler> menuHandlers = HouseholdActivityFactory.getMenuResultHandlers(this, household);
        for(IMenuResultHandler menuHandler:menuHandlers)
            if(menuHandler.canHandleResult(requestCode))
                menuHandler.handleResult(data, resultCode);
    }

    public void handleBottomMenu(View view) {
        List<IMenuHandler> bottomMenuItem = HouseholdActivityFactory.getBottomMenuHandler(this, household);
        for(IMenuHandler menuItem: bottomMenuItem)
            if(menuItem.shouldOpen(view.getId()))
                menuItem.open();
    }
}
