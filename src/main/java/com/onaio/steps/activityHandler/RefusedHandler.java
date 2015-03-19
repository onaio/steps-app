package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.onaio.steps.R;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

public class RefusedHandler implements IHandler,IPrepare{

    private Household household;
    private ListActivity activity;
    private int MENU_ID = R.id.action_refused;

    public RefusedHandler(ListActivity activity, Household household) {
        this.activity = activity;
        this.household = household;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id==MENU_ID;
    }

    @Override
    public boolean open() {
        household.setStatus(HouseholdStatus.REFUSED);
        household.update(new DatabaseHelper(activity.getApplicationContext()));
        StepsActivityHandler handler = new StepsActivityHandler(activity);
        handler.open();
        return false;
    }

    @Override
    public void handleResult(Intent data, int resultCode) {

    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return false;
    }

    @Override
    public boolean shouldDisable(Household household) {
        return household.getSelectedMember()==null;
    }

    @Override
    public void disable(Menu menu) {
        MenuItem menuItem = menu.findItem(MENU_ID);
        menuItem.setEnabled(false);
    }
}
