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

package com.onaio.steps.handler.strategies.survey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;

import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.ODKForm.ODKSavedForm;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TakeSurveyForHouseholdStrategyTest extends StepsTestRunner {

    private Household household;
    private TakeSurveyForHouseholdStrategy takeSurveyForHouseholdStrategy;

    @Before
    public void Setup(){
        household = Mockito.mock(Household.class);
        HouseholdActivity householdActivity = Mockito.mock(HouseholdActivity.class);
        takeSurveyForHouseholdStrategy = new TakeSurveyForHouseholdStrategy(household, householdActivity);
    }

    @Test
    public void ShouldNotInactivateWhenMemberIsSelected(){
        Mockito.when(household.getStatus()).thenReturn(InterviewStatus.NOT_DONE);
        assertFalse(takeSurveyForHouseholdStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsDone(){
        Mockito.when(household.getStatus()).thenReturn(InterviewStatus.DONE);
        assertTrue(takeSurveyForHouseholdStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsDeferred(){
        Mockito.when(household.getStatus()).thenReturn(InterviewStatus.DEFERRED);
        assertFalse(takeSurveyForHouseholdStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsIncomplete(){
        Mockito.when(household.getStatus()).thenReturn(InterviewStatus.INCOMPLETE);
        assertFalse(takeSurveyForHouseholdStrategy.shouldInactivate());
    }


    @Test
    public void ShouldHandleResultForSurveyCompleted(){
        ODKSavedForm odkSavedForm = Mockito.mock(ODKSavedForm.class);

        Mockito.when(odkSavedForm.getStatus()).thenReturn(Constants.ODK_FORM_COMPLETE_STATUS);
        Mockito.when(odkSavedForm.getId()).thenReturn("0");
        Mockito.when(odkSavedForm.getJrFormId()).thenReturn("test_form");
        Mockito.when(odkSavedForm.getDisplayName()).thenReturn("Test Form");

        takeSurveyForHouseholdStrategy.handleResult(odkSavedForm);

        Mockito.verify(household).setStatus(eq(InterviewStatus.DONE));
        Mockito.verify(household).setOdkFormId(eq("0"));
        Mockito.verify(household).setOdkJrFormId(eq("test_form"));
        Mockito.verify(household).setOdkJrFormTitle(eq("Test Form"));
        Mockito.verify(household).update(Mockito.any(DatabaseHelper.class));
    }

    @Test
    public void ShouldGetNameFormat(){
        String formNameFormat = "STEPS_Instrument_V3_1-%s";
        Mockito.when(household.getName()).thenReturn("123-100");
        assertEquals("STEPS_Instrument_V3_1-123-100",takeSurveyForHouseholdStrategy.getFormName(formNameFormat));

    }

}