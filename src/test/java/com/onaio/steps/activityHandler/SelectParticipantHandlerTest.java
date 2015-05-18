package com.onaio.steps.activityHandler;

import android.app.ListActivity;
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

import junit.framework.Assert;

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
        Mockito.stub(householdMock.getPhoneNumber()).toReturn("8050342");
        Mockito.stub(householdMock.getComments()).toReturn("dummy comments");

        Intent intent = new Intent();
        intent.putExtra(Constants.HOUSEHOLD, householdMock);
        householdActivity = Robolectric.buildActivity(HouseholdActivity.class).withIntent(intent).create().get();
        dbMock = mock(DatabaseHelper.class);
        dialogMock = mock(CustomDialog.class);

        androidDialogMock = Mockito.mock(android.app.Dialog.class);
        selectParticipantHandler = new SelectParticipantHandlerStub(householdActivity, householdMock, dialogMock, dbMock, androidDialogMock,Mockito.mock(View.class));
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

    @Test
    public void ShouldInActivateWhenThereAreNoMembers(){
        Mockito.stub(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).toReturn(0);

        Assert.assertTrue(selectParticipantHandler.shouldInactivate());
    }

    @Test
    public void ShouldInActivateWhenHouseholdStatusIsIncomplete(){
        Mockito.stub(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).toReturn(1);
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.INCOMPLETE);

        Assert.assertTrue(selectParticipantHandler.shouldInactivate());
    }

    @Test
    public void ShouldInActivateWhenHouseholdStatusIsDone(){
        Mockito.stub(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).toReturn(1);
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.DONE);

        Assert.assertTrue(selectParticipantHandler.shouldInactivate());
    }

    @Test
    public void ShouldInActivateWhenHouseholdStatusIsDeferred(){
        Mockito.stub(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).toReturn(1);
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.DEFERRED);

        Assert.assertTrue(selectParticipantHandler.shouldInactivate());
    }

    @Test
    public void ShouldInActivateWhenHouseholdStatusIsRefused(){
        Mockito.stub(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).toReturn(1);
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.REFUSED);

        Assert.assertTrue(selectParticipantHandler.shouldInactivate());
    }

    @Test
    public void ShouldInActivateWhenHouseholdStatusIsNotDone(){
        Mockito.stub(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).toReturn(1);
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.NOT_DONE);

        Assert.assertTrue(selectParticipantHandler.shouldInactivate());
    }

    @Test
    public void ShouldActivateWhenHouseholdStatusIsNotSelected(){
        Mockito.stub(householdMock.numberOfNonSelectedMembers(Mockito.any(DatabaseHelper.class))).toReturn(1);
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.NOT_SELECTED);

        Assert.assertFalse(selectParticipantHandler.shouldInactivate());
    }

    @Test
    public void ShouldHideTheMenuItemWhenInactivated(){
        HouseholdActivity householdActivityMock = Mockito.mock(HouseholdActivity.class);
        SelectParticipantHandler handler = new SelectParticipantHandler(householdActivityMock,householdMock, dbMock, androidDialogMock);
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(householdActivityMock.findViewById(R.id.action_select_participant)).toReturn(viewMock);

        handler.inactivate();

        Mockito.verify(viewMock).setVisibility(View.GONE);
    }

    @Test
    public void ShouldMakeTheMenuVisibleItemWhenActivated(){
        HouseholdActivity householdActivityMock = Mockito.mock(HouseholdActivity.class);
        SelectParticipantHandler handlerStub = new SelectParticipantHandler(householdActivityMock,householdMock, dbMock, androidDialogMock);
        View viewMock = Mockito.mock(View.class);
        Mockito.stub(householdActivityMock.findViewById(R.id.action_select_participant)).toReturn(viewMock);

        handlerStub.activate();

        Mockito.verify(viewMock).setVisibility(View.VISIBLE);
    }


    private class SelectParticipantHandlerStub extends SelectParticipantHandler{
        private View view;

        public SelectParticipantHandlerStub(ListActivity activity, Household household, CustomDialog dialog, DatabaseHelper db, android.app.Dialog androidDialog, View view) {
            super(activity, household, db, androidDialog);
            this.view = view;
        }

        @Override
        protected View getView() {
            return view;
        }
    }

}