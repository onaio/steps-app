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

import static junit.framework.Assert.assertEquals;

import android.content.Intent;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.ParticipantActivity;
import com.onaio.steps.handler.actions.BackHomeHandler;
import com.onaio.steps.handler.actions.DeferredHandler;
import com.onaio.steps.handler.actions.IncompleteRefusedHandler;
import com.onaio.steps.handler.actions.NotReachableHandler;
import com.onaio.steps.handler.actions.RefusedHandler;
import com.onaio.steps.handler.actions.TakeSurveyHandler;
import com.onaio.steps.handler.activities.EditParticipantActivityHandler;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.Participant;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import java.util.ArrayList;
import java.util.List;

public class ParticipantActivityFactoryTest extends StepsTestRunner {

    private Participant participant;
    private ParticipantActivity participantActivity;

    @Before
    public void Setup() {
        participant = new Participant("123-100", "surname", "firstName", Gender.Female, 33, InterviewStatus.NOT_DONE, "2015-10-10");
        Intent intent = new Intent().putExtra(Constants.PARTICIPANT, participant);
        participantActivity = Robolectric.buildActivity(ParticipantActivity.class, intent).create().get();
    }

    @Test
    public void ShouldGetProperMenuHandlers() {
        List<IMenuHandler> menuHandlers = ParticipantActivityFactory.getMenuHandlers(participantActivity, participant);
        ArrayList<Class> handlerTypes = getTypes(menuHandlers);

        assertEquals(2, menuHandlers.size());
        Assert.assertTrue(handlerTypes.contains(EditParticipantActivityHandler.class));
        Assert.assertTrue(handlerTypes.contains(BackHomeHandler.class));
    }

    @Test
    public void ShouldGetProperMenuPrepares() {
        List<IMenuPreparer> menuHandlers = ParticipantActivityFactory.getMenuPreparer(participantActivity, participant, null);
        ArrayList<Class> handlerTypes = getTypes(menuHandlers);

        assertEquals(1, menuHandlers.size());
        Assert.assertTrue(handlerTypes.contains(EditParticipantActivityHandler.class));
    }

    @Test
    public void ShouldGetProperMenuResultHandlers() {
        List<IActivityResultHandler> menuHandlers = ParticipantActivityFactory.getResultHandlers(participantActivity, participant);
        ArrayList<Class> handlerTypes = getTypes(menuHandlers);

        assertEquals(2, menuHandlers.size());
        Assert.assertTrue(handlerTypes.contains(EditParticipantActivityHandler.class));
        Assert.assertTrue(handlerTypes.contains(TakeSurveyHandler.class));
    }

    private <T> ArrayList<Class> getTypes(List<T> menuHandlers) {
        ArrayList<Class> handlerTypes = new ArrayList<Class>();
        for (T handler : menuHandlers)
            handlerTypes.add(handler.getClass());
        return handlerTypes;
    }

    @Test
    public void ShouldGetProperCustomMenu() {
        List<IMenuPreparer> customMenuPreparer = ParticipantActivityFactory.getCustomMenuPreparer(participantActivity, participant);
        ArrayList<Class> handlerTypes = getTypes(customMenuPreparer);

        assertEquals(5, customMenuPreparer.size());
        Assert.assertTrue(handlerTypes.contains(TakeSurveyHandler.class));
        Assert.assertTrue(handlerTypes.contains(DeferredHandler.class));
        Assert.assertTrue(handlerTypes.contains(RefusedHandler.class));
        Assert.assertTrue(handlerTypes.contains(IncompleteRefusedHandler.class));
        Assert.assertTrue(handlerTypes.contains(NotReachableHandler.class));
    }

    @Test
    public void ShouldGetProperCustomMenuHandlers() {
        List<IMenuHandler> customMenuHandler = ParticipantActivityFactory.getCustomMenuHandler(participantActivity, participant);
        ArrayList<Class> handlerTypes = getTypes(customMenuHandler);

        assertEquals(5, customMenuHandler.size());
        Assert.assertTrue(handlerTypes.contains(TakeSurveyHandler.class));
        Assert.assertTrue(handlerTypes.contains(DeferredHandler.class));
        Assert.assertTrue(handlerTypes.contains(RefusedHandler.class));
        Assert.assertTrue(handlerTypes.contains(IncompleteRefusedHandler.class));
        Assert.assertTrue(handlerTypes.contains(NotReachableHandler.class));
    }
}