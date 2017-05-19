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

import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.onaio.steps.R;
import com.onaio.steps.adapters.HouseholdAdapter;
import com.onaio.steps.handler.factories.HouseholdListActivityFactory;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.handler.interfaces.IViewPreparer;
import com.onaio.steps.model.Household;

import java.util.List;

import static com.onaio.steps.helper.Constants.HEADER_GREEN;

public class HouseholdListActivity extends BaseListActivity {

    @Override
    protected void prepareScreen() {
        setLayout();
        populateHouseholds();
        bindHouseholdItem();
    }

    protected void setLayout() {
        setContentView(R.layout.main);
        setTitle(R.string.main_header);
        Button householdButton = (Button) findViewById(R.id.action_add_new_item);
        householdButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_new_household, 0, 0, 0);
        Button submitDataButton = (Button) findViewById(R.id.action_submit_data);
        submitDataButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cloud_upload_white_24dp, 0, 0, 0);
        List<IViewPreparer> viewPreparers = getViewPreparer();
        for(IViewPreparer curPreparer : viewPreparers) {
            if(curPreparer.shouldBeDisabled()) {
                curPreparer.disable();
            } else {
                curPreparer.enable();
            }
        }
        setTitleColor(Color.parseColor(HEADER_GREEN));
    }

    protected void populateHouseholds() {
        List<Household> households = Household.getAllInOrder(db);
        getListView().setAdapter(new HouseholdAdapter(this, households));
    }

    protected void bindHouseholdItem() {
        ListView households = getListView();
        households.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Household household = Household.find_by(db, id);
                HouseholdListActivityFactory.getHouseholdItemHandler(HouseholdListActivity.this, household).open();
            }
        });
    }

    @Override
    protected int getMenuViewLayout() {
        return R.menu.main_activity_actions;
    }

    @Override
    protected List<IMenuHandler> getMenuHandlers() {
        return HouseholdListActivityFactory.getMenuHandlers(this, Household.getAllInOrder(db));
    }

    @Override
    protected List<IActivityResultHandler> getResultHandlers() {
        return HouseholdListActivityFactory.getResultHandlers(this);
    }

    @Override
    protected List<IMenuPreparer> getMenuPreparer(Menu menu) {
        return HouseholdListActivityFactory.getMenuPreparer(this, Household.getAllInOrder(db), menu);
    }

    @Override
    protected List<IMenuHandler> getCustomMenuHandler() {
        return HouseholdListActivityFactory.getCustomMenuHandler(this, Household.getAllInOrder(db));
    }

    private List<IViewPreparer> getViewPreparer() {
        return HouseholdListActivityFactory.getViewPreparer(this, Household.getAllInOrder(db));
    }
}
