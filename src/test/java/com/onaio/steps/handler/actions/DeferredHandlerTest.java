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

import android.content.DialogInterface;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.handler.strategies.survey.DeferSurveyForHouseholdStrategy;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class DeferredHandlerTest {

    private final int MENU_ID = R.id.action_deferred;
    @Mock
    private HouseholdActivity activityMock;
    @Mock
    private Household householdMock;
    @Mock
    private CustomDialog dialogMock;
    private DeferredHandler deferredHandler;

    @Before
    public void Setup(){
        activityMock = mock(HouseholdActivity.class);
        householdMock = mock(Household.class);
        dialogMock = mock(CustomDialog.class);
        deferredHandler = new DeferredHandler(activityMock, new DeferSurveyForHouseholdStrategy(householdMock,activityMock),dialogMock);
    }

    @Test
    public void ShouldBeAbleToOpenWhenMenuIdMatches(){
        assertTrue(deferredHandler.shouldOpen(MENU_ID));
    }

    @Test
    public void ShouldNotBeAbleToOpenForDifferentMenuId(){
        assertFalse(deferredHandler.shouldOpen(R.id.action_refused));
    }

    @Test
    public void ShouldSetProperStatusAndNotifyUserWhenOpened(){
        deferredHandler.open();

        verify(householdMock).setStatus(InterviewStatus.DEFERRED);
        verify(householdMock).update(any(DatabaseHelper.class));
        verify(dialogMock).notify(eq(activityMock),any(DialogInterface.OnClickListener.class), eq(R.string.survey_deferred_title), eq(R.string.survey_deferred_message));
    }

    @Test
    public void ShouldInactivateWhenMemberIsNotSelected(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.NOT_SELECTED);

        assertTrue(deferredHandler.shouldInactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyNotDone(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.NOT_DONE);

        assertFalse(deferredHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyDone(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.DONE);

        assertTrue(deferredHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyDeferred(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.DEFERRED);

        assertTrue(deferredHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyRefused(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.REFUSED);

        assertTrue(deferredHandler.shouldInactivate());
    }

    @Test
    public void ShouldHideItemWhenInactivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(activityMock.findViewById(MENU_ID)).toReturn(viewMock);

        deferredHandler.inactivate();

        verify(viewMock).setVisibility(View.GONE);
    }

    @Test
    public void ShouldShowItemWhenActivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(activityMock.findViewById(MENU_ID)).toReturn(viewMock);

        deferredHandler.activate();

        verify(viewMock).setVisibility(View.VISIBLE);
    }



}