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

package com.onaio.steps.adapter;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.HouseholdListActivity;
import com.onaio.steps.adapters.HouseholdAdapter;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

import java.util.ArrayList;
import java.util.List;

public class HouseholdAdapterTest extends StepsTestRunner {

    private final String ID = "1";
    private final String NAME = "Bill Gates";
    private final String PHONE_NUMBER = "12345";
    private final String SELECTED_MEMBER_ID = "4";
    private final String CREATED_AT = "";
    private final String UNIQUE_DEVICE_ID = "2";
    private final String COMMENTS = "no comments";
    private final InterviewStatus STATUS = InterviewStatus.DONE;

    private HouseholdAdapter adapter;
    private List<Household> householdList;

    @Before
    public void setUp() {
        householdList = new ArrayList<>();
        householdList.add(getHousehold());

        HouseholdListActivity householdListActivity = Mockito.spy(Robolectric.buildActivity(HouseholdListActivity.class).create().resume().get());
        adapter = new HouseholdAdapter(householdListActivity, householdList, null);
    }

    @Test
    public void testGetCountShouldReturnOne() {
        Assert.assertEquals(1, adapter.getItemCount());
    }

    @Test
    public void testGetItemShouldVerifyTheData() {
        Household household = adapter.getItem(0);

        Assert.assertEquals(ID, household.getId());
        Assert.assertEquals(NAME, household.getName());
        Assert.assertEquals(PHONE_NUMBER, household.getPhoneNumber());
        Assert.assertEquals(SELECTED_MEMBER_ID, household.getSelectedMemberId());
        Assert.assertEquals(CREATED_AT, household.getCreatedAt());
        Assert.assertEquals(UNIQUE_DEVICE_ID, household.getUniqueDeviceId());
        Assert.assertEquals(COMMENTS, household.getComments());
        Assert.assertEquals(STATUS, household.getStatus());
    }

    @Test
    public void testGetItemIdShouldReturnOneAsLong() {
        Assert.assertEquals(1, adapter.getItemId(0));
    }

    /*@Test
    public void testGetViewShouldVerifyViewData() {
        View itemView = adapter.getView(0, null, null);

        TextView householdName = (TextView) itemView.findViewById(R.id.main_text);

        Assert.assertEquals("HHID:" + NAME, householdName.getText().toString());
    }*/

    private Household getHousehold() {
        return new Household(ID, NAME, PHONE_NUMBER, SELECTED_MEMBER_ID, STATUS, CREATED_AT, UNIQUE_DEVICE_ID, COMMENTS);
    }
}
