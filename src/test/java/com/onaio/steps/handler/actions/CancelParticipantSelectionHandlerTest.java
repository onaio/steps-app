package com.onaio.steps.handler.actions;

import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activities.HouseholdActivity;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class CancelParticipantSelectionHandlerTest {

    private final int MENU_ID = R.id.action_cancel_participant;
    @Mock
    private HouseholdActivity activityMock;
    @Mock
    private Household householdMock;
    @Mock
    private CancelParticipantSelectionHandler selectionHandler;

    @Before
    public void Setup(){
        activityMock = mock(HouseholdActivity.class);
        householdMock = mock(Household.class);
        selectionHandler = new CancelParticipantSelectionHandler(activityMock, householdMock);
    }

    @Test
    public void ShouldBeAbleToOpenWhenMenuIdMatches(){
        assertTrue(selectionHandler.shouldOpen(MENU_ID));
    }

    @Test
    public void ShouldNotBeAbleToOpenForDifferentMenuId(){
        assertFalse(selectionHandler.shouldOpen(R.id.action_refused));
    }

    @Test
    public void ShouldInactivateWhenMemberIsNotSelected(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.NOT_SELECTED);

        assertTrue(selectionHandler.shouldInactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyNotDone(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.NOT_DONE);

        assertFalse(selectionHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyDone(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.DONE);

        assertTrue(selectionHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyDeferred(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.DEFERRED);

        assertTrue(selectionHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyRefused(){
        Mockito.stub(householdMock.getStatus()).toReturn(InterviewStatus.REFUSED);

        assertTrue(selectionHandler.shouldInactivate());
    }


    @Test
    public void ShouldHideItemWhenInactivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(activityMock.findViewById(MENU_ID)).toReturn(viewMock);

        selectionHandler.inactivate();

        verify(viewMock).setVisibility(View.GONE);
    }

    @Test
    public void ShouldShowItemWhenActivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(activityMock.findViewById(MENU_ID)).toReturn(viewMock);

        selectionHandler.activate();

        verify(viewMock).setVisibility(View.VISIBLE);
    }


}