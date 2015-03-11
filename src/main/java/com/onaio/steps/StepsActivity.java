package com.onaio.steps;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.onaio.steps.activityHandler.ActivityHandlerFactory;
import com.onaio.steps.activityHandler.IActivityHandler;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;

import java.util.ArrayList;
import java.util.List;

public class StepsActivity extends ListActivity {

    private DatabaseHelper db;
    public static final String PHONE_ID = "phoneId";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        db = new DatabaseHelper(getApplicationContext());
        getListView().setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fetchHouseholds()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<IActivityHandler> activityHandlers = ActivityHandlerFactory.getHandlers();
        for(IActivityHandler handler : activityHandlers){
            if(handler.shouldOpen(item.getItemId()))
                return handler.open(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<IActivityHandler> activityHandlers = ActivityHandlerFactory.getHandlers();
        for(IActivityHandler activityHandler: activityHandlers){
            if(activityHandler.canHandleResult(requestCode))
                activityHandler.handleResult(this,data,resultCode);
        }
    }

    private List<String> fetchHouseholds() {
        List<Household> households = db.getHouseholds();
        List<String> householdNames = new ArrayList<String>();
        for(Household household: households)
            householdNames.add(household.getName());
        return householdNames;
    }

}
