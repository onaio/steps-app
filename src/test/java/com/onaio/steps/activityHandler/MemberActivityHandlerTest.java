package com.onaio.steps.activityHandler;

import android.content.Intent;

import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.activity.MemberActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Gender;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.HouseholdStatus;
import com.onaio.steps.model.Member;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class MemberActivityHandlerTest {

    @Mock
    HouseholdActivity householdActivity;
    MemberActivityHandler memberActivityHandler;
    private Member member;
    private String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());

    @Before
    public void SetUp() {
        householdActivity = Mockito.mock(HouseholdActivity.class);
    }

    @Test
    public void ShouldStartMemberActivityWhenMemberIsNotNull(){
        Household household = new Household("2", "Any HouseholdName", "123456789", "", HouseholdStatus.NOT_SELECTED, currentDate,"Dummy comments");
        member = new Member("Rana", "Nikhil", Gender.Female, 20, household, false);
        memberActivityHandler = new MemberActivityHandler(householdActivity, member);

        memberActivityHandler.open();

        Mockito.verify(householdActivity).startActivity(Mockito.argThat(matchIntent()));
    }

    private ArgumentMatcher<Intent> matchIntent() {
        return new ArgumentMatcher<Intent>() {
            @Override
            public boolean matches(Object argument) {
                Intent intent = (Intent) argument;
                Member actualMember = (Member) intent.getSerializableExtra(Constants.MEMBER);
                Assert.assertEquals(member,actualMember);
                Assert.assertEquals(MemberActivity.class.getName(),intent.getComponent().getClassName());
                return true;
            }
        };
    }

    @Test
    public void ShouldNotStartMemberActivityWhenMemberIsNull(){
        member=null;
        memberActivityHandler = new MemberActivityHandler(householdActivity, member);

        memberActivityHandler.open();
        Mockito.verify(householdActivity,Mockito.never()).startActivity(Mockito.any(Intent.class));
    }
}