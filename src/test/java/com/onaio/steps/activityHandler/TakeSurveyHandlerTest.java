package com.onaio.steps.activityHandler;

import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.model.Household;

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

    @Before
    public void Setup(){
        householdActivityMock = Mockito.mock(HouseholdActivity.class);
        Household householdMock = Mockito.mock(Household.class);
        takeSurveyHandler = new TakeSurveyHandler(householdActivityMock, householdMock);
    }

    @Test
    public void ShouldCheckWhetherActivityCanBeStartedForProperMenuId(){
        assertTrue(takeSurveyHandler.shouldOpen(R.id.action_take_survey));
    }

    @Test
    public void ShouldCheckWhetherActivityCanBeStartedForOtherMenuId(){
        assertFalse(takeSurveyHandler.shouldOpen(R.id.action_deferred));
    }


    @Test
    public void ShouldCheckWhetherActivityCanBeInactivated(){
        assertTrue(takeSurveyHandler.shouldInactivate());
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
}