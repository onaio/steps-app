package com.onaio.steps.activityHandler;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.onaio.steps.R;
import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.CustomDialog;
import com.onaio.steps.helper.DatabaseHelper;
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
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;


@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SelectParticipantHandlerTest {

    @Mock
    private Household householdMock;
    private SelectParticipantHandler selectParticipantHandler;
    private DatabaseHelper dbMock;
    private CustomDialog dialogMock;
    private HouseholdActivity householdActivity;
    @Mock
    private android.app.Dialog androidDialogMock;

    @Before
    public void Setup(){
        householdMock = mock(Household.class);
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.NOT_SELECTED);
        Intent intent = new Intent();
        intent.putExtra(Constants.HOUSEHOLD,householdMock);
        householdActivity = Robolectric.buildActivity(HouseholdActivity.class).withIntent(intent).create().get();
        dbMock = mock(DatabaseHelper.class);
        dialogMock = mock(CustomDialog.class);

        androidDialogMock = Mockito.mock(android.app.Dialog.class);
        selectParticipantHandler = new SelectParticipantHandlerMock(householdActivity, householdMock, dialogMock, dbMock, androidDialogMock,Mockito.mock(View.class));
    }

    @Test
    public void ShouldBeAbleToOpenWhenMenuIdMatches(){
        int MENU_ID = R.id.action_select_participant;
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

        verify(dialogMock).confirm(eq(householdActivity),any(DialogInterface.OnClickListener.class),eq(CustomDialog.EmptyListener),any(View.class),eq(R.string.participant_re_elect_reason_title));
    }

    @Test
    public void ShouldConfirmWhenSurveyIsDeferred(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.DEFERRED);

        selectParticipantHandler.open();

        verify(dialogMock).confirm(eq(householdActivity),any(DialogInterface.OnClickListener.class),eq(CustomDialog.EmptyListener),any(View.class),eq(R.string.participant_re_elect_reason_title));
    }

    @Test
    public void ShouldNotifyUserBeforeSelection(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.NOT_SELECTED);
        Button buttonMock = Mockito.mock(Button.class);
        stub(androidDialogMock.findViewById(R.id.confirm)).toReturn(buttonMock);
        stub(androidDialogMock.findViewById(R.id.cancel)).toReturn(buttonMock);

        selectParticipantHandler.open();

        verify(androidDialogMock).show();
        verify(androidDialogMock).setCancelable(true);
        verify(androidDialogMock).requestWindowFeature(Window.FEATURE_NO_TITLE);
        verify(androidDialogMock).setContentView(R.layout.select_participant_dialog);
    }


    private class SelectParticipantHandlerMock extends SelectParticipantHandler{
        private View view;

        public SelectParticipantHandlerMock(ListActivity activity, Household household, CustomDialog dialog, DatabaseHelper db, android.app.Dialog androidDialog, View view) {
            super(activity, household, dialog, db, androidDialog);
            this.view = view;
        }

        @Override
        protected View getView() {
            return view;
        }
    }

}