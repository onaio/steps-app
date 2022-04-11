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

import static org.junit.Assert.assertEquals;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.handler.strategies.survey.NotReachableSurveyForEmptyHouseholdStrategy;
import com.onaio.steps.model.Household;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class NotReachableOnEmptyHouseholdHandlerTest extends StepsTestRunner {

    private NotReachableOnEmptyHouseholdHandler notReachableHandler;
    private static final int MENU_ID = R.id.action_not_reachable_empty_hh;

    @Before
    public void setup(){
        HouseholdActivity householdActivityMock = Mockito.mock(HouseholdActivity.class);
        Household householdMock = Mockito.mock(Household.class);
        notReachableHandler = new NotReachableOnEmptyHouseholdHandler(householdActivityMock, new NotReachableSurveyForEmptyHouseholdStrategy(householdMock, householdActivityMock));
    }

    @Test
    public void ShouldReturnExpectedViewId() {
        assertEquals(MENU_ID, notReachableHandler.getViewId());
    }
}