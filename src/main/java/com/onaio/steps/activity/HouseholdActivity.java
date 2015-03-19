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
import com.onaio.steps.activityHandler.ActivityHandlerFactory;
import com.onaio.steps.activityHandler.IHandler;
import com.onaio.steps.activityHandler.IPrepare;
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
        populatePhoneNumber();
        populateMembers();
        bindMemberItems();
        setTitle(household.getName());
    }

    private void bindMemberItems() {
        ListView members = getListView();
        members.setOnItemClickListener(memberItemListener);
    }

    private AdapterView.OnItemClickListener memberItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Member member = Member.find_by(db, id, household);
            ActivityHandlerFactory.getMemberItemHandler(HouseholdActivity.this, member).open();
        }
    };

    private void populatePhoneNumber() {
        Intent intent = getIntent();
        household = (Household)intent.getSerializableExtra(Constants.HOUSEHOLD);
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
        List<IHandler> menuHandlers = ActivityHandlerFactory.getHouseholdMenuHandlers(this,household);
        for(IHandler menuHandler:menuHandlers)
            if(menuHandler.shouldOpen(item.getItemId()))
                menuHandler.open();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        List<IPrepare> menuItemsToPrepare = ActivityHandlerFactory.getHouseholdMenuItemToPrepare(this,household);
        for(IPrepare menuItemToPrepare: menuItemsToPrepare)
            if(menuItemToPrepare.shouldDisable(household))
                menuItemToPrepare.disable(menu);
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
        List<IHandler> menuHandlers = ActivityHandlerFactory.getHouseholdMenuHandlers(this,household);
        for(IHandler menuHandler:menuHandlers)
            if(menuHandler.canHandleResult(requestCode))
                menuHandler.handleResult(data, resultCode);
    }
}
