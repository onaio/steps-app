package com.onaio.steps.activityHandler;

import android.content.Intent;

import com.onaio.steps.activity.StepsActivity;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class BackHomeHandlerTest {

    StepsActivity stepsActivityMock;
    BackHomeHandler backHomeHandler;
    @Before
    public void setup(){
        stepsActivityMock = Mockito.mock(StepsActivity.class);
        backHomeHandler = new BackHomeHandler(stepsActivityMock);
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

        Mockito.verify(stepsActivityMock).finish();
    }

}