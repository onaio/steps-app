package com.onaio.steps.activity;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Factory.HouseholdActivityFactory;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IPrepare;
import com.onaio.steps.activityHandler.Interface.IResultHandler;
import com.onaio.steps.adapter.MemberAdapter;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
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
        Intent intent = getIntent();
        household = (Household)intent.getSerializableExtra(Constants.HOUSEHOLD);
        styleActionBar();
        handleMembers();
        prepareCustomMenu();
    }

    private void styleActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setIcon(R.drawable.ic_action_back);
        actionBar.setSubtitle(household.getPhoneNumber());
        actionBar.setTitle(household.getName());
    }

    private void handleMembers() {
        populateMembers();
        bindMemberItems();
    }

    private void prepareCustomMenu() {
        List<IPrepare> bottomMenus = HouseholdActivityFactory.getCustomMenuPreparer(this, household);
        for(IPrepare menu:bottomMenus)
            if(menu.shouldInactivate())
                menu.inactivate();
            else
                menu.activate();
    }

    private void bindMemberItems() {
        ListView members = getListView();
        members.setOnItemClickListener(memberItemListener);
    }

    private AdapterView.OnItemClickListener memberItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Member member = household.findMember(db, id);
            HouseholdActivityFactory.getMemberItemHandler(HouseholdActivity.this, member).open();
        }
    };

    private void populateMembers() {
        db = new DatabaseHelper(this);
        MemberAdapter memberAdapter = new MemberAdapter(this, household.getAllMembers(db), household.getSelectedMemberId());
        getListView().setAdapter(memberAdapter);
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
        List<IPrepare> menuPreparers = HouseholdActivityFactory.getMenuPreparer(this, household, menu);
        for (IPrepare menuPreparer: menuPreparers)
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
        List<IResultHandler> menuHandlers = HouseholdActivityFactory.getMenuResultHandlers(this, household);
        for(IResultHandler menuHandler:menuHandlers)
            if(menuHandler.canHandleResult(requestCode))
                menuHandler.handleResult(data, resultCode);
    }

    public void handleCustomMenu(View view) {
        List<IMenuHandler> bottomMenuItem = HouseholdActivityFactory.getCustomMenuHandler(this, household);
        for(IMenuHandler menuItem: bottomMenuItem)
            if(menuItem.shouldOpen(view.getId()))
                menuItem.open();
    }


    public void onWindowFocusChanged(boolean hasFocus) {
        View content = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        ImageView who_watermark = (ImageView) findViewById(R.id.item_image);
        who_watermark.getLayoutParams().height = (content.getHeight())/2;
        who_watermark.getLayoutParams().width=content.getWidth()/2;
        super.onWindowFocusChanged(hasFocus);
    }
}
