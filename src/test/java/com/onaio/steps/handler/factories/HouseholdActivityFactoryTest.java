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

import static junit.framework.TestCase.assertEquals;

import android.content.Intent;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.handler.HouseholdActivityBackButtonPreparer;
import com.onaio.steps.handler.SelectedParticipantContainerHandler;
import com.onaio.steps.handler.actions.BackHomeHandler;
import com.onaio.steps.handler.actions.CancelParticipantSelectionHandler;
import com.onaio.steps.handler.actions.DeferredHandler;
import com.onaio.steps.handler.actions.IncompleteRefusedHandler;
import com.onaio.steps.handler.actions.NotReachableHandler;
import com.onaio.steps.handler.actions.NotReachableOnEmptyHouseholdHandler;
import com.onaio.steps.handler.actions.RefusedHandler;
import com.onaio.steps.handler.actions.SelectParticipantHandler;
import com.onaio.steps.handler.actions.SelectedParticipantActionsHandler;
import com.onaio.steps.handler.actions.TakeSurveyHandler;
import com.onaio.steps.handler.activities.EditHouseholdActivityHandler;
import com.onaio.steps.handler.activities.MemberActivityHandler;
import com.onaio.steps.handler.activities.NewMemberActivityHandler;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IListItemHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import java.util.ArrayList;
import java.util.List;

public class HouseholdActivityFactoryTest extends StepsTestRunner {

    private Household household;
    private HouseholdActivity activity;

    @Before
    public void Setup(){
        household = new Household("name", "123", InterviewStatus.SELECTION_NOT_DONE, "12-12-2015", "testDeviceId","Dummy comments", null);
        Intent intent = new Intent().putExtra(Constants.HH_HOUSEHOLD, household);
        activity = Robolectric.buildActivity(HouseholdActivity.class, intent).create().get();
    }

    @Test
    public void ShouldHaveProperMenuHandlers(){
        List<IMenuHandler> menuHandlers = HouseholdActivityFactory.getMenuHandlers(activity, household);

        ArrayList<Class> menuHandlerTypes = getTypes(menuHandlers);

        assertEquals(3,menuHandlers.size());
        Assert.assertTrue(menuHandlerTypes.contains(SelectParticipantHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(BackHomeHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(EditHouseholdActivityHandler.class));
    }

    @Test
    public void ShouldHaveProperResultHandlers(){
        List<IActivityResultHandler> menuHandlers = HouseholdActivityFactory.getResultHandlers(activity, household);

        ArrayList<Class> menuHandlerTypes = getTypes(menuHandlers);

        assertEquals(3, menuHandlers.size());
        Assert.assertTrue(menuHandlerTypes.contains(NewMemberActivityHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(EditHouseholdActivityHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(TakeSurveyHandler.class));
    }

    @Test
    public void ShouldHaveMemberActivityHandlerAsMemberItemHandler(){
        IListItemHandler itemHandler = HouseholdActivityFactory.getMemberItemHandler(activity, null);

        Assert.assertEquals(MemberActivityHandler.class, itemHandler.getClass());
    }

    @Test
    public void ShouldHaveCustomMenuPreparers(){
        List<IMenuPreparer> menuHandlers = HouseholdActivityFactory.getCustomMenuPreparer(activity, household);

        ArrayList<Class> menuHandlerTypes = getTypes(menuHandlers);

        assertEquals(12, menuHandlers.size());
        Assert.assertTrue(menuHandlerTypes.contains(TakeSurveyHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(DeferredHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(RefusedHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(SelectedParticipantActionsHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(NewMemberActivityHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(SelectParticipantHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(SelectedParticipantContainerHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(CancelParticipantSelectionHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(IncompleteRefusedHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(HouseholdActivityBackButtonPreparer.class));
        Assert.assertTrue(menuHandlerTypes.contains(NotReachableHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(NotReachableOnEmptyHouseholdHandler.class));

    }

    @Test
    public void ShouldHaveCustomMenuHandlers(){
        List<IMenuHandler> menuHandlers = HouseholdActivityFactory.getCustomMenuHandler(activity, household);

        ArrayList<Class> menuHandlerTypes = getTypes(menuHandlers);

        assertEquals(9, menuHandlers.size());
        Assert.assertTrue(menuHandlerTypes.contains(TakeSurveyHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(DeferredHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(RefusedHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(NewMemberActivityHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(CancelParticipantSelectionHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(SelectParticipantHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(IncompleteRefusedHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(NotReachableHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(NotReachableOnEmptyHouseholdHandler.class));
    }

    private <T> ArrayList<Class> getTypes(List<T> menuHandlers) {
        ArrayList<Class> menuHandlerTypes = new ArrayList<Class>();
        for (T handler:menuHandlers)
            menuHandlerTypes.add(handler.getClass());
        return menuHandlerTypes;
    }
}