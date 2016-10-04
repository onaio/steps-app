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

package com.onaio.steps.handler.activities;

import android.app.ListActivity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.activities.NewHouseholdActivity;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.adapters.HouseholdAdapter;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.RequestCode;

import static android.app.Activity.RESULT_OK;

public class NewHouseholdActivityHandler implements IMenuHandler, IActivityResultHandler {

    private ListActivity activity;
    private CustomDialog dialog = new CustomDialog();

    public NewHouseholdActivityHandler(ListActivity activity) {
        this(activity,new CustomDialog());
    }

    NewHouseholdActivityHandler(ListActivity activity, CustomDialog dialog) {
        this.activity = activity;
        this.dialog = dialog;
    }

    @Override
    public boolean shouldOpen(int menu_id) {
        return menu_id == R.id.action_add_new_item;
    }

    @Override
    public boolean open() {
        if (isEmpty(getValue(Constants.HH_PHONE_ID))) {
            notifyUserToSetPhoneId(activity);
        } else {
            Intent intent = new Intent(activity.getBaseContext(), NewHouseholdActivity.class);
            intent.putExtra(Constants.HH_PHONE_ID, getValue(Constants.HH_PHONE_ID));
            intent.putExtra(Constants.HH_HOUSEHOLD_SEED, getValue(Constants.HH_HOUSEHOLD_SEED));
            activity.startActivityForResult(intent, RequestCode.NEW_HOUSEHOLD.getCode());
        }
        return true;
    }

    @Override
    public void handleResult(Intent data, int resultCode) {
        if (resultCode == RESULT_OK) {
            HouseholdAdapter householdAdapter = (HouseholdAdapter) activity.getListView().getAdapter();
            if (householdAdapter == null)
                return;
            householdAdapter.reinitialize(Household.getAllInOrder(new DatabaseHelper(activity.getApplicationContext())));
            householdAdapter.notifyDataSetChanged();

            Intent householdActivityIntent = new Intent(activity, HouseholdActivity.class);
            householdActivityIntent.putExtra(Constants.HH_HOUSEHOLD,data.getSerializableExtra(Constants.HH_HOUSEHOLD));
            activity.startActivity(householdActivityIntent);
        }
    }

    @Override
    public boolean canHandleResult(int requestCode) {
        return requestCode == RequestCode.NEW_HOUSEHOLD.getCode();
    }

    private void notifyUserToSetPhoneId(ListActivity activity) {
        dialog.notify(activity, CustomDialog.EmptyListener, R.string.phone_id_message_title, R.string.phone_id_message);
    }

    private String getValue(String key) {
        return KeyValueStoreFactory.instance(activity).getString(key);
    }

    private boolean isEmpty(String value) {
        return value == null || value.equals("");
    }
}
