package com.onaio.steps.activityHandler.Factory;

import com.onaio.steps.activity.SettingsActivity;
import com.onaio.steps.activity.StepsActivity;
import com.onaio.steps.activityHandler.ExportHandler;
import com.onaio.steps.activityHandler.HouseholdActivityHandler;
import com.onaio.steps.activityHandler.ImportHandler;
import com.onaio.steps.activityHandler.Interface.IActivityResultHandler;
import com.onaio.steps.activityHandler.Interface.IListItemHandler;
import com.onaio.steps.activityHandler.Interface.IMenuHandler;
import com.onaio.steps.activityHandler.NewHouseholdActivityHandler;
import com.onaio.steps.activityHandler.SavedFormsHandler;
import com.onaio.steps.activityHandler.SettingActivityHandler;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;

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
public class StepsActivityFactoryTest extends TestCase {

    private StepsActivity stepsActivityMock;
    @Before
    public void Setup(){
        stepsActivityMock = Robolectric.buildActivity(StepsActivity.class).create().get();
    }

    @Test
    public void ShouldHaveProperMenuHandlers(){
        List<IMenuHandler> menuHandlers = StepsActivityFactory.getMenuHandlers(stepsActivityMock, null);
        ArrayList<Class> handlerTypes = getTypes(menuHandlers);

        Assert.assertEquals(3,menuHandlers.size());
        Assert.assertTrue(handlerTypes.contains(SettingActivityHandler.class));
        Assert.assertTrue(handlerTypes.contains(ExportHandler.class));
        Assert.assertTrue(handlerTypes.contains(ImportHandler.class));
    }

    @Test
    public void ShouldHaveProperResultHandlers(){
        List<IActivityResultHandler> resultHandlers = StepsActivityFactory.getResultHandlers(stepsActivityMock);
        ArrayList<Class> handlerTypes = getTypes(resultHandlers);

        Assert.assertEquals(4, resultHandlers.size());
        Assert.assertTrue(handlerTypes.contains(SettingActivityHandler.class));
        Assert.assertTrue(handlerTypes.contains(NewHouseholdActivityHandler.class));
        Assert.assertTrue(handlerTypes.contains(ImportHandler.class));
        Assert.assertTrue(handlerTypes.contains(SavedFormsHandler.class));
    }

    @Test
    public void ShouldHaveProperCustomMenuHandlers(){
        List<IMenuHandler> menuHandlers = StepsActivityFactory.getCustomMenuHandler(stepsActivityMock);
        ArrayList<Class> handlerTypes = getTypes(menuHandlers);

        Assert.assertEquals(2,menuHandlers.size());
        Assert.assertTrue(handlerTypes.contains(SettingActivityHandler.class));
        Assert.assertTrue(handlerTypes.contains(NewHouseholdActivityHandler.class));
    }

    @Test
    public void ShouldHaveProperHouseholdItemHandler(){
        IListItemHandler handler = StepsActivityFactory.getHouseholdItemHandler(stepsActivityMock, null);

        Assert.assertEquals(HouseholdActivityHandler.class,handler.getClass());
    }


    private<T> ArrayList<Class> getTypes(List<T> menuHandlers) {
        ArrayList<Class> handlerTypes = new ArrayList<Class>();
        for(T handler:menuHandlers)
            handlerTypes.add(handler.getClass());
        return handlerTypes;
    }
}