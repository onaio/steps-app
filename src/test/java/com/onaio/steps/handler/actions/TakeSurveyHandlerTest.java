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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.handler.strategies.survey.TakeSurveyForHouseholdStrategy;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.ODKForm.IForm;
import com.onaio.steps.model.ODKForm.ODKSavedForm;
import com.onaio.steps.model.RequestCode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class TakeSurveyHandlerTest extends StepsTestRunner {

    private HouseholdActivity householdActivityMock;
    private TakeSurveyHandler takeSurveyHandler;
    private Household householdMock;
    private ArrayList<IForm> savedForms;

    @Before
    public void Setup(){
        savedForms = new ArrayList<IForm>();
        householdActivityMock = Mockito.mock(HouseholdActivity.class);
        householdMock= Mockito.mock(Household.class);
        takeSurveyHandler = new TakeSurveyHandler(householdActivityMock, new TakeSurveyForHouseholdStrategy(householdMock,householdActivityMock));
    }

    @Test
    public void ShouldOpenActivityForProperMenuId(){
        assertTrue(takeSurveyHandler.shouldOpen(R.id.action_take_survey));
    }

    @Test
    public void ShouldNotOpenForOtherMenuId(){
        assertFalse(takeSurveyHandler.shouldOpen(R.id.action_deferred));
    }

    @Test
    public void ShouldInactivateWhenMemberIsNotSelected(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.SELECTION_NOT_DONE);

        Assert.assertTrue(takeSurveyHandler.shouldDeactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyNotDone(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.NOT_DONE);

        Assert.assertFalse(takeSurveyHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyDone(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.DONE);

        Assert.assertTrue(takeSurveyHandler.shouldDeactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyDeferred(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.DEFERRED);

        Assert.assertFalse(takeSurveyHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyRefused(){
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.REFUSED);

        Assert.assertTrue(takeSurveyHandler.shouldDeactivate());
    }

    @Test
    public void ShouldBeAbleToInactivateView(){
        View viewMock = Mockito.mock(View.class);
        Mockito.when(householdActivityMock.findViewById(R.id.action_take_survey)).thenReturn(viewMock);

        takeSurveyHandler.deactivate();

        Mockito.verify(viewMock).setVisibility(View.INVISIBLE);
    }

    @Test
    public void ShouldBeAbleToActivateView() {
        View viewMock = Mockito.mock(Button.class);
        Mockito.when(householdActivityMock.findViewById(R.id.action_take_survey)).thenReturn(viewMock);

        takeSurveyHandler.activate();

        Mockito.verify(viewMock).setVisibility(View.VISIBLE);
    }

    @Test
    public void ShouldActivateViewWithInterviewNowText() {
        Button viewMock = Mockito.mock(Button.class);
        Mockito.when(householdActivityMock.findViewById(R.id.action_take_survey)).thenReturn(viewMock);
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.NOT_DONE);

        takeSurveyHandler.activate();

        Mockito.verify(viewMock).setText(R.string.interview_now);
    }

    @Test
    public void ShouldActivateViewWithContinueInterviewText() {
        Button viewMock = Mockito.mock(Button.class);
        Mockito.when(householdActivityMock.findViewById(R.id.action_take_survey)).thenReturn(viewMock);
        Mockito.when(householdMock.getStatus()).thenReturn(InterviewStatus.INCOMPLETE);

        takeSurveyHandler.activate();

        Mockito.verify(viewMock).setText(R.string.continue_interview);
    }

    @Test
    public void ShouldBeAbleToHandleResultForProperRequestCode(){
        assertTrue(takeSurveyHandler.canHandleResult(RequestCode.SURVEY.getCode()));
    }

    @Test
    public void ShouldNotBeAbleToHandleResultForOtherRequestCode(){
        assertFalse(takeSurveyHandler.canHandleResult(RequestCode.NEW_HOUSEHOLD.getCode()));
    }

    @Test
    public void ShouldDoNothingIfNoSavedFormIsFound(){
        TakeSurveyHandlerStub surveyHandler = new TakeSurveyHandlerStub(householdActivityMock, householdMock, savedForms);

        surveyHandler.handleResult(new Intent().setData(Mockito.mock(Uri.class)), Activity.RESULT_OK);

        Mockito.verify(householdMock,Mockito.never()).update(Mockito.any(DatabaseHelper.class));
    }

    @Test
    public void ShouldUpdateStatusAsDoneWhenFormHasStatusOfComplete(){
        mockSavedForm(savedForms, Constants.ODK_FORM_COMPLETE_STATUS);
        TakeSurveyHandlerStub surveyHandler = new TakeSurveyHandlerStub(householdActivityMock, householdMock, savedForms);

        surveyHandler.handleResult(new Intent().setData(Mockito.mock(Uri.class)), Activity.RESULT_OK);

        Mockito.verify(householdMock).setStatus(InterviewStatus.DONE);
        Mockito.verify(householdMock).update(Mockito.any(DatabaseHelper.class));
    }

    @Test
    public void ShouldUpdateStatusAsInProgressWhenFormHasStatusOfInComplete(){
        mockSavedForm(savedForms, "incomplete");
        TakeSurveyHandlerStub surveyHandler = new TakeSurveyHandlerStub(householdActivityMock, householdMock, savedForms);

        surveyHandler.handleResult(new Intent().setData(Mockito.mock(Uri.class)), Activity.RESULT_OK);

        Mockito.verify(householdMock).setStatus(InterviewStatus.INCOMPLETE);
        Mockito.verify(householdMock).update(Mockito.any(DatabaseHelper.class));
    }

    private void mockSavedForm(ArrayList<IForm> savedForms, String formStatus) {
        ODKSavedForm savedForm = Mockito.mock(ODKSavedForm.class);
        savedForms.add(savedForm);
        Mockito.when(savedForm.getStatus()).thenReturn(formStatus);
    }

    @Test
    public void ShouldNotHandleResultForErrorResult(){
        takeSurveyHandler.handleResult(null, Activity.RESULT_CANCELED);

        Mockito.verify(householdMock,Mockito.never()).setStatus(InterviewStatus.DONE);
        Mockito.verify(householdMock,Mockito.never()).update(Mockito.any(DatabaseHelper.class));
    }

    class TakeSurveyHandlerStub extends TakeSurveyHandler{

        private List<IForm> savedForms;

        public TakeSurveyHandlerStub(AppCompatActivity activity, Household household, List<IForm> savedForms) {
            super(activity, new TakeSurveyForHouseholdStrategy(household,activity));
            this.savedForms = savedForms;
        }

        @Override
        protected List<IForm> getSavedForms(Intent data) {
            return savedForms;
        }
    }

}