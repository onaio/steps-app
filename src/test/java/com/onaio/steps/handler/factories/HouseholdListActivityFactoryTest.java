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

package com.onaio.steps.handler.factories;

import com.onaio.steps.activities.HouseholdListActivity;
import com.onaio.steps.handler.actions.ExportHandler;
import com.onaio.steps.handler.actions.FinalisedFormHandler;
import com.onaio.steps.handler.actions.ImportHandler;
import com.onaio.steps.handler.actions.SaveToSDCardHandler;
import com.onaio.steps.handler.actions.SubmitDataHandler;
import com.onaio.steps.handler.activities.HouseholdActivityHandler;
import com.onaio.steps.handler.activities.NewHouseholdActivityHandler;
import com.onaio.steps.handler.activities.SettingActivityHandler;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IListItemHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class HouseholdListActivityFactoryTest{

    private HouseholdListActivity householdListActivityMock;
    @Before
    public void Setup(){
        householdListActivityMock = Robolectric.buildActivity(HouseholdListActivity.class).create().get();
    }

    @Test
    public void ShouldHaveProperMenuHandlers(){
        List<IMenuHandler> menuHandlers = HouseholdListActivityFactory.getMenuHandlers(householdListActivityMock, null);
        ArrayList<Class> handlerTypes = getTypes(menuHandlers);

        Assert.assertEquals(5,menuHandlers.size());
        Assert.assertTrue(handlerTypes.contains(SettingActivityHandler.class));
        Assert.assertTrue(handlerTypes.contains(SubmitDataHandler.class));
        Assert.assertTrue(handlerTypes.contains(ImportHandler.class));
        Assert.assertTrue(handlerTypes.contains(FinalisedFormHandler.class));
        Assert.assertTrue(handlerTypes.contains(SaveToSDCardHandler.class));
    }

    @Test
    public void ShouldHaveProperResultHandlers(){
        List<IActivityResultHandler> resultHandlers = HouseholdListActivityFactory.getResultHandlers(householdListActivityMock);
        ArrayList<Class> handlerTypes = getTypes(resultHandlers);

        Assert.assertEquals(2, resultHandlers.size());
        Assert.assertTrue(handlerTypes.contains(SettingActivityHandler.class));
        Assert.assertTrue(handlerTypes.contains(NewHouseholdActivityHandler.class));
    }

    @Test
    public void ShouldHaveProperCustomMenuHandlers(){
        List<IMenuHandler> menuHandlers = HouseholdListActivityFactory.getCustomMenuHandler(householdListActivityMock, null);
        ArrayList<Class> handlerTypes = getTypes(menuHandlers);

        Assert.assertEquals(2,menuHandlers.size());
        Assert.assertTrue(handlerTypes.contains(NewHouseholdActivityHandler.class));
        Assert.assertTrue(handlerTypes.contains(SubmitDataHandler.class));
    }

    @Test
    public void ShouldHaveProperHouseholdItemHandler(){
        IListItemHandler handler = HouseholdListActivityFactory.getHouseholdItemHandler(householdListActivityMock, null);

        Assert.assertEquals(HouseholdActivityHandler.class,handler.getClass());
    }


    private<T> ArrayList<Class> getTypes(List<T> menuHandlers) {
        ArrayList<Class> handlerTypes = new ArrayList<Class>();
        for(T handler:menuHandlers)
            handlerTypes.add(handler.getClass());
        return handlerTypes;
    }
}