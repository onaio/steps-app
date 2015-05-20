package com.onaio.steps.activityHandler;


import android.content.Intent;

import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.activity.HouseholdListActivity;
import com.onaio.steps.helper.Constants;
import com.onaio.steps.model.Household;
import com.onaio.steps.model.InterviewStatus;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class HouseholdActivityHandlerTest {

    HouseholdListActivity householdListActivity;
    HouseholdActivityHandler householdActivityHandler;
    Household household;
    private String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());


    @Before
    public void Setup(){
        householdListActivity = Mockito.mock(HouseholdListActivity.class);
    }

    @Test
    public void ShouldStartNewMemberActivityIfHouseholdIsNotNull(){
        household = new Household("2", "Any HouseholdName", "123456789", "", InterviewStatus.NOT_SELECTED, currentDate,"Dummy comments");
        householdActivityHandler = new HouseholdActivityHandler(householdListActivity, household);
        householdActivityHandler.open();

        Mockito.verify(householdListActivity).startActivity(Mockito.argThat(matchIntent()));
    }

    @Test
    public void ShouldNotStartNewMemberActivityIfHouseholdIsNull(){
        householdActivityHandler = new HouseholdActivityHandler(householdListActivity, null);
        householdActivityHandler.open();

        Mockito.verify(householdListActivity,Mockito.never()).startActivity(Mockito.any(Intent.class));
    }


    private ArgumentMatcher<Intent> matchIntent() {
        return new ArgumentMatcher<Intent>() {
            @Override
            public boolean matches(Object argument) {
                Intent intent = (Intent) argument;
                Household actualHousehold = (Household) intent.getSerializableExtra(Constants.HOUSEHOLD);
                Assert.assertEquals(household, actualHousehold);
                Assert.assertEquals(HouseholdActivity.class.getName(),intent.getComponent().getClassName());
                return true;
            }
        };
    }
}