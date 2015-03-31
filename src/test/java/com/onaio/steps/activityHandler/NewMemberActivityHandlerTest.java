package com.onaio.steps.activityHandler;


import android.app.Activity;
import android.content.Intent;

import com.onaio.steps.R;
import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.activity.NewMemberActivity;
import com.onaio.steps.adapter.MemberAdapter;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.helper.DatabaseHelper;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.Member;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class NewMemberActivityHandlerTest {
    HouseholdActivity householdActivity;
    NewMemberActivityHandler newMemberActivityHandler;
    @Before
    public  void Setup() {
        householdActivity = Robolectric.setupActivity(HouseholdActivity.class);
        Household householdMock = Mockito.mock(Household.class);
        newMemberActivityHandler = new NewMemberActivityHandler(householdActivity, householdMock);
    }

    @Test
    public void ShouldOpenActivityWhenProperMenuIdIsPassed(){
        assertTrue(newMemberActivityHandler.shouldOpen(R.id.action_add));
    }

    @Test
    public void ShouldNotOpenForOtherMenuId(){
        assertFalse(newMemberActivityHandler.shouldOpen(R.id.action_deferred));
    }

    @Test
    public void ShouldStartNewMemberActivityIfMemberIsNotNull(){
        ShadowActivity newMemberActivityShadow = Robolectric.shadowOf(householdActivity);
        assertTrue(newMemberActivityHandler.open());

        Intent newIntent = newMemberActivityShadow.getNextStartedActivityForResult().intent;
        assertTrue(newIntent.getComponent().getClassName().equals(NewMemberActivity.class.getName()));
        assertTrue(newIntent.getStringExtra(Constants.HOUSEHOLD).equals("HOUSEHOLD"));
    }


    @Test
    public void ShouldHandleResultForResultCodeOk(){
        String date = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
        Household household = new Household("2", "Any HouseholdName", "123456789", "", HouseholdStatus.NOT_SELECTED, date);
        Member member1 = new Member("Rana", "Nikhil", "Female", 20, household);
        Member member2 = new Member("Joseph", "Evan", "Female", 15, household);
        member1.save(new DatabaseHelper(householdActivity.getApplicationContext()));
        member2.save(new DatabaseHelper(householdActivity.getApplicationContext()));
        MemberAdapter memberAdapterMock = Mockito.mock(MemberAdapter.class);
        householdActivity.getListView().setAdapter(memberAdapterMock);

        newMemberActivityHandler.handleResult(null, Activity.RESULT_OK);

        Mockito.verify(memberAdapterMock).reinitialize(Mockito.anyList());
        Mockito.verify(memberAdapterMock).notifyDataSetChanged();
    }

    @Test
    public void ShouldNotHandleResultForOtherResultCode(){
        MemberAdapter memberAdapterMock = Mockito.mock(MemberAdapter.class);
        householdActivity.getListView().setAdapter(memberAdapterMock);

        newMemberActivityHandler.handleResult(null, Activity.RESULT_OK);

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