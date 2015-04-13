package com.onaio.steps.activityHandler;

import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.helper.Dialog;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SelectedParticipantContainerHandlerTest {

    private final int MENU_ID = R.id.selected_participant;
    @Mock
    private HouseholdActivity activityMock;
    @Mock
    private Household householdMock;
    @Mock
    private Dialog dialogMock;
    private SelectedParticipantContainerHandler selectedParticipantContainerHandler;

    @Before
    public void Setup(){
        activityMock = mock(HouseholdActivity.class);
        householdMock = mock(Household.class);
        dialogMock = mock(Dialog.class);
        selectedParticipantContainerHandler = new SelectedParticipantContainerHandler(activityMock, householdMock);
    }

    @Test
    public void ShouldInactivateWhenMemberIsNotSelected(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.NOT_SELECTED);

        assertTrue(selectedParticipantContainerHandler.shouldInactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyNotDone(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.NOT_DONE);

        assertFalse(selectedParticipantContainerHandler.shouldInactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyDone(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.DONE);

        assertTrue(selectedParticipantContainerHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyDeferred(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.DEFERRED);

        assertFalse(selectedParticipantContainerHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyRefused(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.REFUSED);

        assertTrue(selectedParticipantContainerHandler.shouldInactivate());
    }

    @Test
    public void ShouldHideItemWhenInactivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(activityMock.findViewById(MENU_ID)).toReturn(viewMock);

        selectedParticipantContainerHandler.inactivate();

        verify(viewMock).setVisibility(View.INVISIBLE);
    }

    @Test
    public void ShouldShowItemWhenActivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(activityMock.findViewById(MENU_ID)).toReturn(viewMock);

        selectedParticipantContainerHandler.activate();

        verify(viewMock).setVisibility(View.VISIBLE);
    }
}