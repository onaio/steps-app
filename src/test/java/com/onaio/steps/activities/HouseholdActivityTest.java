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


import static org.junit.Assert.assertEquals;

import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.onaio.steps.R;
import com.onaio.steps.adapters.HouseholdAdapter;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;

@Config(emulateSdk = 16, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class HouseholdActivityTest {

    private Household household;
    private Member member2;
    private Member member1;
    private Intent intent;
    private ActivityController<HouseholdActivity> householdActivityController;

    @Before
    public void setup() {
        intent = new Intent();
        household = Mockito.mock(Household.class);
        member1 = new Member(101, "raj", "Nik", Gender.Male, 19, household, "100", false);
        member2 = new Member(102, "rana", "Sandhya", Gender.Female, 22, household, "100", false);
        ArrayList<Member> members = new ArrayList<Member>();
        members.add(member1);
        members.add(member2);
        Mockito.stub(household.getName()).toReturn("123-100");
        Mockito.stub(household.getPhoneNumber()).toReturn("1234567");
        Mockito.stub(household.getAllNonDeletedMembers(Mockito.any(DatabaseHelper.class))).toReturn(members);
        householdActivityController = Robolectric.buildActivity(HouseholdActivity.class);
    }

    @Test
    public void ShouldStyleActionBar() {
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.DONE);
        Mockito.stub(household.getComments()).toReturn("dummy comments");
        intent.putExtra(Constants.HH_HOUSEHOLD,household);
        HouseholdActivity householdActivity = householdActivityController.withIntent(intent).create().get();

        TextView idHeader = (TextView) householdActivity.findViewById(R.id.household_id_header);
        TextView numberHeader = (TextView) householdActivity.findViewById(R.id.household_number_header);
        TextView commentHeader = (TextView) householdActivity.findViewById(R.id.text_view_comment);
        assertEquals("Household ID: 123-100", idHeader.getText().toString());
        assertEquals("Phone Number: 1234567", numberHeader.getText().toString());
        assertEquals("dummy comments" ,commentHeader.getText().toString());
        assertEquals(Color.parseColor(Constants.HEADER_GREEN), idHeader.getCurrentTextColor());
    }

    @Test
    public void ShouldPopulateAdapterWithMembersAndDisplayAppropriateMessage() {
        Mockito.stub(household.getComments()).toReturn("dummy comments");
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.DONE);
        intent.putExtra(Constants.HH_HOUSEHOLD,household);
        HouseholdActivity householdActivity = householdActivityController.withIntent(intent).create().get();
        RecyclerView list = householdActivity.findViewById(R.id.list);

        HouseholdAdapter listAdapter = (HouseholdAdapter) list.getAdapter();
        assertEquals(2, listAdapter.getItemCount());
        assertEquals(member1, listAdapter.getItem(0));
        assertEquals(member2, listAdapter.getItem(1));
        TextView viewById = (TextView) householdActivity.findViewById(R.id.survey_message);
        assertEquals(householdActivity.getString(R.string.survey_done_message), viewById.getText().toString());
    }

    @Test
    public void ShouldPopulateAdapterWithMembersAndDisplaySurveyRefusedMessage() {
        Mockito.stub(household.getComments()).toReturn("dummy comments");
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.REFUSED);
        intent.putExtra(Constants.HH_HOUSEHOLD,household);
        HouseholdActivity householdActivity = householdActivityController.withIntent(intent).create().get();
        RecyclerView list = householdActivity.findViewById(R.id.list);

        HouseholdAdapter listAdapter = (HouseholdAdapter) list.getAdapter();
        assertEquals(2, listAdapter.getItemCount());
        assertEquals(member1, listAdapter.getItem(0));
        assertEquals(member2, listAdapter.getItem(1));
        TextView viewById = (TextView) householdActivity.findViewById(R.id.survey_message);
        assertEquals(householdActivity.getString(R.string.survey_refused_message), viewById.getText().toString());
    }







}