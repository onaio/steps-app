package com.onaio.steps.handler.strategies.survey;

import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

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
public class DeferSurveyForHouseholdStrategyTest {


    private Household household;
    private HouseholdActivity householdActivity;
    private DeferSurveyForHouseholdStrategy deferSurveyForHouseholdStrategy;

    @Before
    public void Setup(){
        household = Mockito.mock(Household.class);
        householdActivity = Mockito.mock(HouseholdActivity.class);
        deferSurveyForHouseholdStrategy = new DeferSurveyForHouseholdStrategy(household, householdActivity);
    }

    @Test
    public void ShouldNotInactivateWhenSurveyIsDone(){
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.NOT_DONE);
        assertFalse(deferSurveyForHouseholdStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsDone(){
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.DONE);
        assertTrue(deferSurveyForHouseholdStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsRefused(){
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.REFUSED);
        assertTrue(deferSurveyForHouseholdStrategy.shouldInactivate());
    }

    @Test
    public void ShouldOpenAndUpdateStatus(){
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.REFUSED);
        deferSurveyForHouseholdStrategy.open();
        Mockito.verify(household).setStatus(InterviewStatus.DEFERRED);
        Mockito.verify(household).update(Mockito.any(DatabaseHelper.class));
    }




}