package com.onaio.steps.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.activityHandler.ActivityHandlerFactory;
import com.onaio.steps.activityHandler.IActivityHandler;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;

import java.util.List;

public class HouseholdActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.household);
        Intent intent = getIntent();
        String household_number = intent.getStringExtra("HOUSEHOLD_NUMBER");
        TextView phoneNumber = (TextView) findViewById(R.id.household_number);
        phoneNumber.setText(household_number);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<IActivityHandler> menuHandlers = ActivityHandlerFactory.getHouseholdMenuHandlers();
        for(IActivityHandler menuHandler:menuHandlers)
            if(menuHandler.shouldOpen(item.getItemId()))
                menuHandler.open(this);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
