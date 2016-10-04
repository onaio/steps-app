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

import com.onaio.steps.activities.HouseholdListActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class BackHomeHandlerTest {

    HouseholdListActivity householdListActivityMock;
    BackHomeHandler backHomeHandler;
    @Before
    public void setup(){
        householdListActivityMock = Mockito.mock(HouseholdListActivity.class);
        backHomeHandler = new BackHomeHandler(householdListActivityMock);
    }

    @Test
    public void ShouldCheckWhetherActivityCanBeStartedWhenProperIdMatches(){
        assertTrue(backHomeHandler.shouldOpen(android.R.id.home));
    }

    @Test
    public void ShouldCheckWhetherActivityCanNotBeStartedForOtherId(){
        assertFalse(backHomeHandler.shouldOpen(android.R.id.background));
    }

    @Test
    public void ShouldFinishCurrentActivity(){
        backHomeHandler.open();

        Mockito.verify(householdListActivityMock).finish();
    }

}