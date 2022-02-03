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

package com.onaio.steps.modelViewWrapper;

import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.exceptions.NoUniqueIdException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.Device;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.ServerStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HouseholdViewWrapper {
    private AppCompatActivity activity;

    public HouseholdViewWrapper(AppCompatActivity activity) {
        this.activity = activity;
    }

    public Household getHousehold(int nameViewId, int numberViewId, int commentsViewId) throws InvalidDataException, NoUniqueIdException {
        TextView nameView = (TextView) activity.findViewById(nameViewId);
        TextView numberView = (TextView) activity.findViewById(numberViewId);
        String phoneNumber = numberView.getText().toString();
        String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH).format(new Date());
        EditText commentsView = (EditText) activity.findViewById(commentsViewId);
        String comments = commentsView.getText().toString();
        String uniqueDeviceId = Device.getUniqueDeviceId(activity);
        Household household = new Household(nameView.getText().toString(), phoneNumber, InterviewStatus.EMPTY_HOUSEHOLD, currentDate, uniqueDeviceId,comments);
        household.setServerStatus(ServerStatus.NOT_SENT);
        return household;
    }

    public Household updateHousehold(Household household, int numberViewId, int commentsViewId) {
        TextView numberView = (TextView) activity.findViewById(numberViewId);
        String phoneNumber = numberView.getText().toString();
        TextView commentsView = (TextView) activity.findViewById(commentsViewId);
        String comments = commentsView.getText().toString();
        household.setPhoneNumber(phoneNumber);
        household.setComments(comments);
        return household;
    }


}
