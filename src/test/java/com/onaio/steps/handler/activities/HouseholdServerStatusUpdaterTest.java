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

import android.os.AsyncTask;

import com.onaio.steps.activities.HouseholdListActivity;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.ServerStatus;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowAsyncTask;

import java.util.concurrent.Executor;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml", shadows = {HouseholdServerStatusUpdaterTest.ShadowLoadingDoneHouseHold.class})
@RunWith(RobolectricTestRunner.class)
public class HouseholdServerStatusUpdaterTest {

    private HouseholdListActivity householdListActivity;
    private HouseholdServerStatusUpdater householdServerStatusUpdater;

    @Before
    public void setup(){
        householdListActivity = Mockito.spy(Robolectric.buildActivity(HouseholdListActivity.class).create().resume().get());
        householdServerStatusUpdater = new HouseholdServerStatusUpdater(householdListActivity);
    }

    @Test
    public void ShouldVerifyAllTheServerStatusBeforeAndAfterSent() {
        DatabaseHelper db = new DatabaseHelper(householdListActivity);

        Household household = new Household("", "1-1", "", "", InterviewStatus.DONE, "", "", "");
        household.setServerStatus(ServerStatus.NOT_SENT);
        household.save(db);

        Assert.assertEquals(ServerStatus.NOT_SENT, Household.find_by(db,"1-1").getServerStatus());
        householdServerStatusUpdater.markAllSent();

        Assert.assertEquals(ServerStatus.SENT, Household.find_by(db,"1-1").getServerStatus());
        Mockito.doNothing().when(householdListActivity).refreshList();
        Mockito.verify(householdListActivity, Mockito.times(1)).refreshList();
    }

    @Implements(AsyncTask.class)
    public static class ShadowLoadingDoneHouseHold<Params, Progress, Result> extends ShadowAsyncTask<Params, Progress, Result> {

        @Override
        @Implementation
        public AsyncTask<Params, Progress, Result> executeOnExecutor(Executor executor, Params... params) {
            return super.execute(params);
        }
    }
}