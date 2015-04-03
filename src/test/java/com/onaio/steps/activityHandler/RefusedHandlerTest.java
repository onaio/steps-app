package com.onaio.steps.activityHandler;

import android.app.AlertDialog;
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
public class RefusedHandlerTest {

    private HouseholdActivity householdActivityMock;
    private Household householdMock;
    private RefusedHandler refusedHandler;
    private  AlertDialog.Builder alertDialogMock;
    private int MENU_ID = R.id.action_refused;


    @Before
    public void setup(){
       householdActivityMock = Mockito.mock(HouseholdActivity.class);
       householdMock = Mockito.mock(Household.class);
       alertDialogMock= Mockito.mock(AlertDialog.Builder.class);
       refusedHandler = new RefusedHandler(householdActivityMock, householdMock);
    }

    @Test
    public void ShouldBeAbleToOpenWhenMenuIdMatches(){
        assertTrue(refusedHandler.shouldOpen(MENU_ID));
    }

    @Test
    public void ShouldNotBeAbleToOpenForDifferentMenuId(){
        assertFalse(refusedHandler.shouldOpen(R.id.action_deferred));
    }

    @Test
    public void ShouldSetProperStatusWhenUserRefusesForSurvey(){
    /*  refusedHandler = new RefusedHandler(householdActivityMock, householdMock,alertDialogMock);

        Not able to simulate positive and negative button click on AlertDialog

        verify(householdMock).setStatus(HouseholdStatus.REFUSED);
        verify(householdMock).update(any(DatabaseHelper.class));*/
    }

    @Test
    public void ShouldInactivateWhenMemberIsNotSelected(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.NOT_SELECTED);

        assertTrue(refusedHandler.shouldInactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyNotDone(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.NOT_DONE);

        assertFalse(refusedHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyDone(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.DONE);

        assertTrue(refusedHandler.shouldInactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyDeferred(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.DEFERRED);

        assertFalse(refusedHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyRefused(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.REFUSED);

        assertTrue(refusedHandler.shouldInactivate());
    }

    @Test
    public void ShouldHideItemWhenInactivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(householdActivityMock.findViewById(MENU_ID)).toReturn(viewMock);

        refusedHandler.inactivate();

        verify(viewMock).setVisibility(View.INVISIBLE);
    }

    @Test
    public void ShouldShowItemWhenActivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(householdActivityMock.findViewById(MENU_ID)).toReturn(viewMock);

        refusedHandler.activate();

        verify(viewMock).setVisibility(View.VISIBLE);
    }


}