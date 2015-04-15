package com.onaio.steps.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
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

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.Factory.StepsActivityFactory;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IResultHandler;
import com.onaio.steps.adapter.HouseholdAdapter;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Household;

import java.util.List;

import static com.onaio.steps.helper.Constants.HEADER_GREEN;
import static com.onaio.steps.helper.Constants.PHONE_ID;

public class StepsActivity extends ListActivity {

    private DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        prepareScreen();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        prepareScreen();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<IMenuHandler> activityHandlers = StepsActivityFactory.getMenuHandlers(this);
        for(IMenuHandler handler : activityHandlers){
            if(handler.shouldOpen(item.getItemId()))
                return handler.open();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<IResultHandler> activityHandlers = StepsActivityFactory.getResultHandlers(this);
        for(IResultHandler activityHandler: activityHandlers){
            if(activityHandler.canHandleResult(requestCode))
                activityHandler.handleResult(data,resultCode);
        }
    }

    public void prepareScreen() {
        setLayout();
        populateHouseholds();
        bindHouseholdItems();
        setWatermark();
    }

    private void setWatermark() {
        final RelativeLayout homePage = (RelativeLayout)findViewById(R.id.main_layout);
        if(homePage == null) return;
        homePage.post(new Runnable() {
            public void run() {
                Window win = getWindow();
                View contentView = win.findViewById(Window.ID_ANDROID_CONTENT);
                ImageView imageView = (ImageView) findViewById(R.id.item_image);
                imageView.getLayoutParams().height = contentView.getHeight() / 2;
                imageView.getLayoutParams().width = contentView.getWidth() / 2;
            }
        });
    }

    private void setLayout() {
        if(getValue(PHONE_ID) == null || getValue(PHONE_ID).equals(""))
            setContentView(R.layout.first_main);
        else
            setContentView(R.layout.main);
        setTitle(R.string.main_header);
        setTitleColor(Color.parseColor(HEADER_GREEN));
    }

    private void bindHouseholdItems() {
        ListView households = getListView();
        households.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Household household = Household.find_by(db, id);
                StepsActivityFactory.getHouseholdItemHandler(StepsActivity.this, household).open();
            }
        });
    }

    private void populateHouseholds() {
        db = new DatabaseHelper(getApplicationContext());
        getListView().setAdapter(new HouseholdAdapter(this, Household.getAll(db)));
    }

    private String getValue(String key) {
        return KeyValueStoreFactory.instance(this).getString(key) ;
    }

    public void handleCustomMenu(View view){
        List<IMenuHandler> handlers = StepsActivityFactory.getCustomMenuHandler(this);
        for(IMenuHandler handler:handlers)
            if(handler.shouldOpen(view.getId()))
                handler.open();
    }
}
