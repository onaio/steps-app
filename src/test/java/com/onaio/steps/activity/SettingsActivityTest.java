package com.onaio.steps.activity;

import android.content.Intent;
import android.widget.TextView;

import com.onaio.steps.R;
import com.onaio.steps.helper.Constants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Robolectric.shadowOf;

@Config(emulateSdk = 16,manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SettingsActivityTest {

    private SettingsActivity settingsActivity;

    @Before
    public void setup(){
        settingsActivity = Robolectric.buildActivity(SettingsActivity.class).setup().get();
    }

    @Test
    public void ShouldBeAbleToPopulateViewWithSettingsLayout(){
        TextView phoneIdView = (TextView)settingsActivity.findViewById(R.id.deviceId);
        TextView endpointUrlView = (TextView)settingsActivity.findViewById(R.id.endpointUrl);
        TextView householdSeedView = (TextView)settingsActivity.findViewById(R.id.household_seed);



        Intent intentMock = Mockito.mock(Intent.class);
        Mockito.stub(intentMock.getStringExtra(Constants.PHONE_ID)).toReturn("1234");
        Mockito.stub(intentMock.getStringExtra(Constants.HOUSEHOLD_SEED)).toReturn("100");
        Mockito.stub(intentMock.getStringExtra(Constants.ENDPOINT_URL)).toReturn("http://192.168.0.120");

        settingsActivity=Robolectric.buildActivity(SettingsActivity.class).create().get();
        assertEquals(R.id.settings, shadowOf(settingsActivity).getContentView().getId());
        assertNotNull(phoneIdView);
        assertNotNull(endpointUrlView);
        assertNotNull(householdSeedView);

  //      assertEquals("1234",phoneIdView.getText());
  //      assertEquals("100",householdSeedView.getText());
  //      assertEquals("http://192.168.0.120",phoneIdView.getText().toString());
    }

}