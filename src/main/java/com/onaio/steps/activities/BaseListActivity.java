package com.onaio.steps.activities;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.onaio.steps.R;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.helper.DatabaseHelper;

import java.util.List;

public abstract class BaseListActivity extends ListActivity{
    protected DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        db = new DatabaseHelper(this);
        prepareScreen();
        setWatermark();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        prepareScreen();
        setWatermark();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(getMenuViewLayout(), menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<IMenuHandler> activityHandlers = getMenuHandlers();
        for(IMenuHandler handler : activityHandlers){
            if(handler.shouldOpen(item.getItemId()) )
                return handler.open();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<IActivityResultHandler> activityHandlers = getResultHandlers();
        for(IActivityResultHandler activityHandler: activityHandlers){
            if(activityHandler.canHandleResult(requestCode))
                activityHandler.handleResult(data,resultCode);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        List<IMenuPreparer> menuItemHandlers = getMenuPreparer(menu);
        for(IMenuPreparer handler:menuItemHandlers)
            if(handler.shouldInactivate())
                handler.inactivate();
            else
                handler.activate();
        super.onPrepareOptionsMenu(menu);
        return true;
    }



    private void setWatermark() {
        final RelativeLayout homePage = (RelativeLayout)findViewById(R.id.main_layout);
        if(homePage == null) return;
        runOnUiThread(new Runnable() {
            public void run() {
                Window win = getWindow();
                View contentView = win.findViewById(Window.ID_ANDROID_CONTENT);
                ImageView imageView = (ImageView) findViewById(R.id.item_image);
                int height = contentView.getHeight() / 2;
                int width = contentView.getWidth() / 2;
                imageView.getLayoutParams().height = height == 0 ? 300 : height;
                imageView.getLayoutParams().width = width == 0 ? 300 : width;
            }
        });
    }

    public void handleCustomMenu(View view){
        List<IMenuHandler> handlers = getCustomMenuHandler();
        for(IMenuHandler handler:handlers)
            if(handler.shouldOpen(view.getId()))
                handler.open();
    }

    protected abstract int getMenuViewLayout();
    protected abstract List<IMenuHandler> getMenuHandlers();
    protected abstract List<IActivityResultHandler> getResultHandlers();
    protected abstract List<IMenuPreparer> getMenuPreparer(Menu menu);
    protected abstract List<IMenuHandler> getCustomMenuHandler();
    protected abstract void prepareScreen();

}
