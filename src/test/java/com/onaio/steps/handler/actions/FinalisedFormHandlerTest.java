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

package com.onaio.steps.handler.actions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.ComponentName;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.HouseholdListActivity;
import com.onaio.steps.model.RequestCode;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

public class FinalisedFormHandlerTest extends StepsTestRunner {

    private FinalisedFormHandler finalisedFormHandler;
    private HouseholdListActivity householdListActivity;

    @Before
    public void Setup(){
        householdListActivity = Mockito.mock(HouseholdListActivity.class);
        finalisedFormHandler = new FinalisedFormHandler(householdListActivity);
    }

    @Test
    public void ShouldOpenActivityForProperMenuId(){
        assertTrue(finalisedFormHandler.shouldOpen(R.id.action_saved_form));
    }

    @Test
    public void ShouldNotOpenForOtherMenuId(){
        assertFalse(finalisedFormHandler.shouldOpen(R.id.action_deferred));
    }

    @Test
    public void ShouldOpenTheProperIntentForSavedForms(){
        finalisedFormHandler.open();

        Mockito.verify(householdListActivity).startActivityForResult(Mockito.argThat(intentMatcher()), Mockito.eq(RequestCode.DATA_SUBMISSION.getCode()));
    }

    private ArgumentMatcher<Intent> intentMatcher() {
        return intent -> {
            ComponentName component = intent.getComponent();
            Assert.assertEquals(Intent.ACTION_EDIT,intent.getAction());
            Assert.assertEquals("org.odk.collect.android",component.getPackageName());
            Assert.assertEquals("org.odk.collect.android.activities.InstanceUploaderList",component.getClassName());
            return true;
        };
    }


}