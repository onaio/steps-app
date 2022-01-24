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

package com.onaio.steps.handler.actions;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.adapters.MemberAdapter;
import com.onaio.steps.handler.factories.HouseholdActivityFactory;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.ReElectReason;

import java.util.List;

import static com.onaio.steps.model.InterviewStatus.CANCEL_SELECTION;
import static com.onaio.steps.model.InterviewStatus.SELECTION_NOT_DONE;

public class CancelParticipantSelectionHandler implements IMenuPreparer,IMenuHandler {

    private final int MENU_ID = R.id.action_cancel_participant;
    private Household household;
    private ListActivity activity;
    private DatabaseHelper databaseHelper;

    public CancelParticipantSelectionHandler(ListActivity activity, Household household) {
        this.activity=activity;
        this.household=household;
        this.databaseHelper=new DatabaseHelper(activity);
    }

    @Override
    public boolean shouldDeactivate() {
        boolean selectedStatus = household.getStatus() == InterviewStatus.NOT_DONE;
        return !(selectedStatus);
    }

    @Override
    public void deactivate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.GONE);
    }

    @Override
    public void activate() {
        View item = activity.findViewById(MENU_ID);
        item.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id==MENU_ID;
    }

    @Override
    public boolean open() {
        final ListView membersView = activity.getListView();
        final View confirmation = getView();
        CustomDialog customDialog = new CustomDialog();

        DialogInterface.OnClickListener confirmationDialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveReason(confirmation);
                updateHousehold();
                updateView(membersView);

            }
        };
        customDialog.confirm(activity, confirmationDialogListener, CustomDialog.EmptyListener, confirmation, R.string.confirm_ok);
        return true;
    }

    private void prepareCustomMenus() {
        List<IMenuPreparer> bottomMenus = HouseholdActivityFactory.getCustomMenuPreparer(activity, household);
        for(IMenuPreparer menu:bottomMenus)
            if(menu.shouldDeactivate())
                menu.deactivate();
            else
                menu.activate();
    }

    private void updateView(ListView membersView) {
        MemberAdapter membersAdapter = (MemberAdapter) membersView.getAdapter();
        membersAdapter.reinitialize(household.getAllNonDeletedMembers(databaseHelper),null);
        membersAdapter.notifyDataSetChanged();
        prepareCustomMenus();
    }

    private void updateHousehold() {
        household.setSelectedMemberId(null);
        household.setStatus(CANCEL_SELECTION);
        household.update(new DatabaseHelper(activity));
    }

    private void saveReason(View confirmation) {
        TextView reasonView = (TextView) confirmation.findViewById(R.id.reason);
        ReElectReason reason = new ReElectReason(reasonView.getText().toString(), household);
        reason.save(databaseHelper);
    }

    protected View getView() {
        LayoutInflater factory = LayoutInflater.from(activity);
        return factory.inflate(R.layout.selection_confirm, null);
    }
}
