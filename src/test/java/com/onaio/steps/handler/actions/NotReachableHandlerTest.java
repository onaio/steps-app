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

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import android.content.DialogInterface;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.handler.strategies.survey.NotReachableSurveyForHouseholdStrategy;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class NotReachableHandlerTest {

    private HouseholdActivity householdActivityMock;
    private Household householdMock;
    private NotReachableHandler notReachableHandler;
    private CustomDialog dialogMock;
    private int MENU_ID = R.id.action_not_reachable;


    @Before
    public void setup(){
       householdActivityMock = Mockito.mock(HouseholdActivity.class);
       householdMock = Mockito.mock(Household.class);
       dialogMock = Mockito.mock(CustomDialog.class);
       notReachableHandler = new NotReachableHandler(householdActivityMock, new NotReachableSurveyForHouseholdStrategy(householdMock,householdActivityMock));
    }

    @Test
    public void ShouldBeAbleToOpenWhenMenuIdMatches(){
        assertTrue(notReachableHandler.shouldOpen(MENU_ID));
    }

    @Test
    public void ShouldNotBeAbleToOpenForDifferentMenuId(){
        assertFalse(notReachableHandler.shouldOpen(R.id.action_deferred));
    }

    @Test
    public void ShouldSetProperStatusWhenUserRefusesForSurvey(){
     notReachableHandler = new NotReachableHandler(householdActivityMock, new NotReachableSurveyForHouseholdStrategy(householdMock,householdActivityMock), dialogMock);

     notReachableHandler.open();

     verify(dialogMock).confirm(eq(householdActivityMock), any(DialogInterface.OnClickListener.class), eq(CustomDialog.EmptyListener), eq(R.string.survey_not_reachable_message), eq(R.string.survey_not_reachable_title));
    }

    @Test
    public void ShouldInactivateWhenMemberIsNotSelected(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.SELECTION_NOT_DONE);

        assertTrue(notReachableHandler.shouldDeactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyNotDone(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.NOT_DONE);

        assertFalse(notReachableHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyDone(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.DONE);

        assertTrue(notReachableHandler.shouldDeactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyDeferred(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.DEFERRED);

        assertFalse(notReachableHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyNotReachable(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.NOT_REACHABLE);

        assertTrue(notReachableHandler.shouldDeactivate());
    }

    @Test
    public void ShouldHideItemWhenInactivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(householdActivityMock.findViewById(MENU_ID)).toReturn(viewMock);

        notReachableHandler.deactivate();

        verify(viewMock).setVisibility(View.GONE);
    }

    @Test
    public void ShouldShowItemWhenActivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(householdActivityMock.findViewById(MENU_ID)).toReturn(viewMock);

        notReachableHandler.activate();

        verify(viewMock).setVisibility(View.VISIBLE);
    }


}