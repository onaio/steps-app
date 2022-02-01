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


import android.text.Html;
import android.view.Menu;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.onaio.steps.R;
import com.onaio.steps.adapters.ParticipantAdapter;
import com.onaio.steps.handler.factories.ParticipantListActivityFactory;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.handler.interfaces.IViewPreparer;
import com.onaio.steps.model.Participant;

import java.util.ArrayList;
import java.util.List;

public class ParticipantListActivity extends BaseListActivity {

    private RecyclerView list;

    @Override
    protected void prepareScreen() {
        setLayout();
        populateParticipants();
    }

    protected void setLayout() {
        setContentView(R.layout.main);
        Button participantHeader = (Button) findViewById(R.id.action_add_new_item);
        participantHeader.setText(R.string.action_add_participant);
        participantHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_new_member, 0, 0, 0);
        Button submitDataButton = (Button) findViewById(R.id.action_submit_data);
        submitDataButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cloud_upload_white_24dp, 0, 0, 0);
        setTitle(getString(R.string.participant_header));
        list = findViewById(R.id.list);
    }

    protected void populateParticipants() {
        List<Participant> participants = Participant.getAllParticipants(db);

        list.setAdapter(new ParticipantAdapter(this, participants, new ParticipantAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, Participant participant) {
                ParticipantListActivityFactory.getParticipantItemHandler(ParticipantListActivity.this, participant).open();
            }
        }));
    }

    @Override
    protected int getMenuViewLayout() {
        return R.menu.participant_list_actions;
    }

    @Override
    protected List<IMenuHandler> getMenuHandlers() {
        return ParticipantListActivityFactory.getMenuHandlers(this);
    }

    @Override
    protected List<IActivityResultHandler> getResultHandlers() {
        return ParticipantListActivityFactory.getResultHandlers(this);
    }

    @Override
    protected List<IMenuPreparer> getMenuPreparer(Menu menu) {
        return new ArrayList<IMenuPreparer>();
    }

    public List<IViewPreparer> getViewPreparer() {
        return ParticipantListActivityFactory.getViewPreparer(this, Participant.getAllParticipants(db));
    }

    @Override
    protected List<IMenuHandler> getCustomMenuHandler() {
        return ParticipantListActivityFactory.getCustomMenuHandler(this);
    }

    @Override
    public void refreshList() {
        populateParticipants();
    }
}


