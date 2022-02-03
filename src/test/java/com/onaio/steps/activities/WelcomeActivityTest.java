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

package com.onaio.steps.activities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.robolectric.Shadows.shadowOf;

import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

public class WelcomeActivityTest extends StepsTestRunner {

    private WelcomeActivity welcomeActivity;

    @Before
    public void setUp() throws Exception {
        welcomeActivity = Robolectric.buildActivity(WelcomeActivity.class).create().get();
    }

    @Test
    public void ShouldBeAbleToLoadWelcomeLayout(){
           assertEquals(R.id.welcome_layout,shadowOf(welcomeActivity).getContentView().getId());
    }

    @Test
    public void ShouldSetFirstLayoutProperlyWhenPhoneIdIsNotSet(){
        View mainLayout = welcomeActivity.findViewById(R.id.main_layout);
        View firstMain = welcomeActivity.findViewById(R.id.welcome_layout);
        View getStarted = welcomeActivity.findViewById(R.id.go_to_settings);
        String title = welcomeActivity.getTitle().toString();

        assertNotNull(getStarted);
        assertNull(mainLayout);
        assertNotNull(firstMain);
        assertEquals("STEPS", title);
    }
}