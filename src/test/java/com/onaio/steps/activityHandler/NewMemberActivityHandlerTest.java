package com.onaio.steps.activityHandler;


import com.onaio.steps.R;
import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.adapter.MemberAdapter;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

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
    public void ShouldOpenWhenProperMenuIdIsPassed(){
        assertTrue(newMemberActivityHandler.shouldOpen(R.id.action_add));
    }

    @Test
    public void ShouldNotOpenForOtherMenuId(){
        assertFalse(newMemberActivityHandler.shouldOpen(R.id.action_deferred));
    }

    @Test
    public void ShouldOpen(){
        assertTrue(newMemberActivityHandler.open());
    }

    @Test
    public void ShouldHandleResultForResultCodeOk(){
        MemberAdapter memberAdapterMock = Mockito.mock(MemberAdapter.class);
        householdActivity.getListView().setAdapter(memberAdapterMock);

    }

    @Test
    public void ShouldBeAbleToHandleResultForProperRequestCode(){
        assertTrue(newMemberActivityHandler.canHandleResult(Constants.NEW_MEMBER_IDENTIFIER));
    }

    @Test
    public void ShouldNotBeAbleToHandleResultForOtherRequestCode(){
        assertTrue(newMemberActivityHandler.canHandleResult(Constants.MEMBER_IDENTIFIER));
    }



}