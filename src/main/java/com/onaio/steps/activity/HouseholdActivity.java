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
import android.widget.RelativeLayout;
import android.widget.TextView;

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
        setWatermark();
        Intent intent = getIntent();
        household = (Household)intent.getSerializableExtra(Constants.HOUSEHOLD);
        styleActionBar();
        handleMembers();
        prepareCustomMenu();
    }

    private void styleActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setIcon(R.drawable.ic_action_back);
        actionBar.setSubtitle(household.getPhoneNumber());
        actionBar.setTitle(household.getName());
    }

    private void handleMembers() {
        populateMembers();
        bindMemberItems();
        populateSelectedMember();
    }

    private void populateSelectedMember() {
        if(household.getSelectedMemberId()==null)
            return;
        TextView nameView = (TextView) findViewById(R.id.selected_participant_name);
        TextView detailView = (TextView) findViewById(R.id.selected_participant_details);
        Member selectedMember = household.getSelectedMember(db);
        nameView.setText(selectedMember.getFormattedName());
        detailView.setText(selectedMember.getFormattedDetail());
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
        MemberAdapter memberAdapter = new MemberAdapter(this, household.getAllUnselectedMembers(db));
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


    private void setWatermark() {
        final RelativeLayout root = (RelativeLayout)findViewById(R.id.household_main);
        root.post(new Runnable() {
            public void run() {
                Window win = getWindow();
                View contentView = win.findViewById(Window.ID_ANDROID_CONTENT);
                ImageView imageView = (ImageView) findViewById(R.id.item_image);
                imageView.getLayoutParams().height = contentView.getHeight() / 2;
                imageView.getLayoutParams().width = contentView.getWidth() / 2;
            }
        });
    }
}
