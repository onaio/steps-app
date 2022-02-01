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

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.ParticipantListActivity;
import com.onaio.steps.handler.actions.FinalisedFormHandler;
import com.onaio.steps.handler.actions.SubmitDataHandler;
import com.onaio.steps.handler.activities.NewParticipantActivityHandler;
import com.onaio.steps.handler.activities.ParticipantActivityHandler;
import com.onaio.steps.handler.activities.SettingActivityHandler;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IListItemHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import java.util.ArrayList;
import java.util.List;

public class ParticipantListActivityFactoryTest extends StepsTestRunner {

    private ParticipantListActivity participantListActivityMock;

    @Before
    public void Setup() {
        participantListActivityMock = Robolectric.buildActivity(ParticipantListActivity.class).create().get();
    }

    @Test
    public void ShouldHaveProperMenuHandlers() {
        List<IMenuHandler> menuHandlers = ParticipantListActivityFactory.getMenuHandlers(participantListActivityMock);
        ArrayList<Class> handlerTypes = getTypes(menuHandlers);

        Assert.assertEquals(3, menuHandlers.size());
        Assert.assertTrue(handlerTypes.contains(SettingActivityHandler.class));
        Assert.assertTrue(handlerTypes.contains(FinalisedFormHandler.class));
    }

    @Test
    public void ShouldHaveProperResultHandlers() {
        List<IActivityResultHandler> resultHandlers = ParticipantListActivityFactory.getResultHandlers(participantListActivityMock);
        ArrayList<Class> handlerTypes = getTypes(resultHandlers);

        Assert.assertEquals(2, resultHandlers.size());
        Assert.assertTrue(handlerTypes.contains(SettingActivityHandler.class));
        Assert.assertTrue(handlerTypes.contains(NewParticipantActivityHandler.class));
    }

    @Test
    public void ShouldHaveProperCustomMenuHandlers() {
        List<IMenuHandler> menuHandlers = ParticipantListActivityFactory.getCustomMenuHandler(participantListActivityMock);
        ArrayList<Class> handlerTypes = getTypes(menuHandlers);

        Assert.assertEquals(3, menuHandlers.size());
        Assert.assertTrue(handlerTypes.contains(SettingActivityHandler.class));
        Assert.assertTrue(handlerTypes.contains(NewParticipantActivityHandler.class));
        Assert.assertTrue(handlerTypes.contains(SubmitDataHandler.class));
    }

    @Test
    public void ShouldHaveProperHouseholdItemHandler() {
        IListItemHandler handler = ParticipantListActivityFactory.getParticipantItemHandler(participantListActivityMock, null);

        Assert.assertEquals(ParticipantActivityHandler.class, handler.getClass());
    }


    private <T> ArrayList<Class> getTypes(List<T> menuHandlers) {
        ArrayList<Class> handlerTypes = new ArrayList<Class>();
        for (T handler : menuHandlers)
            handlerTypes.add(handler.getClass());
        return handlerTypes;
    }
}