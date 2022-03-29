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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.util.ReflectionHelpers.setField;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.onaio.steps.R;
import com.onaio.steps.StepsTestRunner;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.handler.exceptions.ExceptionHandler;
import com.onaio.steps.handler.strategies.survey.TakeSurveyForHouseholdStrategy;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.ODKForm.IForm;
import com.onaio.steps.model.ODKForm.ODKSavedForm;
import com.onaio.steps.model.RequestCode;
import com.onaio.steps.utils.Faker;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TakeSurveyHandlerTest extends StepsTestRunner {

    private HouseholdActivity householdActivitySpy;
    private TakeSurveyHandler takeSurveyHandler;
    private Household householdMock;
    private ArrayList<IForm> savedForms;
    private TakeSurveyForHouseholdStrategy takeSurveyForHouseholdStrategy;
    private ExceptionHandler exceptionHandler;

    @Before
    public void Setup(){
        Household household = new Household("1", "john", "123", null, InterviewStatus.NOT_DONE, null, null, "", "");
        Intent intent = new Intent();
        intent.putExtra(Constants.HH_HOUSEHOLD,household);
        savedForms = new ArrayList<>();
        householdActivitySpy = spy(Robolectric.buildActivity(HouseholdActivity.class, intent).create().get());
        householdMock= mock(Household.class);
        exceptionHandler = mock(ExceptionHandler.class);
        takeSurveyForHouseholdStrategy = spy(new TakeSurveyForHouseholdStrategy(householdMock, householdActivitySpy));
        takeSurveyHandler = new TakeSurveyHandler(householdActivitySpy, takeSurveyForHouseholdStrategy);
        setField(takeSurveyHandler, "exceptionHandler", exceptionHandler);
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
        when(householdMock.getStatus()).thenReturn(InterviewStatus.SELECTION_NOT_DONE);

        Assert.assertTrue(takeSurveyHandler.shouldDeactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyNotDone(){
        when(householdMock.getStatus()).thenReturn(InterviewStatus.NOT_DONE);

        Assert.assertFalse(takeSurveyHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyDone(){
        when(householdMock.getStatus()).thenReturn(InterviewStatus.DONE);

        Assert.assertTrue(takeSurveyHandler.shouldDeactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyDeferred(){
        when(householdMock.getStatus()).thenReturn(InterviewStatus.DEFERRED);

        Assert.assertFalse(takeSurveyHandler.shouldDeactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyRefused(){
        when(householdMock.getStatus()).thenReturn(InterviewStatus.REFUSED);

        Assert.assertTrue(takeSurveyHandler.shouldDeactivate());
    }

    @Test
    public void ShouldBeAbleToInactivateView(){
        View viewMock = mock(View.class);
        when(householdActivitySpy.findViewById(R.id.action_take_survey)).thenReturn(viewMock);

        takeSurveyHandler.deactivate();

        Mockito.verify(viewMock).setVisibility(View.INVISIBLE);
    }

    @Test
    public void ShouldBeAbleToActivateView() {
        View viewMock = mock(Button.class);
        when(householdActivitySpy.findViewById(R.id.action_take_survey)).thenReturn(viewMock);

        takeSurveyHandler.activate();

        Mockito.verify(viewMock).setVisibility(View.VISIBLE);
    }

    @Test
    public void ShouldActivateViewWithInterviewNowText() {
        Button viewMock = mock(Button.class);
        when(householdActivitySpy.findViewById(R.id.action_take_survey)).thenReturn(viewMock);
        when(householdMock.getStatus()).thenReturn(InterviewStatus.NOT_DONE);

        takeSurveyHandler.activate();

        Mockito.verify(viewMock).setText(R.string.interview_now);
    }

    @Test
    public void ShouldActivateViewWithContinueInterviewText() {
        Button viewMock = mock(Button.class);
        when(householdActivitySpy.findViewById(R.id.action_take_survey)).thenReturn(viewMock);
        when(householdMock.getStatus()).thenReturn(InterviewStatus.INCOMPLETE);

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
        TakeSurveyHandlerStub surveyHandler = new TakeSurveyHandlerStub(householdActivitySpy, householdMock, savedForms);

        surveyHandler.handleResult(new Intent().setData(mock(Uri.class)), Activity.RESULT_OK);

        Mockito.verify(householdMock,Mockito.never()).update(any(DatabaseHelper.class));
    }

    @Test
    public void ShouldUpdateStatusAsDoneWhenFormHasStatusOfComplete(){
        mockSavedForm(savedForms, Constants.ODK_FORM_COMPLETE_STATUS);
        TakeSurveyHandlerStub surveyHandler = new TakeSurveyHandlerStub(householdActivitySpy, householdMock, savedForms);

        surveyHandler.handleResult(new Intent().setData(mock(Uri.class)), Activity.RESULT_OK);

        Mockito.verify(householdMock).setStatus(InterviewStatus.DONE);
        Mockito.verify(householdMock).update(any(DatabaseHelper.class));
    }

    @Test
    public void ShouldUpdateStatusAsInProgressWhenFormHasStatusOfInComplete(){
        mockSavedForm(savedForms, "incomplete");
        TakeSurveyHandlerStub surveyHandler = new TakeSurveyHandlerStub(householdActivitySpy, householdMock, savedForms);

        surveyHandler.handleResult(new Intent().setData(mock(Uri.class)), Activity.RESULT_OK);

        Mockito.verify(householdMock).setStatus(InterviewStatus.INCOMPLETE);
        Mockito.verify(householdMock).update(any(DatabaseHelper.class));
    }

    private void mockSavedForm(ArrayList<IForm> savedForms, String formStatus) {
        ODKSavedForm savedForm = mock(ODKSavedForm.class);
        savedForms.add(savedForm);
        when(savedForm.getStatus()).thenReturn(formStatus);
    }

    @Test
    public void ShouldNotHandleResultForErrorResult(){
        takeSurveyHandler.handleResult(null, Activity.RESULT_CANCELED);

        Mockito.verify(householdMock,Mockito.never()).setStatus(InterviewStatus.DONE);
        Mockito.verify(householdMock,Mockito.never()).update(any(DatabaseHelper.class));
    }

    @Test
    public void testOpenShouldCallStrategyOpenMethodAndHandleException() throws IOException {
        doNothing().when(takeSurveyForHouseholdStrategy).open();
        takeSurveyHandler.open();

        verify(takeSurveyForHouseholdStrategy, times(1)).open();

        // handle exception
        doThrow(NullPointerException.class).when(takeSurveyForHouseholdStrategy).open();
        takeSurveyHandler.open();

        ArgumentCaptor<Exception> arException = ArgumentCaptor.forClass(Exception.class);
        verify(exceptionHandler, times(1)).handle(arException.capture());

        assertEquals(NullPointerException.class.getName(), arException.getValue().getClass().getName());
    }

    @Test
    public void testGetSavedFormsShouldReturnSavedFormAndHandleException() throws RemoteException {
        Faker.findODKSavedForm(householdActivitySpy);

        Intent intent = mock(Intent.class);
        Uri uri = mock(Uri.class);

        when(intent.getData()).thenReturn(uri);
        when(uri.getLastPathSegment()).thenReturn("");

        List<IForm> savedForms = takeSurveyHandler.getSavedForms(intent);
        assertFalse(savedForms.isEmpty());

        ODKSavedForm savedForm = (ODKSavedForm) savedForms.get(0);
        assertEquals("id", savedForm.getId());
        assertEquals("jrFormId", savedForm.getJrFormId());
        assertEquals("displayName", savedForm.getDisplayName());
        assertEquals("complete", savedForm.getStatus());

        // handle exception
        ContentResolver contentResolver = householdActivitySpy.getContentResolver();
        when(contentResolver.acquireContentProviderClient(any(Uri.class))).thenReturn(null);

        assertNull(takeSurveyHandler.getSavedForms(intent));
    }

    @Test
    public void testTryToResolveShouldCallOpenMethod() throws IOException {
        doNothing().when(takeSurveyForHouseholdStrategy).open();
        takeSurveyHandler.tryToResolve();

        verify(takeSurveyForHouseholdStrategy, times(1)).open();
    }

    class TakeSurveyHandlerStub extends TakeSurveyHandler{

        private List<IForm> savedForms;

        public TakeSurveyHandlerStub(AppCompatActivity activity, Household household, List<IForm> savedForms) {
            super(activity, new TakeSurveyForHouseholdStrategy(household,activity));
            this.savedForms = savedForms;
        }

        @Override
        public List<IForm> getSavedForms(Intent data) {
            return savedForms;
        }
    }

}