package com.onaio.steps.activityHandler.Factory;

import android.content.Intent;

import com.onaio.steps.activity.EditHouseholdActivity;
import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.activity.NewMemberActivity;
import com.onaio.steps.activityHandler.BackHomeHandler;
import com.onaio.steps.activityHandler.DeferredHandler;
import com.onaio.steps.activityHandler.EditHouseholdActivityHandler;
import com.onaio.steps.activityHandler.ExportHandler;
import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IListItemHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.Interface.IMenuPreparer;
import com.onaio.steps.activityHandler.MemberActivityHandler;
import com.onaio.steps.activityHandler.NewMemberActivityHandler;
import com.onaio.steps.activityHandler.RefusedHandler;
import com.onaio.steps.activityHandler.SelectParticipantHandler;
import com.onaio.steps.activityHandler.SelectedParticipantActionsHandler;
import com.onaio.steps.activityHandler.SelectedParticipantContainerHandler;
import com.onaio.steps.activityHandler.TakeSurveyHandler;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

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
        household = new Household("name", "123", HouseholdStatus.NOT_SELECTED, "12-12-2015");
        Intent intent = new Intent().putExtra(Constants.HOUSEHOLD, household);
        activity = Robolectric.buildActivity(HouseholdActivity.class).withIntent(intent).create().get();
    }

    @Test
    public void ShouldHaveProperMenuHandlers(){
        List<IMenuHandler> menuHandlers = HouseholdActivityFactory.getMenuHandlers(activity, household);

        ArrayList<Class> menuHandlerTypes = getTypes(menuHandlers);

        assertEquals(4,menuHandlers.size());
        Assert.assertTrue(menuHandlerTypes.contains(ExportHandler.class));
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
        Assert.assertTrue(menuHandlerTypes.contains(SelectParticipantHandler.class));
    }

    @Test
    public void ShouldHaveMemberActivityHandlerAsMemberItemHandler(){
        IListItemHandler itemHandler = HouseholdActivityFactory.getMemberItemHandler(activity, null);

        Assert.assertEquals(MemberActivityHandler.class, itemHandler.getClass());
    }

    @Test
    public void ShouldHaveProperMenuPreparers(){
        List<IMenuPreparer> menuHandlers = HouseholdActivityFactory.getMenuPreparer(activity, household, null);

        ArrayList<Class> menuHandlerTypes = getTypes(menuHandlers);

        assertEquals(1, menuHandlers.size());
        Assert.assertTrue(menuHandlerTypes.contains(ExportHandler.class));
    }

    @Test
    public void ShouldHaveCustomMenuPreparers(){
        List<IMenuPreparer> menuHandlers = HouseholdActivityFactory.getCustomMenuPreparer(activity, household);

        ArrayList<Class> menuHandlerTypes = getTypes(menuHandlers);

        assertEquals(7, menuHandlers.size());
        Assert.assertTrue(menuHandlerTypes.contains(TakeSurveyHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(DeferredHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(RefusedHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(SelectedParticipantActionsHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(NewMemberActivityHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(SelectParticipantHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(SelectedParticipantContainerHandler.class));
    }

    @Test
    public void ShouldHaveCustomMenuHandlers(){
        List<IMenuHandler> menuHandlers = HouseholdActivityFactory.getCustomMenuHandler(activity, household);

        ArrayList<Class> menuHandlerTypes = getTypes(menuHandlers);

        assertEquals(5, menuHandlers.size());
        Assert.assertTrue(menuHandlerTypes.contains(TakeSurveyHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(DeferredHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(RefusedHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(NewMemberActivityHandler.class));
        Assert.assertTrue(menuHandlerTypes.contains(SelectParticipantHandler.class));
    }

    private <T> ArrayList<Class> getTypes(List<T> menuHandlers) {
        ArrayList<Class> menuHandlerTypes = new ArrayList<Class>();
        for (T handler:menuHandlers)
            menuHandlerTypes.add(handler.getClass());
        return menuHandlerTypes;
    }
}