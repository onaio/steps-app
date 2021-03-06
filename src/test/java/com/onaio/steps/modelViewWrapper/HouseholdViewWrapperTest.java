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

import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.EditText;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.activities.NewHouseholdActivity;
import com.onaio.steps.exceptions.InvalidDataException;
import com.onaio.steps.exceptions.NoUniqueIdException;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class HouseholdViewWrapperTest {

    private NewHouseholdActivity activity;
    private String currentDate;

    @Before
    public void Setup(){
        NewHouseholdActivity activity = Robolectric.setupActivity(NewHouseholdActivity.class);
        this.activity = Mockito.spy(activity);
        currentDate = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH).format(new Date());

    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void ShouldGiveHouseholdWhenPhoneNumberAndCommentsAreEmpty() throws InvalidDataException, NoUniqueIdException {
        TelephonyManager telephonyManager = Mockito.mock(TelephonyManager.class);
        Mockito.when(telephonyManager.getDeviceId()).thenReturn("testUniqueDevId");
        Mockito.when(activity.getSystemService(Context.TELEPHONY_SERVICE)).thenReturn(telephonyManager);
        HouseholdViewWrapper householdViewWrapper = new HouseholdViewWrapper(activity);
        TextView nameView = ((TextView) activity.findViewById(R.id.generated_household_id));
        nameView.setText("new name");
        TextView numberView = (TextView) activity.findViewById(R.id.household_number);
        numberView.setText("");
        EditText commentsView = (EditText) activity.findViewById(R.id.household_comments);
        commentsView.setText("");
        Household household = householdViewWrapper.getHousehold(R.id.generated_household_id, R.id.household_number,R.id.household_comments);
        assertTrue(household.getName().equals("new name"));
        assertTrue(household.getStatus().equals(InterviewStatus.SELECTION_NOT_DONE));
    }

    @Test
    public void ShouldGiveHousehold() throws InvalidDataException, NoUniqueIdException {
        TelephonyManager telephonyManager = Mockito.mock(TelephonyManager.class);
        Mockito.when(telephonyManager.getDeviceId()).thenReturn("testUniqueDevId");
        Mockito.when(activity.getSystemService(Context.TELEPHONY_SERVICE)).thenReturn(telephonyManager);
        HouseholdViewWrapper householdViewWrapper = new HouseholdViewWrapper(activity);
        TextView nameView = ((TextView) activity.findViewById(R.id.generated_household_id));
        nameView.setText("new name");
        TextView numberView = (TextView) activity.findViewById(R.id.household_number);
        numberView.setText("123456789");
        EditText commentsView = (EditText) activity.findViewById(R.id.household_comments);
        commentsView.setText("Dummy Comments");
        Household household = householdViewWrapper.getHousehold(R.id.generated_household_id, R.id.household_number,R.id.household_comments);
        assertTrue(household.getName().equals("new name"));
        assertTrue(household.getPhoneNumber().equals("123456789"));
        assertEquals("Dummy Comments", household.getComments());
        assertTrue(household.getStatus().equals(InterviewStatus.SELECTION_NOT_DONE));

    }

    @Test
    public void ShouldUpdateHouseholdAndShouldNotUpdateGeneratedId() throws InvalidDataException {
        Household anotherHousehold = new Household("5", "1234-10", "80503456", "", InterviewStatus.NOT_DONE, currentDate, "uniqueDevId", "Some Comments");
        HouseholdViewWrapper householdViewWrapper = new HouseholdViewWrapper(activity);
        TextView nameView = ((TextView) activity.findViewById(R.id.generated_household_id));
        TextView numberView = (TextView) activity.findViewById(R.id.household_number);
        EditText commentsView = (EditText) activity.findViewById(R.id.household_comments);
        numberView.setText("123456789");
        nameView.setText("123-20");
        commentsView.setText("Dummy Comments");

        Household household = householdViewWrapper.updateHousehold(anotherHousehold, R.id.household_number,R.id.household_comments);

       assertEquals("1234-10",household.getName());
        assertEquals("123456789",household.getPhoneNumber());
        assertEquals("Dummy Comments",household.getComments());
        assertTrue(household.getStatus().equals(InterviewStatus.NOT_DONE));

    }

}