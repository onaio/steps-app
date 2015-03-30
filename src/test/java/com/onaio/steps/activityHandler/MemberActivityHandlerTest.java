package com.onaio.steps.activityHandler;

import android.content.Intent;

import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.activity.MemberActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Member;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertTrue;

public class MemberActivityHandlerTest {

    HouseholdActivity householdActivity;
    MemberActivityHandler memberActivityHandler;
    @Before
    public void setUp() throws Exception {
        householdActivity = Robolectric.setupActivity(HouseholdActivity.class);
        Member member = Mockito.mock(Member.class);
        memberActivityHandler = new MemberActivityHandler(householdActivity, member);
    }
    @Test
    public void ShouldOpen(){
        ShadowActivity shadowActivity = Robolectric.shadowOf(householdActivity);
    //    assertTrue(memberActivityHandler.open());
        memberActivityHandler.open();
        Intent newIntent = shadowActivity.getNextStartedActivityForResult().intent;
        assertTrue(newIntent.getComponent().getClassName().equals(MemberActivity.class.getName()));
      //assertTrue(newIntent.getStringExtra(Constants.MEMBER_IDENTIFIER.toString).equals());
    }
}