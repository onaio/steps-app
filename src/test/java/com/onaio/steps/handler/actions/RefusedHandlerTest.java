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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import android.content.DialogInterface;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.handler.strategies.survey.RefuseSurveyForHouseholdStrategy;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class RefusedHandlerTest extends StepsTestRunner {

    private HouseholdActivity householdActivityMock;
    private Household householdMock;
    private RefusedHandler refusedHandler;
    private CustomDialog dialogMock;
    private int MENU_ID = R.id.action_refused;


    @Before
    public void setup(){
       householdActivityMock = Mockito.mock(HouseholdActivity.class);
       householdMock = Mockito.mock(Household.class);
       dialogMock = Mockito.mock(CustomDialog.class);
       refusedHandler = new RefusedHandler(householdActivityMock, new RefuseSurveyForHouseholdStrategy(householdMock,householdActivityMock));
    }

    @Test
    public void ShouldBeAbleToOpenWhenMenuIdMatches(){
        assertTrue(refusedHandler.shouldOpen(MENU_ID));
    }

    @Test
    public void ShouldNotBeAbleToOpenForDifferentMenuId(){
        assertFalse(refusedHandler.shouldOpen(R.id.action_deferred));
    }

    @Test
    public void ShouldSetProperStatusWhenUserRefusesForSurvey(){
     refusedHandler = new RefusedHandler(householdActivityMock, new RefuseSurveyForHouseholdStrategy(householdMock,householdActivityMock), dialogMock);

     refusedHandler.open();

     verify(dialogMock).confirm(eq(householdActivityMock), any(DialogInterface.OnClickListener.class), eq(CustomDialog.EmptyListener), eq(R.string.survey_refusal_message), eq(R.string.survey_refusal_title));
    }

    @Test
    public void ShouldInactivateWhenMemberIsNotSelected(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.SELECTION_NOT_DONE);

        assertTrue(refusedHandler.shouldDeactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyNotDone(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.NOT_DONE);

        assertFalse(refusedHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyDone(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.DONE);

        assertTrue(refusedHandler.shouldDeactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyDeferred(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.DEFERRED);

        assertFalse(refusedHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyRefused(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.REFUSED);

        assertTrue(refusedHandler.shouldDeactivate());
    }

    @Test
    public void ShouldHideItemWhenInactivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.when(householdActivityMock.findViewById(MENU_ID)).thenReturn(viewMock);

        refusedHandler.deactivate();

        verify(viewMock).setVisibility(View.GONE);
    }

    @Test
    public void ShouldShowItemWhenActivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.when(householdActivityMock.findViewById(MENU_ID)).thenReturn(viewMock);

        refusedHandler.activate();

        verify(viewMock).setVisibility(View.VISIBLE);
    }


}