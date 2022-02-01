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

package com.onaio.steps.handler.activities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.MainActivityOrchestrator;
import com.onaio.steps.activities.WelcomeActivity;
import com.onaio.steps.model.RequestCode;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowActivity;

public class SettingActivityHandlerTest extends StepsTestRunner {


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
        ShadowActivity shadowActivity = shadowOf(welcomeActivity);
        Intent nextStartedActivity = shadowActivity.getNextStartedActivity();
        assertEquals(MainActivityOrchestrator.class.getName(),nextStartedActivity.getComponent().getClassName());
        assertTrue(welcomeActivity.isFinishing());
    }

}