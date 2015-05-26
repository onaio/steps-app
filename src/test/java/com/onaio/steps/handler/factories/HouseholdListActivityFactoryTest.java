package com.onaio.steps.handler.factories;

import com.onaio.steps.activities.HouseholdListActivity;
import com.onaio.steps.handler.actions.ExportHandler;
import com.onaio.steps.handler.actions.FinalisedFormHandler;
import com.onaio.steps.handler.actions.ImportHandler;
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

        Assert.assertEquals(4,menuHandlers.size());
        Assert.assertTrue(handlerTypes.contains(SettingActivityHandler.class));
        Assert.assertTrue(handlerTypes.contains(ExportHandler.class));
        Assert.assertTrue(handlerTypes.contains(ImportHandler.class));
        Assert.assertTrue(handlerTypes.contains(FinalisedFormHandler.class));
    }

    @Test
    public void ShouldHaveProperResultHandlers(){
        List<IActivityResultHandler> resultHandlers = HouseholdListActivityFactory.getResultHandlers(householdListActivityMock);
        ArrayList<Class> handlerTypes = getTypes(resultHandlers);

        Assert.assertEquals(3, resultHandlers.size());
        Assert.assertTrue(handlerTypes.contains(SettingActivityHandler.class));
        Assert.assertTrue(handlerTypes.contains(NewHouseholdActivityHandler.class));
        Assert.assertTrue(handlerTypes.contains(ImportHandler.class));
    }

    @Test
    public void ShouldHaveProperCustomMenuHandlers(){
        List<IMenuHandler> menuHandlers = HouseholdListActivityFactory.getCustomMenuHandler(householdListActivityMock);
        ArrayList<Class> handlerTypes = getTypes(menuHandlers);

        Assert.assertEquals(1,menuHandlers.size());
        Assert.assertTrue(handlerTypes.contains(NewHouseholdActivityHandler.class));
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