package com.onaio.steps.activityHandler;


import com.onaio.steps.activity.HouseholdActivity;
import com.onaio.steps.model.Household;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class HouseholdActivityHandlerTest {

    HouseholdActivity householdActivity;
    Household householdMock;

    @Before
    public void Setup(){
        householdActivity = Robolectric.setupActivity(HouseholdActivity.class);
        householdMock = Mockito.mock(Household.class);
        new HouseholdActivityHandler(householdActivity,householdMock);
    }

    @Test
    public void Should(){}


}