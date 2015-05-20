package com.onaio.steps.activityHandler;

import com.onaio.steps.activity.HouseholdListActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class BackHomeHandlerTest {

    HouseholdListActivity householdListActivityMock;
    BackHomeHandler backHomeHandler;
    @Before
    public void setup(){
        householdListActivityMock = Mockito.mock(HouseholdListActivity.class);
        backHomeHandler = new BackHomeHandler(householdListActivityMock);
    }

    @Test
    public void ShouldCheckWhetherActivityCanBeStartedWhenProperIdMatches(){
        assertTrue(backHomeHandler.shouldOpen(android.R.id.home));
    }

    @Test
    public void ShouldCheckWhetherActivityCanNotBeStartedForOtherId(){
        assertFalse(backHomeHandler.shouldOpen(android.R.id.background));
    }

    @Test
    public void ShouldFinishCurrentActivity(){
        backHomeHandler.open();

        Mockito.verify(householdListActivityMock).finish();
    }

}