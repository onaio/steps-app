package com.onaio.steps.activityHandler;

import android.app.Activity;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.RequestCode;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class TakeSurveyHandlerTest {

    private HouseholdActivity householdActivityMock;
    private TakeSurveyHandler takeSurveyHandler;
    private Household householdMock;

    @Before
    public void Setup(){
        householdActivityMock = Mockito.mock(HouseholdActivity.class);
        householdMock= Mockito.mock(Household.class);
        takeSurveyHandler = new TakeSurveyHandler(householdActivityMock, householdMock);
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
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.NOT_SELECTED);

        Assert.assertTrue(takeSurveyHandler.shouldInactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyNotDone(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.NOT_DONE);

        Assert.assertFalse(takeSurveyHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyDone(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.DONE);

        Assert.assertTrue(takeSurveyHandler.shouldInactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyDeferred(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.DEFERRED);

        Assert.assertFalse(takeSurveyHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyRefused(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.REFUSED);

        Assert.assertTrue(takeSurveyHandler.shouldInactivate());
    }

    @Test
    public void ShouldBeAbleToInactivateView(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(householdActivityMock.findViewById(R.id.action_take_survey)).toReturn(viewMock);

        takeSurveyHandler.inactivate();

        Mockito.verify(viewMock).setVisibility(View.INVISIBLE);
    }

    @Test
    public void ShouldBeAbleToActivateView() {
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(householdActivityMock.findViewById(R.id.action_take_survey)).toReturn(viewMock);

        takeSurveyHandler.activate();

        Mockito.verify(viewMock).setVisibility(View.VISIBLE);
    }

    @Test
    public void ShouldBeAbleToHandleResultForProperRequestCode(){
        assertTrue(takeSurveyHandler.canHandleResult(RequestCode.SURVEY.getCode()));
    }

    @Test
    public void ShouldNotBeAbleToHandleResultForOtherRequestCode(){
        assertFalse(takeSurveyHandler.canHandleResult(RequestCode.NEW_HOUSEHOLD.getCode()));
    }

//    @Test
//    public void ShouldHandleResultForResultOk(){
//        takeSurveyHandler.handleResult(null, Activity.RESULT_OK);
//
//        Mockito.verify(householdMock).setStatus(HouseholdStatus.DONE);
//        Mockito.verify(householdMock).update(Mockito.any(DatabaseHelper.class));
//    }

    @Test
    public void ShouldNotHandleResultForErrorResult(){
        takeSurveyHandler.handleResult(null, Activity.RESULT_CANCELED);

        Mockito.verify(householdMock,Mockito.never()).setStatus(HouseholdStatus.DONE);
        Mockito.verify(householdMock,Mockito.never()).update(Mockito.any(DatabaseHelper.class));
    }

}