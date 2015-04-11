package com.onaio.steps.activityHandler;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;

import com.onaio.steps.R;
import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.activity.NewMemberActivity;
import com.onaio.steps.adapter.MemberAdapter;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;

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

        Mockito.verify(householdActivityMock).startActivityForResult(Mockito.argThat(matchIntent()), Mockito.eq(Constants.NEW_MEMBER_IDENTIFIER));
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
        assertTrue(newMemberActivityHandler.canHandleResult(Constants.NEW_MEMBER_IDENTIFIER));
    }


    @Test
    public void ShouldNotBeAbleToHandleResultForOtherRequestCode(){
        assertTrue(newMemberActivityHandler.canHandleResult(Constants.NEW_MEMBER_IDENTIFIER));
    }
}