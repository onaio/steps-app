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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Member;
import com.onaio.steps.model.ServerStatus;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.fakes.RoboMenu;

import java.util.List;

public class HouseholdListActivityTest extends StepsTestRunner {

    private HouseholdListActivity householdListActivityMock;

    @Before
    public void Setup(){
        Household household = new Household("1", "household Name", "123456789", "1", InterviewStatus.NOT_DONE, "2015-12-13", "uniqueDevId", "Dummy comments");
        household.setServerStatus(ServerStatus.NOT_SENT);
        Member member = new Member(1, "rana", "manisha", Gender.Female, 28, household, "123-1", false);
        DatabaseHelper db = new DatabaseHelper(ApplicationProvider.getApplicationContext());
        household.save(db);
        member.save(db);
        householdListActivityMock = Robolectric.buildActivity(HouseholdListActivity.class)
                .create()
                .get();

    }

    @Test
    public void ShouldSetMainLayoutProperlyOnCreateWhenPhoneIdIsSet(){
        KeyValueStoreFactory.instance(householdListActivityMock).putString(Constants.HH_PHONE_ID,"123");

        View mainLayout = householdListActivityMock.findViewById(R.id.main_layout);
        View firstMain = householdListActivityMock.findViewById(R.id.welcome_layout);
        String title = householdListActivityMock.getTitle().toString();
        //int titleColor = householdListActivityMock.getTitleColor();

        assertNotNull(mainLayout);
        assertNull(firstMain);
        assertEquals(householdListActivityMock.getString(R.string.main_header), title);
        //assertEquals(Color.parseColor(Constants.HEADER_GREEN),titleColor);
    }

    @Test
    public void ShouldPopulateMenu(){
        RoboMenu menu = new RoboMenu();

        householdListActivityMock.onCreateOptionsMenu(menu);

        MenuItem exportMenuItem = menu.findItem(R.id.action_export);
        MenuItem settingsMenuItem = menu.findItem(R.id.action_settings);
        MenuItem importMenuItem = menu.findItem(R.id.action_import);
        MenuItem savedFormItem = menu.findItem(R.id.action_saved_form);

        assertNotNull(exportMenuItem);
        assertNotNull(settingsMenuItem);
        assertNotNull(importMenuItem);
        assertNotNull(savedFormItem);
    }

    @Test
    public void ShouldGetMenuViewLayout(){
        assertEquals(R.menu.main_activity_actions,householdListActivityMock.getMenuViewLayout());
    }

    @Test
    public void ShouldGetProperMenuHandlers(){
        List<IMenuHandler> menuHandlers = householdListActivityMock.getMenuHandlers();
        assertEquals(6,menuHandlers.size());
    }

    @Test
    public void ShouldGetProperResultHandlers(){
        List<IActivityResultHandler> resultHandlers = householdListActivityMock.getResultHandlers();
        assertEquals(2,resultHandlers.size());
    }

    @Test
    public void ShouldGetProperMenuPreparer(){
        Menu mock = Mockito.mock(Menu.class);
        List<IMenuPreparer> menuHandlers = householdListActivityMock.getMenuPreparer(mock);
        assertEquals(1,menuHandlers.size());
    }

    @Test
    public void ShouldGetProperCustomMenuHandlers(){
        List<IMenuHandler> menuHandlers = householdListActivityMock.getCustomMenuHandler();
        assertEquals(2,menuHandlers.size());
    }
}