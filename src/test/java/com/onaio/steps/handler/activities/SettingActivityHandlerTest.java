package com.onaio.steps.handler.activities;

import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activities.MainActivityOrchestrator;
import com.onaio.steps.activities.WelcomeActivity;
import com.onaio.steps.model.RequestCode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SettingActivityHandlerTest {


    private WelcomeActivity welcomeActivity;
    private SettingActivityHandler settingActivityHandler;

    @Before
    public void setup(){
        welcomeActivity = Robolectric.setupActivity(WelcomeActivity.class);
        settingActivityHandler = new SettingActivityHandler(welcomeActivity);
    }

    @Test
    public void ShouldCheckWhetherSettingsActivityCanBeStartedForProperId(){
        assertTrue(settingActivityHandler.shouldOpen(R.id.action_settings));
        assertTrue(settingActivityHandler.shouldOpen(R.id.go_to_settings));
    }

    @Test
    public void ShouldCheckSettingsActivityShouldNotStartedForOtherId(){
        assertFalse(settingActivityHandler.shouldOpen(R.id.action_deferred));
    }

    @Test
    public void ShouldHandleResultForProperRequestCode() {
        assertTrue(settingActivityHandler.canHandleResult(RequestCode.SETTINGS.getCode()));
    }

    @Test
    public void ShouldNotHandleResultForOtherRequestCode() {
        assertFalse(settingActivityHandler.canHandleResult(RequestCode.NEW_MEMBER.getCode()));
    }

    @Test
    public void ShouldBeAbleToHandleResultAndStartMainOrchestraActivity(){
        settingActivityHandler.handleResult(null, WelcomeActivity.RESULT_OK);
        ShadowActivity shadowActivity = Robolectric.shadowOf(welcomeActivity);
        Intent nextStartedActivity = shadowActivity.getNextStartedActivity();
        assertEquals(MainActivityOrchestrator.class.getName(),nextStartedActivity.getComponent().getClassName());
        assertTrue(welcomeActivity.isFinishing());
    }

}