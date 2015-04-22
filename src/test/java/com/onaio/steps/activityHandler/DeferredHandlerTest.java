package com.onaio.steps.activityHandler;

import android.content.DialogInterface;
import android.view.View;

import com.onaio.steps.R;
import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.helper.DatabaseHelper;
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

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class DeferredHandlerTest {

    private final int MENU_ID = R.id.action_deferred;
    @Mock
    private HouseholdActivity activityMock;
    @Mock
    private Household householdMock;
    @Mock
    private Dialog dialogMock;
    private DeferredHandler deferredHandler;

    @Before
    public void Setup(){
        activityMock = mock(HouseholdActivity.class);
        householdMock = mock(Household.class);
        dialogMock = mock(Dialog.class);
        deferredHandler = new DeferredHandler(activityMock, householdMock,dialogMock);
    }

    @Test
    public void ShouldBeAbleToOpenWhenMenuIdMatches(){
        assertTrue(deferredHandler.shouldOpen(MENU_ID));
    }

    @Test
    public void ShouldNotBeAbleToOpenForDifferentMenuId(){
        assertFalse(deferredHandler.shouldOpen(R.id.action_refused));
    }

    @Test
    public void ShouldSetProperStatusAndNotifyUserWhenOpened(){
        deferredHandler.open();

        verify(householdMock).setStatus(HouseholdStatus.DEFERRED);
        verify(householdMock).update(any(DatabaseHelper.class));
        verify(dialogMock).notify(eq(activityMock),any(DialogInterface.OnClickListener.class), eq(R.string.survey_deferred_title), eq(R.string.survey_deferred_message));
    }

    @Test
    public void ShouldInactivateWhenMemberIsNotSelected(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.NOT_SELECTED);

        assertTrue(deferredHandler.shouldInactivate());
    }

    @Test
    public void ShouldNotInactivateWhenSurveyNotDone(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.NOT_DONE);

        assertFalse(deferredHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyDone(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.DONE);

        assertTrue(deferredHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyDeferred(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.DEFERRED);

        assertTrue(deferredHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyRefused(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.REFUSED);

        assertTrue(deferredHandler.shouldInactivate());
    }

    @Test
    public void ShouldHideItemWhenInactivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(activityMock.findViewById(MENU_ID)).toReturn(viewMock);

        deferredHandler.inactivate();

        verify(viewMock).setVisibility(View.GONE);
    }

    @Test
    public void ShouldShowItemWhenActivated(){
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(activityMock.findViewById(MENU_ID)).toReturn(viewMock);

        deferredHandler.activate();

        verify(viewMock).setVisibility(View.VISIBLE);
    }



}