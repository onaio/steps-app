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
public class RefuseSurveyForHouseholdStrategyTest {

    private Household household;
    private HouseholdActivity householdActivity;
    private RefuseSurveyForHouseholdStrategy refuseSurveyForHouseholdStrategy;

    @Before
    public void Setup(){
        household = Mockito.mock(Household.class);
        householdActivity = Mockito.mock(HouseholdActivity.class);
        refuseSurveyForHouseholdStrategy = new RefuseSurveyForHouseholdStrategy(household, householdActivity);
    }

    @Test
    public void ShouldNotInactivateWhenMemberIsSelected(){
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.NOT_DONE);
        assertFalse(refuseSurveyForHouseholdStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsDone(){
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.DONE);
        assertTrue(refuseSurveyForHouseholdStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsDeferred(){
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.DEFERRED);
        assertFalse(refuseSurveyForHouseholdStrategy.shouldInactivate());
    }

    @Test
    public void ShouldOpenAndUpdateStatus(){
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.DEFERRED);
        refuseSurveyForHouseholdStrategy.open();
        Mockito.verify(household).setStatus(InterviewStatus.REFUSED);
        Mockito.verify(household).update(Mockito.any(DatabaseHelper.class));
    }

}