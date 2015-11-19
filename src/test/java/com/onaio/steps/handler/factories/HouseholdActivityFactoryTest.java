package com.onaio.steps.handler.factories;

import android.content.Intent;

import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.handler.actions.BackHomeHandler;
import com.onaio.steps.handler.actions.CancelParticipantSelectionHandler;
import com.onaio.steps.handler.actions.DeferredHandler;
import com.onaio.steps.handler.activities.EditHouseholdActivityHandler;
import com.onaio.steps.handler.interfaces.IActivityResultHandler;
import com.onaio.steps.handler.interfaces.IListItemHandler;
import com.onaio.steps.handler.interfaces.IMenuHandler;
import com.onaio.steps.handler.interfaces.IMenuPreparer;
import com.onaio.steps.handler.activities.MemberActivityHandler;
import com.onaio.steps.handler.activities.NewMemberActivityHandler;
import com.onaio.steps.handler.actions.RefusedHandler;
import com.onaio.steps.handler.actions.SelectParticipantHandler;
import com.onaio.steps.handler.actions.SelectedParticipantActionsHandler;
import com.onaio.steps.handler.SelectedParticipantContainerHandler;
import com.onaio.steps.handler.actions.TakeSurveyHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

import junit.framework.Assert;
import junit.framework.TestCase;

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
public class HouseholdActivityFactoryTest extends TestCase {

    private Household household;
    private HouseholdActivity activity;

    @Before
    public void Setup(){
        household = new Household("name", "123", InterviewStatus.NOT_SELECTED, "12-12-2015","Dummy comments");
        Intent intent = new Intent().putExtra(Constants.HH_HOUSEHOLD, household);
        activity = Robolectric.buildActivity(HouseholdActivity.class).withIntent(intent).create().get();
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

        assertEquals(8, menuHandlers.size());
        Assert.assertTrue(menuHandlerTypes.contains(TakeSurveyHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(DeferredHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(RefusedHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(SelectedParticipantActionsHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(NewMemberActivityHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(SelectParticipantHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(SelectedParticipantContainerHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(CancelParticipantSelectionHandler.class));

    }

    @Test
    public void ShouldHaveCustomMenuHandlers(){
        List<IMenuHandler> menuHandlers = HouseholdActivityFactory.getCustomMenuHandler(activity, household);

        ArrayList<Class> menuHandlerTypes = getTypes(menuHandlers);

        assertEquals(6, menuHandlers.size());
        Assert.assertTrue(menuHandlerTypes.contains(TakeSurveyHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(DeferredHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(RefusedHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(NewMemberActivityHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(CancelParticipantSelectionHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(SelectParticipantHandler.class));
    }

    private <T> ArrayList<Class> getTypes(List<T> menuHandlers) {
        ArrayList<Class> menuHandlerTypes = new ArrayList<Class>();
        for (T handler:menuHandlers)
            menuHandlerTypes.add(handler.getClass());
        return menuHandlerTypes;
    }
}