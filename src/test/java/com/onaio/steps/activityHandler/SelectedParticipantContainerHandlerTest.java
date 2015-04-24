package com.onaio.steps.activityHandler;

import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SelectedParticipantContainerHandlerTest {

    private Household householdMock;
    private HouseholdActivity householdActivity;
    private SelectedParticipantContainerHandler selectedParticipantContainerHandler;

    @Before
    public void setup(){
        householdMock = Mockito.mock(Household.class);
        householdActivity = Mockito.mock(HouseholdActivity.class);
        selectedParticipantContainerHandler = new SelectedParticipantContainerHandler(householdActivity, householdMock);
    }

    @Test
    public void ShouldNotInactivateWhenSurveyNotDone(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.NOT_DONE);

        assertFalse(selectedParticipantContainerHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenMemberIsNotSelected(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.NOT_SELECTED);

        assertTrue(selectedParticipantContainerHandler.shouldInactivate());
    }

    @Test
    public void ShouldActivateWhenHouseholdStatusIsIncomplete(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.INCOMPLETE);

        assertFalse(selectedParticipantContainerHandler.shouldInactivate());
    }

    @Test
    public void ShouldActivateWhenSurveyIsDeferred(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.DEFERRED);

        assertFalse(selectedParticipantContainerHandler.shouldInactivate());
    }

    @Test
    public void ShouldActivateWhenSurveyIsRefused(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.REFUSED);

        assertTrue(selectedParticipantContainerHandler.shouldInactivate());
    }

    @Test
    public void ShouldHideItemWhenInactivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(householdActivity.findViewById(R.id.selected_participant)).toReturn(viewMock);

        selectedParticipantContainerHandler.inactivate();

        verify(viewMock).setVisibility(View.GONE);
    }

    @Test
    public void ShouldShowItemWhenActivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(householdActivity.findViewById(R.id.selected_participant)).toReturn(viewMock);

       selectedParticipantContainerHandler.activate();

        verify(viewMock).setVisibility(View.VISIBLE);
    }



}