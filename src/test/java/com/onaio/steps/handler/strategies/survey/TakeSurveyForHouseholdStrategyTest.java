package com.onaio.steps.handler.strategies.survey;

import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.helper.KeyValueStore;
import com.onaio.steps.helper.KeyValueStoreFactory;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;
import com.onaio.steps.model.ODKForm.ODKSavedForm;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class TakeSurveyForHouseholdStrategyTest {

    private Household household;
    private HouseholdActivity householdActivity;
    private TakeSurveyForHouseholdStrategy takeSurveyForHouseholdStrategy;

    @Before
    public void Setup(){
        household = Mockito.mock(Household.class);
        householdActivity = Mockito.mock(HouseholdActivity.class);
        takeSurveyForHouseholdStrategy = new TakeSurveyForHouseholdStrategy(household, householdActivity);
    }

    @Test
    public void ShouldNotInactivateWhenMemberIsSelected(){
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.NOT_DONE);
        assertFalse(takeSurveyForHouseholdStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsDone(){
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.DONE);
        assertTrue(takeSurveyForHouseholdStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsDeferred(){
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.DEFERRED);
        assertFalse(takeSurveyForHouseholdStrategy.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsIncomplete(){
        Mockito.stub(household.getStatus()).toReturn(InterviewStatus.INCOMPLETE);
        assertFalse(takeSurveyForHouseholdStrategy.shouldInactivate());
    }


    @Test
    public void ShouldHandleResultForSurveyCompleted(){
        ODKSavedForm odkSavedForm = Mockito.mock(ODKSavedForm.class);
        Mockito.stub(odkSavedForm.getStatus()).toReturn(Constants.ODK_FORM_COMPLETE_STATUS);
        takeSurveyForHouseholdStrategy.handleResult(odkSavedForm);
        Mockito.verify(household).setStatus(InterviewStatus.DONE);
        Mockito.verify(household).update(Mockito.any(DatabaseHelper.class));
    }

    @Test
    public void ShouldGetNameFormat(){
        String formNameFormat = "STEPS_Instrument_V3_1-%s";
        Mockito.stub(household.getName()).toReturn("123-100");
        assertEquals("STEPS_Instrument_V3_1-123-100",takeSurveyForHouseholdStrategy.getFormName(formNameFormat));

    }

}