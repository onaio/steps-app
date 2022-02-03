/*
 * Copyright 2016. World Health Organization
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onaio.steps.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.handler.actions.ImportHandler;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;

import java.util.List;

public abstract class BaseListActivity extends AppCompatActivity {
    protected DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        db = new DatabaseHelper(this);
        prepareScreen();
        setWatermark();
        super.onCreate(savedInstanceState);
        setActionbarStyle();
    }

    @Override
    protected void onResume() {
        prepareScreen();
        setWatermark();
        super.onResume();
        setActionbarStyle();
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
        for(final IMenuHandler handler : activityHandlers){
            if(handler.shouldOpen(item.getItemId()) ) {
                if (handler instanceof ImportHandler) {
                    DialogInterface.OnClickListener confirmListener = (dialog, which) -> handler.open();
                    new CustomDialog().confirm(this, confirmListener, CustomDialog.EmptyListener, R.string.warning_merging_data, R.string.warning_title);
                } else {
                    return handler.open();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
            if(handler.shouldDeactivate())
                handler.deactivate();
            else
                handler.activate();
        super.onPrepareOptionsMenu(menu);
        return true;
    }



    private void setWatermark() {
        final RelativeLayout homePage = findViewById(R.id.main_layout);
        if(homePage == null) return;
        runOnUiThread(() -> {
            ImageView imageView = findViewById(R.id.item_image);
            imageView.getLayoutParams().height = (int) (getResources().getDimension(R.dimen.watermark_height)
                    / getResources().getDisplayMetrics().density);
            imageView.getLayoutParams().width = (int) (getResources().getDimension(R.dimen.watermark_width)
                    / getResources().getDisplayMetrics().density);
        });
    }

    public void handleCustomMenu(View view){
        List<IMenuHandler> handlers = getCustomMenuHandler();
        for(IMenuHandler handler:handlers)
            if(handler.shouldOpen(view.getId()))
                handler.open();
    }

    public void setActionbarStyle() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    protected abstract int getMenuViewLayout();
    protected abstract List<IMenuHandler> getMenuHandlers();
    protected abstract List<IActivityResultHandler> getResultHandlers();
    protected abstract List<IMenuPreparer> getMenuPreparer(Menu menu);
    protected abstract List<IMenuHandler> getCustomMenuHandler();
    protected abstract void prepareScreen();
    public abstract void refreshList();

}
