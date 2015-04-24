package com.onaio.steps.activityHandler;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;

import com.onaio.steps.R;
import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.activity.NewMemberActivity;
import com.onaio.steps.adapter.MemberAdapter;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.RequestCode;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class NewMemberActivityHandlerTest {
    private DatabaseHelper dbMock;
    HouseholdActivity householdActivityMock;
    Household householdMock;
    NewMemberActivityHandler newMemberActivityHandler;
    @Mock
    private MemberAdapter memberAdapterMock;


    @Before
    public  void Setup() {
        dbMock = Mockito.mock(DatabaseHelper.class);
        householdActivityMock = Mockito.mock(HouseholdActivity.class);
        householdMock = Mockito.mock(Household.class);
        memberAdapterMock = Mockito.mock(MemberAdapter.class);
        newMemberActivityHandler = new NewMemberActivityHandler(householdMock,householdActivityMock, memberAdapterMock, dbMock);
    }

    @Test
    public void ShouldOpenActivityWhenProperMenuIdIsPassedAndWhenSurveyIsNotRefused(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.NOT_DONE);
        assertTrue(newMemberActivityHandler.shouldOpen(R.id.action_add_member));
    }

    @Test
    public void ShouldNotOpenForOtherMenuIdAndForRefusedState(){
        Mockito.stub(householdMock.getStatus()).toReturn(HouseholdStatus.REFUSED);
        assertFalse(newMemberActivityHandler.shouldOpen(R.id.action_deferred));
    }

    @Test
    public void ShouldStartNewMemberActivityIfHouseholdIsNotNull() {
        newMemberActivityHandler.open();

        Mockito.verify(householdActivityMock).startActivityForResult(Mockito.argThat(matchIntent()), Mockito.eq(RequestCode.NEW_MEMBER.getCode()));
    }

    private ArgumentMatcher<Intent> matchIntent() {
        return new ArgumentMatcher<Intent>() {
            @Override
            public boolean matches(Object argument) {
                Intent intent = (Intent) argument;
                Household actualHousehold = (Household) intent.getSerializableExtra(Constants.HOUSEHOLD);
                Assert.assertEquals(householdMock, actualHousehold);
                Assert.assertEquals(NewMemberActivity.class.getName(),intent.getComponent().getClassName());
                return true;
            }
        };
    }

    @Test
    public void ShouldReIntialiseViewResultForResultCodeOk(){
        Cursor cursorMock = Mockito.mock(Cursor.class);
        Mockito.stub(dbMock.exec(Mockito.anyString())).toReturn(cursorMock);
        newMemberActivityHandler.handleResult(null, Activity.RESULT_OK);

        Mockito.verify(memberAdapterMock).reinitialize(Mockito.anyList());
        Mockito.verify(memberAdapterMock).notifyDataSetChanged();
    }

    @Test
    public void ShouldNotReIntialiseViewForOtherResultCode(){
        newMemberActivityHandler.handleResult(null, Activity.RESULT_CANCELED);

        Mockito.verify(memberAdapterMock,Mockito.never()).reinitialize(Mockito.anyList());
        Mockito.verify(memberAdapterMock,Mockito.never()).notifyDataSetChanged();
    }

    @Test
    public void ShouldBeAbleToHandleResultForProperRequestCode(){
        assertTrue(newMemberActivityHandler.canHandleResult(RequestCode.NEW_MEMBER.getCode()));
    }


    @Test
    public void ShouldNotBeAbleToHandleResultForOtherRequestCode(){
        assertTrue(newMemberActivityHandler.canHandleResult(RequestCode.NEW_MEMBER.getCode()));
    }

    @Test
    public void ShouldInactivateWhenHouseholdIsSurveyed(){
        stub(householdMock.getSelectedMemberId()).toReturn("");
        stub(householdMock.getStatus()).toReturn(HouseholdStatus.DONE);
        Assert.assertTrue(newMemberActivityHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenHouseholdSurveyIsIncomplete(){
        stub(householdMock.getSelectedMemberId()).toReturn("");
        stub(householdMock.getStatus()).toReturn(HouseholdStatus.INCOMPLETE);
        Assert.assertTrue(newMemberActivityHandler.shouldInactivate());
    }

    @Test
    public void ShouldInactivateWhenSurveyIsRefused(){
        stub(householdMock.getSelectedMemberId()).toReturn("");
        stub(householdMock.getStatus()).toReturn(HouseholdStatus.REFUSED);
        Assert.assertTrue(newMemberActivityHandler.shouldInactivate());
    }

    @Test
    public void ShouldDisableItemWhenInactivated(){
        View viewMock = Mockito.mock(Button.class);
        stub(householdActivityMock.findViewById(R.id.action_add_member)).toReturn(viewMock);

        newMemberActivityHandler.inactivate();

        verify(viewMock).setVisibility(View.GONE);
    }

    @Test
    public void ShouldShowItemWhenActivated(){
        View viewMock = Mockito.mock(Button.class);
        stub(householdActivityMock.findViewById(R.id.action_add_member)).toReturn(viewMock);

        newMemberActivityHandler.activate();

        verify(viewMock).setVisibility(View.VISIBLE);
    }



}