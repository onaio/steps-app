package com.onaio.steps.activityHandler;

import android.app.ListActivity;
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
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;


@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SelectParticipantHandlerTest {
    private final int MENU_ID = R.id.action_select_participant;
    @Mock
    private HouseholdActivity activityMock;
    @Mock
    private Household householdMock;
    private SelectParticipantHandler selectParticipantHandler;
    private DatabaseHelper dbMock;
    private Dialog dialogMock;
    private View viewMock;

    @Before
    public void Setup(){
        activityMock = mock(HouseholdActivity.class);
        householdMock = mock(Household.class);
        dbMock = mock(DatabaseHelper.class);
        dialogMock = mock(Dialog.class);

        viewMock = Mockito.mock(View.class);
        selectParticipantHandler = new SelectParticipantHandlerMock(activityMock, householdMock, dialogMock, dbMock, viewMock);
    }

    @Test
    public void ShouldBeAbleToOpenWhenMenuIdMatches(){
        assertTrue(selectParticipantHandler.shouldOpen(MENU_ID));
    }

    @Test
    public void ShouldNotBeAbleToOpenForDifferentMenuId(){
        assertFalse(selectParticipantHandler.shouldOpen(R.id.action_refused));
    }

    @Test
    public void ShouldConfirmWhenSurveyIsNotDone(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.NOT_DONE);

        selectParticipantHandler.open();

        verify(dialogMock).confirm(eq(activityMock),any(DialogInterface.OnClickListener.class),eq(Dialog.EmptyListener),any(View.class),eq(R.string.participant_re_elect_reason_title));
    }

    @Test
    public void ShouldConfirmWhenSurveyIsDeferred(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.DEFERRED);

        selectParticipantHandler.open();

        verify(dialogMock).confirm(eq(activityMock),any(DialogInterface.OnClickListener.class),eq(Dialog.EmptyListener),any(View.class),eq(R.string.participant_re_elect_reason_title));
    }

    private class SelectParticipantHandlerMock extends SelectParticipantHandler{
        private View view;

        public SelectParticipantHandlerMock(ListActivity activity, Household household, Dialog dialog, DatabaseHelper db, View view) {
            super(activity, household, dialog, db);
            this.view = view;
        }

        @Override
        protected View getView() {
            return view;
        }
    }

}