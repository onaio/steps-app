package com.onaio.steps.activityHandler;

import android.content.Intent;

import com.onaio.steps.activity.StepsActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class StepsActivityHandlerTest {

    StepsActivity stepsActivityMock;
    StepsActivityHandler stepsActivityHandler;
    @Before
    public void setup(){
        stepsActivityMock = Robolectric.setupActivity(StepsActivity.class);
        stepsActivityHandler = new StepsActivityHandler(stepsActivityMock);
    }

    @Test
    public void ShouldCheckWhetherActivityCanBeStartedWhenProperIdMatches(){
        assertTrue(stepsActivityHandler.shouldOpen(android.R.id.home));
    }

    @Test
    public void ShouldCheckWhetherActivityCanNotBeStartedForOtherId(){
        assertFalse(stepsActivityHandler.shouldOpen(android.R.id.background));
    }

    @Test
    public void ShouldOpenStepsActivity(){
        ShadowActivity stepsActivityShadow = Robolectric.shadowOf(stepsActivityMock);

        stepsActivityHandler.open();


        Intent newIntent = stepsActivityShadow.getNextStartedActivityForResult().intent;
        assertTrue(newIntent.getComponent().getClassName().equals(StepsActivity.class.getName()));
    }

}