package com.onaio.steps.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.ActivityHandlerFactory;
import com.onaio.steps.activityHandler.IActivityHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.Member;

import java.util.ArrayList;
import java.util.List;

public class HouseholdActivity extends ListActivity {

    private Household household;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.household);
        populatePhoneNumber();
        populateMembers();
        bindMemberItems();
        setTitle(household.getName());
    }

    private void bindMemberItems() {
        ListView members = getListView();
        members.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String memberName = ((TextView) view).getText().toString();
                Member member = Member.find_by(db, memberName, household);
                ActivityHandlerFactory.getMemberItemHandler(HouseholdActivity.this, member).open();
            }
        });
    }

    private void populatePhoneNumber() {
        Intent intent = getIntent();
        household = (Household)intent.getSerializableExtra(Constants.HOUSEHOLD);
        TextView phoneNumber = (TextView) findViewById(R.id.household_number);
        phoneNumber.setText(String.valueOf(household.getPhoneNumber()));
    }



    private void populateMembers() {
        db = new DatabaseHelper(getApplicationContext());
        getListView().setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fetchMembers()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<IActivityHandler> menuHandlers = ActivityHandlerFactory.getHouseholdMenuHandlers(this,household);
        for(IActivityHandler menuHandler:menuHandlers)
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
        List<IActivityHandler> menuHandlers = ActivityHandlerFactory.getHouseholdMenuHandlers(this,household);
        for(IActivityHandler menuHandler:menuHandlers)
            if(menuHandler.canHandleResult(requestCode))
                menuHandler.handleResult(data, resultCode);
    }

    private List<String> fetchMembers() {
        List<Member> members = Member.getAll(db,household);
        List<String> memberNames = new ArrayList<String>();
        for(Member member: members)
            memberNames.add(member.getName());
        return memberNames;
    }
}
